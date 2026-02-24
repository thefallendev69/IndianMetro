# Unit Test Guidelines

This document defines the standard pattern for writing unit tests across the project.

Primary reference implementation:
- `features/auth/src/commonTest/kotlin/com/thefallendeveloper/indianmetro/features/auth/OtpEntryViewModelTests.kt`

## Architecture

Use these rules for any new unit test class.

1. Class structure
- Keep one test class per production class.
- Follow filename/classname parity when a file contains a single class.
- Keep constants in `SCREAMING_SNAKE_CASE`.

2. Dependency setup
- Use `corecommon:baseTest` delegates for common lifecycle and test infrastructure.
- Prefer delegate composition in the test class:
  - `ManagedTestLifecycleHooks`
  - `CoroutineSupport by CoroutineTest()`
  - `KoinSupport by KoinTestSupport()`
  - `BaseTestSupport by BaseTest()`

3. Koin-based SUT creation
- Define a top-level test module above the test class in the same file.
- Start test Koin context in `@BeforeTest` with `startKoinForTest(...)`.
- Resolve SUT and collaborators from Koin (`get`, `parametersOf`) rather than constructing inline in each test.
- Stop Koin in `@AfterTest` via `stopKoinForTest()`.

4. Test lifecycle pattern
- `@BeforeTest`:
  - call `setUpManagedTestLifecycle()`
  - start Koin and resolve required instances
- `@AfterTest`:
  - call `stopKoinForTest()`
  - call `tearDownManagedTestLifecycle()`

5. State/event verification (MVI)
- Validate initial state explicitly.
- Drive behavior via events (not internal method calls).
- For async state updates, wait on `StateFlow` predicates (`first { ... }`) before asserting.
- For navigation/event-stream behavior, capture emissions deterministically (`first()`, deferred collection, then assert).

## Recommended Test Template

```kotlin
private val featureTestModule =
    module {
        // Test bindings
    }

class FeatureViewModelTests :
    KoinTest,
    ManagedTestLifecycleHooks,
    CoroutineSupport by CoroutineTest(),
    KoinSupport by KoinTestSupport(),
    BaseTestSupport by BaseTest() {

    private lateinit var viewModel: FeatureViewModel

    @BeforeTest
    fun setUp() {
        setUpManagedTestLifecycle()
        startKoinForTest(featureTestModule)
        viewModel = get()
    }

    @AfterTest
    fun tearDown() {
        stopKoinForTest()
        tearDownManagedTestLifecycle()
    }
}
```

## Verification Checklist

- Unit tests run successfully from Gradle and IDE.
- No hardcoded ad-hoc setup logic duplicated across tests when delegate support exists.
