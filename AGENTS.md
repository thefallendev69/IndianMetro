# Agent Instructions

Use `PROJECT_CONTEXT.md` as the primary repository context document.
Keep it updated when architecture, modules, or conventions change.

Required conventions:
- Use MVI architecture.
- Use SCREAMING_SNAKE_CASE for constants.
- If a file contains a single class, filename must match class name.

Autonomous workflow:
- Implement the required task end-to-end.
- Verify the implemented task works.
- Ensure the solution adheres to architectural guidelines and project conventions.
- Run final build verification for impacted modules and `composeApp`; for broad refactors run full `./gradlew build`.
- Run lint checks, fix issues if needed, and re-run checks.
- Commit with a meaningful message and push.

Task completion flow:
- Every task definition must include:
  - `EndGoal`
  - `Verification steps`
- Start by creating a branch from `EndGoal` using `snake_case` naming.
- Plan the work and split into sub-tasks when needed.
- For each sub-task, apply the autonomous workflow above.
- When all checks are green, push and raise a pull request targeting `main`.

Reference:
- `docs/TASK_COMPLETION_GUIDELINES.md`
