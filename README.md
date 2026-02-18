This is a Kotlin Multiplatform project targeting Android, iOS.

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
    folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

### Build and Run Android Application

To build and run the development version of the Android app:

1. Build debug APK
   ```bash
   ./gradlew :composeApp:assembleDebug
   ```
2. Install/run from Android Studio device selector, or:
   ```bash
   ./gradlew :composeApp:installDebug
   ```

Optional full module verification:
```bash
./gradlew :composeApp:assembleDebug :composeApp:compileKotlinIosSimulatorArm64
```

### Build and Run iOS Application

You can run iOS either from Xcode UI or terminal.

#### Xcode flow

1. Open [/iosApp](./iosApp) in Xcode.
2. Select `iosApp` scheme.
3. Pick an iOS Simulator device.
4. Run.

#### Terminal flow

1. Build for simulator
   ```bash
   xcodebuild \
     -project iosApp/iosApp.xcodeproj \
     -scheme iosApp \
     -configuration Debug \
     -destination 'platform=iOS Simulator,name=iPhone 16,OS=18.2' \
     build
   ```
2. Boot simulator and open Simulator app
   ```bash
   xcrun simctl list devices available
   DEVICE_ID="$(xcrun simctl list devices available | grep 'iPhone 16 (' | head -n 1 | sed -E 's/.*\\(([A-F0-9-]+)\\).*/\\1/')"
   xcrun simctl boot "$DEVICE_ID" || true
   open -a Simulator
   ```
3. Install and launch app
   ```bash
   APP_PATH="$(find ~/Library/Developer/Xcode/DerivedData -path '*Build/Products/Debug-iphonesimulator/IndianMetro.app' | head -n 1)"
   xcrun simctl install "$DEVICE_ID" "$APP_PATH"
   xcrun simctl launch "$DEVICE_ID" com.thefallendeveloper.indianmetro.IndianMetro
   ```
4. Verify process is running
   ```bash
   xcrun simctl spawn "$DEVICE_ID" launchctl list | grep 'UIKitApplication:com.thefallendeveloper.indianmetro.IndianMetro'
   ```

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
