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


def detect_pr_base_ref(explicit_base_ref: str | None) -> str:
    if explicit_base_ref:
        return explicit_base_ref

    github_base_ref = os.getenv("GITHUB_BASE_REF")
    if github_base_ref:
        return github_base_ref

    try:
        result = subprocess.run(
            ["gh", "pr", "view", "--json", "baseRefName", "--jq", ".baseRefName"],
            check=True,
            capture_output=True,
            text=True,
        )
        base_ref = result.stdout.strip()
        if base_ref:
            return base_ref
    except Exception:
        pass

    return "main"


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


def is_excluded_by_path(file_path: str, excluded_paths: list[str]) -> bool:
    normalized_path = file_path.replace("\\", "/")
    return any(pattern in normalized_path for pattern in excluded_paths)


def is_excluded_by_package(coverage_key: str, excluded_packages: list[str]) -> bool:
    package_part = coverage_key.rsplit("/", 1)[0] if "/" in coverage_key else ""
    package_name = package_part.replace("/", ".")
    return any(package_name.startswith(prefix) for prefix in excluded_packages)


def is_non_code_line(line: str) -> bool:
    stripped = line.strip()
    if not stripped:
        return True
    if stripped.startswith("import ") or stripped.startswith("package "):
        return True
    if stripped in {"{", "}", "(", ")", "[", "]", ","}:
        return True
    if stripped.startswith(COMMENT_PREFIXES):
        return True
    return False


def to_line_ranges(line_numbers):
    if not line_numbers:
        return []
    sorted_numbers = sorted(line_numbers)
    ranges = []
    start = sorted_numbers[0]
    end = sorted_numbers[0]

    for line_number in sorted_numbers[1:]:
        if line_number == end + 1:
            end = line_number
        else:
            ranges.append((start, end))
            start = line_number
            end = line_number

    ranges.append((start, end))
    return ranges


def class_name_from_path(file_path: str) -> str:
    return Path(file_path).stem


def file_to_coverage_key(path: str) -> str:
    marker = "/kotlin/"
    if marker in path:
        return path.split(marker, 1)[1]
    return path


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--base-ref")
    parser.add_argument("--threshold", type=float, default=85.0)
    parser.add_argument("--report", action="append", default=[])
    parser.add_argument("--report-glob", action="append", default=[])
    parser.add_argument("--exclude-package", action="append", default=[])
    parser.add_argument("--exclude-path", action="append", default=[])
    args = parser.parse_args()

    report_candidates = list(args.report)
    for pattern in args.report_glob:
        report_candidates.extend(glob(pattern, recursive=True))

    report_paths = [Path(p) for p in report_candidates if Path(p).exists()]
    if not report_paths:
        print("No coverage report files found.")
        sys.exit(1)

    base_ref = detect_pr_base_ref(args.base_ref)
    print(f"Using PR base ref: {base_ref}")
    diff_text = run_git_diff(base_ref)
    changed_lines = parse_changed_lines(diff_text)
    coverage = parse_coverage_reports(report_paths)

    total = 0
    covered = 0
    file_stats = defaultdict(lambda: {"total": 0, "covered": 0, "uncovered": []})
    skipped_not_in_report = []

    for file_path, lines in changed_lines.items():
        if not file_path.endswith(".kt"):
            continue
        if is_excluded_by_path(file_path, args.exclude_path):
            continue

        key = file_to_coverage_key(file_path)
        if is_excluded_by_package(key, args.exclude_package):
            continue

        coverage_for_file = coverage.get(key)
        if coverage_for_file is None:
            skipped_not_in_report.append(file_path)
            continue

        for line_number, line_text in lines.items():
            if is_non_code_line(line_text):
                continue

            total += 1
            file_stats[file_path]["total"] += 1
            if coverage_for_file.get(line_number, False):
                covered += 1
                file_stats[file_path]["covered"] += 1
            else:
                file_stats[file_path]["uncovered"].append((line_number, line_text.strip()))

    if total == 0:
        print("No eligible changed Kotlin code lines found for coverage gate. Passing.")
        sys.exit(0)

    percent = (covered / total) * 100
    print(
        f"Changed-lines coverage: {percent:.2f}% (covered {covered} / total {total}), threshold {args.threshold:.2f}%"
    )

    print("Coverage by file:")
    for file_path in sorted(file_stats.keys()):
        stats = file_stats[file_path]
        file_percent = (stats["covered"] / stats["total"] * 100) if stats["total"] else 0.0
        print(
            f"  - {file_path}: {file_percent:.2f}% (covered {stats['covered']} / total {stats['total']})"
        )
        if stats["uncovered"]:
            line_ranges = to_line_ranges([line_number for line_number, _ in stats["uncovered"]])
            print("    Uncovered ranges:")
            class_name = class_name_from_path(file_path)
            for start, end in line_ranges:
                print(f"      {class_name} ({start} - {end})")

    if skipped_not_in_report:
        print("Skipped files (not present in Kover report):")
        for file_path in sorted(set(skipped_not_in_report)):
            print(f"  - {file_path}")

    if percent < args.threshold:
        print("Coverage gate failed.")
        sys.exit(1)

    print("Coverage gate passed.")


if __name__ == "__main__":
    main()
