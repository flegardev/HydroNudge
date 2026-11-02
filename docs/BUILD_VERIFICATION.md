# Build verification — 2026-10-31

## Final result

The following command completed successfully in **2m52s**, with **24 passing JVM tests**, after the final ring/layout and theme corrections:

```powershell
./gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:assembleRelease :app:bundleRelease :app:assembleDebugAndroidTest --console=plain
```

Evidence: repository-root `build-validation.log`; JUnit XML in `app/build/test-results/testDebugUnitTest`; HTML report `app/build/reports/tests/testDebugUnitTest/index.html`. Lint report: `app/build/reports/lint-results-debug.html` (**0 errors, 1 harmless redundant-resource-qualifier warning**).

A final cached verification of the same task set explicitly captured **Gradle exit code 0** (`build-exit-verification.log`, 2s). This distinguishes Gradle’s successful result from PowerShell treating Java 25 vendor-library stderr warnings as native-command errors. Tests actually executed in the preceding 2m52s run; they were up to date in the final exit-code check.

## Artifacts

All are package `app.hydronudge`, version code 1, version name 1.0.0, min API 26, target/compile API 37. Labels were checked from the packaged release with aapt.

| Artifact | Path | SHA-256 |
|---|---|---|
| Installable debug APK | `app/build/outputs/apk/debug/app-debug.apk` | `58DDAB3C382A646BEBB0AFA4F3266DC26DA3069E9AC57633EBA02532A9F1F640` |
| Unsigned optimized release APK | `app/build/outputs/apk/release/app-release-unsigned.apk` | `562C87B5E7ED93E347A4DDA5339AD1FD6262BE0EDD609A03A9A80E22DC1D4027` |
| Unsigned release bundle | `app/build/outputs/bundle/release/app-release.aab` | `F39F8C282250DAF7A75DDFA6E28AB259BE75C60020CACB0DD003A71AB0BAA5C5` |

Release credentials were not created or configured. The bundle is a build artifact, not an approved upload. APK approximately 2.4 MB; AAB approximately 5.4 MB. The AAB contains all configured ABIs and bundle metadata, so its download size is not the delivered per-device APK size.

## Executed test coverage

- Real Room/SQLite add, edit, delete, totals, unique action IDs, close/reopen persistence, all 12 beverage categories and full local data deletion
- DataStore preference updates across repository instances and remembered notification dismissal
- Daily progress and values over the goal, large totals, mL/US fluid ounces
- Local midnight, current time-zone regrouping, 23/25-hour DST days
- Real-record history totals/averages, missing-day interpretation, category breakdown and goal-day counts
- Backup JSON round trip, invalid versions/IDs/fractional volumes, atomic validation and repeated import
- Reminder suppression for quiet hours, overnight schedules, pause, goals and recent drinks
- WorkManager unique schedule, interval replacement and cancellation
- Host Compose: actual MainActivity startup, quick-add water, choose/log milk, observed totals, navigation, dark mode

Host UI screenshots `app/build/screenshots/dashboard-light-empty.png` and `dashboard-dark-logged.png` were rendered and visually inspected. These are test captures, not a substitute for store-ready device screenshots. A test-only Robolectric width-qualifier bug initially caused a dialog idle timeout; the same complete flow passed after removing that override.

## Packaged privacy and native-library audit

Release manifest requests POST_NOTIFICATIONS and VIBRATE for optional notifications. WorkManager contributes WAKE_LOCK, RECEIVE_BOOT_COMPLETED and ACCESS_NETWORK_STATE; AndroidX uses an app-scoped signature permission for private runtime receivers. There is no Internet permission, foreground service permission, advertising/billing, location, accessibility, overlay or exact-alarm access.

`zipalign -c -P 16 4` passed for the release APK. The 64-bit arm64-v8a and x86_64 copies of `libandroidx.graphics.path.so` and `libdatastore_shared_counter.so` have 16,384-byte LOAD segment alignment. Runtime execution on a physical 16 KiB-page device remains unverified.

## Blocked and still unverified

`./gradlew.bat :app:connectedDebugAndroidTest` failed before test execution with **DeviceException: No connected devices!** (`device-tests.log`). The available API 37 AVD cannot boot because the emulator reports **Android Emulator hypervisor driver is not installed on this machine** (`emulator.log`). Enable supported virtualization/Windows Hypervisor Platform via Android Studio diagnostics, restart Windows if needed, or connect a physical Android phone with USB debugging. Then rerun the command.

Physical API 26/API 37 behavior, background notifications and permissions, widgets/tile/lock screen, battery saver, reboot, force stop, process recreation, rotation, document providers, large fonts and TalkBack remain release gates. Finalize publisher/privacy contact, third-party notice audit, upload key and signed release smoke testing before publication. Nothing was uploaded or published.
