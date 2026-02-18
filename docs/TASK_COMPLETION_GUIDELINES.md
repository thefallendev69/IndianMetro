# Task Completion Guidelines

## Task Definition
Every task must include:
1. EndGoal
2. Verification steps

### Task Input Template
Use this shape when defining a task:

```text
EndGoal:
<single clear sentence describing the final outcome>

Verification steps:
1. <how to verify behavior/functionality>
2. <build/lint/test commands to run>
3. <expected result>
```

EndGoal should describe the outcome, not the implementation detail.
Verification steps should be specific, reproducible, and executable from the repository root.

## Execution Flow
1. Create a branch named from `EndGoal` using `snake_case`.
2. Plan the changes and break the task into smaller sub-tasks when needed.
3. For each task/sub-task, follow the Autonomous workflow.
4. Raise a pull request targeting `main`.

## Branch Naming
- Format: `<end_goal_in_snake_case>`
- Example:
  - EndGoal: "Refactor auth navigation routes"
  - Branch: `refactor_auth_navigation_routes`

## Planning Expectations
Before coding:
1. Identify impacted modules and files.
2. Define dependency or architecture implications.
3. Break work into sub-tasks if:
   - more than one module is impacted,
   - the change includes architecture + implementation,
   - migration/refactor touches existing behavior.

Each sub-task should have:
1. A concrete outcome.
2. A short verification step.
3. A clear "done" condition.

## Autonomous Workflow Per Task/Sub-task
For every task or sub-task:
1. Implement the required change end-to-end.
2. Verify impacted code compiles and behaves as expected.
3. Check alignment with architecture and project conventions.
4. Run lint/test/build checks relevant to the change.
5. Fix issues and re-run checks until green.

Recommended command strategy:
1. Start with impacted module checks.
2. Run `:composeApp` verification for integration impact.
3. For cross-module/structural changes, run broader verification (`check` or `build`).

## Pull Request Expectations
Open a pull request to `main` after checks pass.
Include:
1. EndGoal summary.
2. Change summary by module.
3. Verification commands executed.
4. Test/lint/build outcome.
5. Any known limitations or follow-up items.

## Definition of Done
A task is done only when:
1. EndGoal is achieved.
2. Verification steps pass.
3. Architecture/convention requirements are satisfied.
4. Changes are pushed on a branch and PR is raised to `main`.
