#!/usr/bin/env python3
import argparse
import os
import re
import subprocess
import sys
from glob import glob
import xml.etree.ElementTree as ET
from collections import defaultdict
from pathlib import Path

COMMENT_PREFIXES = ("//", "/*", "*", "*/")


def run_git_diff(base_ref: str) -> str:
    cmd = [
        "git",
        "diff",
        "--unified=0",
        "--diff-filter=AM",
        f"origin/{base_ref}...HEAD",
    ]
    result = subprocess.run(cmd, check=True, capture_output=True, text=True)
    return result.stdout


def parse_changed_lines(diff_text: str):
    changed = defaultdict(dict)
    current_file = None
    current_new_line = None

    for raw_line in diff_text.splitlines():
        line = raw_line.rstrip("\n")

        if line.startswith("+++ b/"):
            current_file = line[len("+++ b/") :]
            current_new_line = None
            continue

        if line.startswith("@@"):
            match = re.search(r"\+(\d+)(?:,(\d+))?", line)
            if not match:
                current_new_line = None
                continue
            current_new_line = int(match.group(1))
            continue

        if current_file is None or current_new_line is None:
            continue

        if line.startswith("+") and not line.startswith("+++"):
            changed[current_file][current_new_line] = line[1:]
            current_new_line += 1
        elif line.startswith("-") and not line.startswith("---"):
            continue
        else:
            current_new_line += 1

    return changed


def parse_coverage_reports(report_paths):
    coverage_by_key = {}

    for report_path in report_paths:
        tree = ET.parse(report_path)
        root = tree.getroot()

        for package in root.findall(".//package"):
            package_name = package.attrib.get("name", "")
            for sourcefile in package.findall("sourcefile"):
                filename = sourcefile.attrib.get("name")
                if not filename:
                    continue
                key = f"{package_name}/{filename}" if package_name else filename
                line_map = coverage_by_key.setdefault(key, {})
                for line in sourcefile.findall("line"):
                    nr = int(line.attrib.get("nr", "0"))
                    ci = int(line.attrib.get("ci", "0"))
                    line_map[nr] = ci > 0

    return coverage_by_key


def is_excluded_file(path: str) -> bool:
    lower = path.lower()
    filename = os.path.basename(lower)

    if "/build/" in lower or "/generated/" in lower or "/gen/" in lower:
        return True
    if "/di/" in lower:
        return True
    if filename.endswith("module.kt"):
        return True
    if "generated" in filename:
        return True
    return False


def is_non_code_line(line: str) -> bool:
    stripped = line.strip()
    if not stripped:
        return True
    if stripped in {"{", "}", "(", ")", "[", "]", ","}:
        return True
    if stripped.startswith(COMMENT_PREFIXES):
        return True
    return False


def file_to_coverage_key(path: str) -> str:
    marker = "/kotlin/"
    if marker in path:
        return path.split(marker, 1)[1]
    return path


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--base-ref", required=True)
    parser.add_argument("--threshold", type=float, default=85.0)
    parser.add_argument("--report", action="append", default=[])
    parser.add_argument("--report-glob", action="append", default=[])
    args = parser.parse_args()

    report_candidates = list(args.report)
    for pattern in args.report_glob:
        report_candidates.extend(glob(pattern, recursive=True))

    report_paths = [Path(p) for p in report_candidates if Path(p).exists()]
    if not report_paths:
        print("No coverage report files found.")
        sys.exit(1)

    diff_text = run_git_diff(args.base_ref)
    changed_lines = parse_changed_lines(diff_text)
    coverage = parse_coverage_reports(report_paths)

    total = 0
    covered = 0

    for file_path, lines in changed_lines.items():
        if not file_path.endswith(".kt"):
            continue
        if is_excluded_file(file_path):
            continue

        key = file_to_coverage_key(file_path)
        coverage_for_file = coverage.get(key, {})

        for line_number, line_text in lines.items():
            if is_non_code_line(line_text):
                continue

            total += 1
            if coverage_for_file.get(line_number, False):
                covered += 1

    if total == 0:
        print("No eligible changed Kotlin code lines found for coverage gate. Passing.")
        sys.exit(0)

    percent = (covered / total) * 100
    print(
        f"Changed-lines coverage: {percent:.2f}% (covered {covered} / total {total}), threshold {args.threshold:.2f}%"
    )

    if percent < args.threshold:
        print("Coverage gate failed.")
        sys.exit(1)

    print("Coverage gate passed.")


if __name__ == "__main__":
    main()
