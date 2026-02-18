# Architecture Guidelines

## Scope
This document defines project-wide architecture rules, aligned with the current `features/auth` implementation.

## Module Structure
- Use feature-first modules.
- Feature modules must be independent from each other.
- Shared utilities and contracts must live under `corecommon`.
- Keep UI tokens/components in design system modules, not in features.

## UI Architecture (MVI)
- Every screen's `ViewModel` must define:
  - `State`: full UI state for that screen.
  - `Event`: sealed class describing all user/system actions.
- `ViewModel` receives events and reduces state.
- Keep composables dumb: render state + forward user actions as events.

## Navigation Architecture
- All route classes must extend `BaseRoute`.
- `FeatureNavigator` only handles `BaseRoute` types.
- `FeatureNavigationSubscription` navigates using `route.route` directly.
- Do not introduce route string wrappers when routes already derive from `BaseRoute`.
- Use route args as typed objects, encoded/decoded via `BaseRoute` extensions.

## Dependency Injection
- Use Koin for feature and app wiring.
- Register navigators as singletons.
- Register screen `ViewModel` dependencies in feature module DI.
- When runtime params are required (for example `phoneNumber`), use Koin parameters.

## Composable Rules
- Composable destination blocks should stay minimal.
- Screen composables should accept `ViewModel` as parameter.
- Use default `koinViewModel()` only when no runtime parameter is needed.
- For parameterized screens, inject `ViewModel` at route level and pass it down.

## Code Conventions
- Base package: `com.thefallendeveloper.indianmetro`.
- Use string resources instead of hardcoded strings in composables.
- Use `SCREAMING_SNAKE_CASE` for constants.
- If a file has one class, file name must match class name.
- Keep lint clean (`ktlint`, `detekt`) before merging.

## Validation Workflow
- Implement task.
- Verify impacted modules compile.
- Verify architecture conformance.
- Run lint (`ktlintCheck`, `detekt`) and fix issues.
- Run broader verification (`check` / `build`) for cross-module changes.
- Commit with a meaningful message.
