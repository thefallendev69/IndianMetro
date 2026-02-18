# Corecommon Usage Guide

## Purpose
`corecommon` contains reusable contracts and cross-feature building blocks.

Current modules:
- `corecommon/libs`
- `corecommon/designsystem`

## `corecommon/libs`

### Navigation Primitives
- `BaseRoute`
  - Base class for all navigation route models.
  - Exposes `route: String`.
  - Includes shared route-arg encoding/decoding extensions.
- `AppRoutes`
  - App-level routes extending `BaseRoute`.
  - Use this for cross-feature/app-shell navigation.
- `FeatureNavigator<Route : BaseRoute>`
  - Pushes route destinations through a shared flow.
  - Use `navigateTo(route)` from ViewModels or coordinators.
- `FeatureNavigatorSubscription`
  - Subscribes to a `FeatureNavigator` and calls `NavController.navigate(route.route)`.
  - No route mapper required.

### Koin helpers
- `featureNavigatorModule<Route : BaseRoute>(qualifier)`
  - Registers typed `FeatureNavigator` instances.
  - Use qualifiers for multiple navigators (`@AppNavigator`, `@AuthNavigator`, etc.).

## `corecommon/designsystem`
- Use this module for:
  - theme setup,
  - color/typography/spacing tokens,
  - reusable UI components.
- Features should consume design-system APIs, not duplicate token values.

## How Features Should Consume `corecommon`

### 1) Define routes
- Create feature routes extending `BaseRoute`.
- Keep route constants and argument contracts in feature navigation package.

### 2) Register navigators
- Add navigator modules in feature DI using `featureNavigatorModule`.
- Use qualifiers to avoid navigator collisions.

### 3) Subscribe in route entry
- In feature root composable, inject `FeatureNavigator<FeatureRoutes>`.
- Call `FeatureNavigatorSubscription(navHostController, featureNavigator)`.

### 4) Navigate from ViewModel
- Emit typed route objects via navigator:
  - `featureNavigator.navigateTo(FeatureRoutes.SomeDestination(...))`

## Do / Don't
- Do keep feature code dependent on `corecommon`, not on other features.
- Do keep route arguments typed.
- Do use `AppRoutes` for app-shell transitions.
- Do not hardcode route strings in multiple places.
- Do not use ad-hoc mapping layers when route already exists on `BaseRoute`.
