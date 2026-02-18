# iOS Simulator Build and Run

This runbook documents verified terminal steps to build and run the iOS app on simulator.

## Prerequisites

- macOS with Xcode installed.
- Xcode Command Line Tools available (`xcodebuild`, `xcrun`).
- iOS simulator runtime installed (used below: iOS 18.2).

## Build

From repository root:

```bash
xcodebuild \
  -project iosApp/iosApp.xcodeproj \
  -scheme iosApp \
  -configuration Debug \
  -destination 'platform=iOS Simulator,name=iPhone 16,OS=18.2' \
  build
```

Expected result:
- Gradle framework embedding succeeds.
- Xcode ends with `** BUILD SUCCEEDED **`.

## Boot Simulator

```bash
xcrun simctl boot 049FF56C-D239-4B50-A65B-A7FB83C5888A || true
open -a Simulator
```

Find device IDs:

```bash
xcrun simctl list devices available
```

## Install and Launch

```bash
APP_PATH=~/Library/Developer/Xcode/DerivedData/iosApp-eafqvsbcpnqmbwcktjiqbjafgibt/Build/Products/Debug-iphonesimulator/IndianMetro.app

/usr/libexec/PlistBuddy -c 'Print :CFBundleIdentifier' "$APP_PATH/Info.plist"
xcrun simctl install 049FF56C-D239-4B50-A65B-A7FB83C5888A "$APP_PATH"
xcrun simctl launch 049FF56C-D239-4B50-A65B-A7FB83C5888A com.thefallendeveloper.indianmetro.IndianMetro
```

Expected bundle ID:
- `com.thefallendeveloper.indianmetro.IndianMetro`

## Verify Running Process

```bash
xcrun simctl spawn 049FF56C-D239-4B50-A65B-A7FB83C5888A launchctl list | rg UIKitApplication:com.thefallendeveloper.indianmetro.IndianMetro
```

Expected output contains:
- `UIKitApplication:com.thefallendeveloper.indianmetro.IndianMetro[...]`

## Troubleshooting

- Error: `Missing bundle ID` during install
  - Ensure app path is from `.../DerivedData/.../Build/Products/...`
  - Do not use `Index.noindex` app bundle path.

- Error: `Command PhaseScriptExecution failed with a nonzero exit code`
  - Re-run the `xcodebuild` command from repo root and inspect the `Compile Kotlin Framework` phase.
