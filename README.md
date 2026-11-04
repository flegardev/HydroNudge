# HydroNudge

Offline-first Android water tracker. Log water, review history, and optionally set a goal or reminders. Records stay on the device unless the user exports a backup. No ads, accounts, analytics, subscriptions, or Internet permission. HydroNudge does not provide medical advice.

## Build and check

Use the checked-in Gradle Wrapper and Android Studio JDK. This project uses `--no-configuration-cache` for Gradle commands.

```powershell
./gradlew.bat --no-configuration-cache :app:assembleDebug
./gradlew.bat --no-configuration-cache :app:testDebugUnitTest
./gradlew.bat --no-configuration-cache :app:lintDebug
./gradlew.bat --no-configuration-cache :app:bundleRelease
```

The release bundle is unsigned until the owner signs it with the verified Play upload key. Never upload a debug or locally test-signed build.

## Project map

- `app/src/main`: Compose app, Room storage, preferences, reminders, notifications, widgets, and Quick Settings tile
- `app/schemas`: exported Room schemas; preserve migration history
- `gradle`: version catalog and Wrapper configuration
- `scripts`: focused Windows build, QA, and release-validation helpers
- `release-assets`: current listing copy, store graphics, genuine screenshots, and asset manifests
- `play-submission`: current policy, declaration review, Console evidence, and closed-test materials
- `docs`: backup format, Android behavior limits, signing instructions, and third-party notices

## Release state

HydroNudge exists in Play Console as `app.hydronudge`; Play App Signing and the Alpha closed-test track are already configured. The owner-signed bundle and closed-test release are still pending. See [current status](PLAY_SUBMISSION_STATUS.md) and the [closed-test checklist](PLAY_CLOSED_TEST_SETUP.md).
