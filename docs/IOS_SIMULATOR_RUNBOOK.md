# iOS Simulator Build and Run

This runbook documents the exact steps to build and run the iOS app from terminal.

## Prerequisites

- macOS with Xcode installed.
- Xcode command line tools installed (`xcodebuild`, `xcrun` available).
- Simulator runtime available (for example iOS 18.2).

## 1. Build the iOS app

From project root:

```bash
xcodebuild \
  -project iosApp/iosApp.xcodeproj \
  -scheme iosApp \
  -configuration Debug \
  -destination 'platform=iOS Simulator,name=iPhone 16,OS=18.2' \
  build
```

Expected result: `BUILD SUCCESSFUL` for Gradle framework task and `** BUILD SUCCEEDED **` from Xcode.

## 2. Boot simulator

```bash
xcrun simctl boot 049FF56C-D239-4B50-A65B-A7FB83C5888A || true
open -a Simulator
```

You can discover simulator IDs with:

```bash
xcrun simctl list devices available
```

## 3. Install app bundle

```bash
APP_PATH=~/Library/Developer/Xcode/DerivedData/iosApp-eafqvsbcpnqmbwcktjiqbjafgibt/Build/Products/Debug-iphonesimulator/IndianMetro.app
xcrun simctl install 049FF56C-D239-4B50-A65B-A7FB83C5888A "$APP_PATH"
```

Validate bundle identifier:

```bash
/usr/libexec/PlistBuddy -c 'Print :CFBundleIdentifier' "$APP_PATH/Info.plist"
```

Expected: `com.thefallendeveloper.indianmetro.IndianMetro`.

## 4. Launch app

```bash
xcrun simctl launch 049FF56C-D239-4B50-A65B-A7FB83C5888A com.thefallendeveloper.indianmetro.IndianMetro
```

If launch output is empty or returns generic open errors, verify process state:

```bash
xcrun simctl spawn 049FF56C-D239-4B50-A65B-A7FB83C5888A launchctl list | rg IndianMetro
```

Expected: an entry like `UIKitApplication:com.thefallendeveloper.indianmetro.IndianMetro[...]`.

## 5. Optional cleanup

```bash
xcrun simctl terminate 049FF56C-D239-4B50-A65B-A7FB83C5888A com.thefallendeveloper.indianmetro.IndianMetro || true
xcrun simctl shutdown 049FF56C-D239-4B50-A65B-A7FB83C5888A
```

## Troubleshooting

- `Missing bundle ID` during install:
  - Do not use the `Index.noindex` app path.
  - Use `.../DerivedData/.../Build/Products/Debug-iphonesimulator/IndianMetro.app`.
- `Command PhaseScriptExecution failed`:
  - Re-run the `xcodebuild` command above from project root and inspect `:composeApp:embedAndSignAppleFrameworkForXcode` output.
