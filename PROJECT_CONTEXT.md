# IndianMetro Project Context

## Overview
- Kotlin Multiplatform project with Compose Multiplatform UI.
- App module: `composeApp`
- Shared UI/design module: `designsystem`
- Feature modules: `features/auth`, `features/onboarding`
- iOS host app: `iosApp`
- Build logic plugin module: `build-logic/linting`

## Architecture
- Feature-first modular structure.
- Features should remain independent from each other.
- Dependency injection uses Koin.
- Navigation is being migrated to Compose Navigation (`nav2`).
- UI architecture pattern: MVI.
- Architecture reference docs:
  - `docs/ARCHITECTURE_GUIDELINES.md`
  - `docs/CORECOMMON_USAGE.md`
  - `docs/TASK_COMPLETION_GUIDELINES.md`

## Conventions
- Base package: `com.thefallendeveloper.indianmetro`
- Linting uses ktlint and detekt via build-logic convention plugins.
- Prefer string resources over hardcoded UI strings.
- Always use SCREAMING_SNAKE_CASE for constants.
- If a file contains only one class, the filename must match the class name.

## Current Navigation Direction
- Route models extend `BaseRoute`.
- `FeatureNavigator` is typed as `FeatureNavigator<Route : BaseRoute>`.
- `FeatureNavigatorSubscription` consumes `FeatureNavigator<out BaseRoute>` and navigates via `route.route`.
- Use typed route args, encoded/decoded through shared `BaseRoute` extensions.

## Notes
- Keep this file updated as decisions evolve.

## Autonomous Workflow
- Implement the required task end-to-end.
- Verify the implemented task works.
- Ensure the solution adheres to architecture and project conventions.
- Run final build verification for impacted modules and `composeApp`; for cross-module or structural refactors run `./gradlew build`.
- Run lint checks, fix issues if required, and re-run verification.
- Commit with a meaningful message and push.
