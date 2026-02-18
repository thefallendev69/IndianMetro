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
- Start by creating a branch from `EndGoal` using `snake_case` naming. The branch should be cut from the latest `main` branch commit
- Plan the work and split into sub-tasks when needed.
- For each sub-task, apply the autonomous workflow above.
- When all checks are green, push and raise a pull request targeting `main`.
- PR title format must start with `[CODEX PR]`.

PR comment handling flow:
- Read all PR comments and make sense of requested changes before editing code.
- Pull latest `main` into the current feature branch before addressing comments.
- If merge conflicts happen, try to resolve them.
- If conflicts are not resolvable safely, ask a human to resolve conflicts.
- After the branch is up to date, resolve review comments one by one.
- If a comment cannot be resolved, add a reply on that comment explaining what is blocked and why.
- Once comment fixes are done, run verification steps again.
- Push the branch and notify the human reviewer for re-review.

Reference:
- `docs/TASK_COMPLETION_GUIDELINES.md`
- `README.md`:
  - `Build and Run Android Application`
  - `Build and Run iOS Application`
