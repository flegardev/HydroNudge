# HydroNudge

**Stay hydrated. Stay free.** Native Android fluid tracking with no ads, accounts, subscriptions, analytics or Internet permission.

## Implemented

- Kotlin, Compose Material 3, single activity, Navigation, MVVM/StateFlow, manual application injection
- Room records and favorite/custom servings, quick water logging, undo, edit/delete, notes and past dates
- Animated progress ring, all 12 beverage categories, mL/US fl oz, editable goal and quick amounts
- DataStore preferences, system/light/dark themes and optional dynamic colors
- WorkManager reminders with intervals ≥15 minutes, waking/quiet hours, personality, pause and goal-aware suppression
- Optional hydration notification with immutable quick-action PendingIntents and Room ID deduplication; user dismissal is remembered
- Responsive small/medium/large Glance widget, shared Room quick actions and periodic refresh
- Configurable Quick Settings tile; locked devices require unlocking before logging
- Calendar-day history, 7/30-day and browsable monthly views, real-data chart and category totals
- System-document-picker JSON export/import with validated transactional merge
- Adaptive droplet icon, unsigned optimized release APK/AAB configuration

These are implementation claims. For executed tests and unverified platform behavior, see [IMPLEMENTATION.md](IMPLEMENTATION.md). This project is **not yet approved for public release**.

## Build locally

Open in Android Studio with installed SDK API 37 and Java compatible with Gradle 9.6. On this workstation:

```powershell
$env:JAVA_HOME='C:/Program Files/Android/Android Studio/jbr'
./gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
./gradlew.bat :app:assembleRelease :app:bundleRelease
./gradlew.bat :app:connectedDebugAndroidTest
```

Debug APK: `app/build/outputs/apk/debug/app-debug.apk`. Release APK is unsigned; signing credentials are intentionally absent. JVM tests include Robolectric for real Room/SQLite, DataStore and Compose execution. A host test does not replace emulator/physical-device testing.

## Project map

- `data/local`: entities, DAO, database and schema
- `data`: validated repository and portable backup codec
- `data/preferences`: DataStore settings
- `domain`: categories, conversions and calendar boundaries
- `ui`: ViewModel, navigation shell, dashboard, drink/favorite editors, settings, history, components/theme
- `reminders`, `notifications`, `widgets`, `quicksettings`: Android integrations
- `app/schemas`: Room schema history; never add a destructive migration fallback

## Platform behavior

No reliable background phone-unlock hook is promised on modern Android. Use the widget, tile, scheduled reminders or optional status notification. No accessibility, overlay, exact-alarm or foreground-service permission is requested. Scheduled work is inexact, may be delayed by battery management, and does not bypass Force stop. Widgets/status can show the prior day until Android permits a background refresh. Opening the app immediately recomputes calendar-based totals.

See [background limitations](docs/ANDROID_LIMITATIONS.md), [backup format](docs/BACKUP_FORMAT.md), [privacy draft](docs/PRIVACY.md), and [release/signing/store checklist](docs/RELEASE.md).

## Real-device QA

The Samsung-device results, screenshots, three confirmed fixes and remaining release gates are in [QA_REPORT.md](qa-reports/2026-11-01/QA_REPORT.md). Twelve distinct device test methods and 28 host tests passed. Device tests preserve existing records, remove only their own identified QA rows/widgets/tile, and restore preferences/system settings. Instrumentation runs directly through ADB with analytics disabled and without uninstall/clear-data cleanup.
