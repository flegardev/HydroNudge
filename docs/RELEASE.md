# Release preparation

Release is NOT approved. Do not upload an unsigned or unverified build.

## Build

On this workstation set `$env:JAVA_HOME='C:/Program Files/Android/Android Studio/jbr'`, then run:

```powershell
./gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:assembleRelease :app:bundleRelease
./gradlew.bat :app:connectedDebugAndroidTest
```

Package ID: `app.hydronudge`. Check ownership and Play Console availability before first publication. Version 1 / 1.0.0. Minimum API 26, target API 37 (installed SDK).

Release builds are optimized with R8. No signing credentials are stored in the project. Use Android Studio **Build → Generate Signed App Bundle / APK** to generate an upload key in a secure location outside the repository. Use a long unique password held in a password manager, back up the key securely, and enroll in Play App Signing. Never commit the keystore, passwords, or signing properties. Increment versionCode for updates. Unsigned AAB compilation does not establish release readiness.

## Store copy draft

Short description: **Track every sip. Gentle reminders. Completely free, private and offline.**

Full description:

Stay hydrated. Stay free.

HydroNudge makes keeping a fluid journal easy. Log a quick glass of water or choose milk, coffee, tea, a protein shake, juice, soup and more. Save your favorite servings and review the drinks you actually recorded.

Set a goal that suits you, choose millilitres or US fluid ounces, and enjoy a calm interface in light or dark mode. Your drink journal belongs to you: no accounts, ads, subscriptions, analytics or Internet permission.

HydroNudge records prepared drink volume as a practical approximation of fluid intake. Fluid contribution is separate from nutritional quality. Goals are personal, not medical prescriptions.

Before publication reconcile copy with the final tested features. Do not advertise reminders or widgets until verified.

## Store assets

- Icon: export a 512 × 512 PNG from the adaptive droplet artwork; verify current Play icon requirements.
- Feature graphic: 1024 × 500 PNG/JPEG; ocean blue, turquoise droplet, “Stay hydrated. Stay free.” Keep text inside generous safe areas.
- Real screenshots: empty dashboard, progress, beverage chooser, favorite serving, history, goal/units settings, dark theme; notifications/widget after verification.
- At least two phone screenshots showing actual release UI without personal status-bar data. Include tablet screenshots if supporting a tablet listing.

## Release gates

Samsung QA on 2026-11-01 passed 12 distinct device test methods and 28 host tests, including real widget/tile actions, process persistence, permission allow/deny and WorkManager reminder delivery. Three issues were fixed and retested. See [the full QA report](../qa-reports/2026-11-01/QA_REPORT.md). Signed-release runtime testing, older devices, Doze/periodic timing and the remaining policy/accessibility gates below are still open.

- [ ] Install and launch release on API 26 and current Android
- [ ] Required acceptance criteria in IMPLEMENTATION.md verified
- [ ] Instrumentation: Room integrity, restart persistence, preferences and UI
- [ ] Offline logging, undo, edit/delete, relaunch and rapid taps
- [ ] Notification permission allowed/denied/revoked, channels, DND and force stop
- [ ] Widgets: multiple instances, resize, reboot, midnight and time zone
- [ ] Rotation, process recreation, large fonts (200%), TalkBack, contrast, 48dp touch targets
- [x] Debug/release manifest audited: no Internet, advertising, billing, exact alarm, accessibility, overlay or foreground-service permission
- [x] Debug build, 24 JVM tests, lint (0 errors/1 nonfunctional warning), optimized unsigned release APK and bundle pass
- [x] 64-bit native ELF LOAD alignment and release APK ZIP alignment support 16 KiB pages
- [ ] Manual signed R8 release smoke test; retain `app/build/outputs/mapping/release/mapping.txt` privately
- [ ] Data safety: verify no collection/sharing/accounts; audit transitive SDKs
- [ ] Privacy policy finalized with publisher/contact and public URL
- [ ] Third-party version-specific notices and licenses complete
- [ ] Content rating, target audience, health declarations reviewed in Play Console
- [ ] Current target API and health/policy requirements checked at submission
- [ ] Upload key secured, Play App Signing enabled, explicit owner approval to upload

## Third-party notices

AndroidX, Compose, Room, Lifecycle, Navigation, DataStore, WorkManager and Glance: Android Open Source Project, Apache License 2.0. Kotlin and kotlinx.coroutines: JetBrains and contributors, Apache License 2.0. JUnit: Eclipse Public License 1.0 (test only). Assemble complete version-specific notices before release. HydroNudge’s source license remains the owner’s decision.
