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

## Conventions
- Base package: `com.thefallendeveloper.indianmetro`
- Linting uses ktlint and detekt via build-logic convention plugins.
- Prefer string resources over hardcoded UI strings.
- Always use SCREAMING_SNAKE_CASE for constants.
- If a file contains only one class, the filename must match the class name.

## Current Navigation Direction
- Each feature can define:
  - `FeatureNavigator` (class, singleton via DI; not object)
  - `NavigationRoutes`
  - `FeatureNavigationSubscription` composable
- Feature navigators expose:
  - private `MutableStateFlow`
  - public `StateFlow`
  - public push method for route updates

## Notes
- Keep this file updated as decisions evolve.
