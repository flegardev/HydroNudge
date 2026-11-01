# HydroNudge implementation and verification

This checklist reports implementation separately from device verification. No release-readiness claim until the acceptance checks are complete.

- [x] M1: Room, repository, lifecycle ViewModel, dashboard, quick add, Undo; host and real Samsung persistence/restart tests passed.
- [x] M2: 12 categories, custom names, favorites, notes, past timestamps, edit/delete implemented; real Samsung water/coffee/milk/protein/custom and edit/delete/Undo passed.
- [x] M3: DataStore, goal, units, strict quick amounts, theme; host/native persistence and Samsung goal/theme tests passed.
- [x] M4: Reminder policy and scheduling tests pass; genuine ReminderWorker posted through Android WorkManager with app backgrounded. Periodic/Doze timing remains unverified.
- [x] M5: Optional ordinary status notification; real permission allow/deny, posting and immutable quick-action replay deduplication passed. Physical dismissal/permanent-denial edge paths remain open.
- [x] M6: Real Samsung medium widget pin/render/quick action/update/removal and native QS tile passed. Small/large resizing and multiple instances remain open.
- [x] M7: Calendar-based totals/averages/category breakdown/daily bars tested; real Samsung history/statistics inspected. Monthly navigation implemented, wider UX checks remain open.
- [x] M8: Light/dark and 150% font inspected; rotation, background/foreground and real process restart passed. TalkBack/200% fonts/tablets remain open.
- [x] M9: Debug/test APK, optimized unsigned release APK/AAB, 28 host tests, 12 distinct device methods passed; lint 0 errors/1 nonfunctional warning; privacy/store/signing documents prepared.
- [ ] RELEASE ACCEPTANCE: all physical-device, background integration, accessibility, legal/contact and signing gates below verified. The app is not yet ready to publish.

## Environment

Windows 11; Android Studio JBR 25.0.4; Gradle wrapper 9.6.0; AGP 9.5.0; installed platform API 37; build tools 36.0.0. Minimum API 26; target API 37. No attached device initially; AVD `Medium_Phone_API_37.0` is available.

Dependency versions checked against Google's Maven metadata and Maven Central on 2026-10-31. Room 2.8.5, DataStore 1.3.0, Lifecycle 2.11.0, KSP 2.3.13, WorkManager 2.12.0, Glance 1.2.0. Starter versions updated to Core 1.19.1, Activity 1.13.0, Navigation 2.10.2, Compose BOM 2026.09.00 and Compose compiler 2.4.21. Final debug/release/test builds verified compatibility.

## Verification log

- Foundation: `:app:assembleDebug :app:testDebugUnitTest` succeeded (4m14s). Test report in `app/build/reports/tests/testDebugUnitTest`.
- Emulator launch failed: “x86_64 emulation currently requires hardware acceleration! Android Emulator hypervisor driver is not installed on this machine”. No connected devices. Enable virtualization and Windows Hypervisor Platform per Android Studio Device Manager diagnostics, reboot if required, or connect a physical device with USB debugging. Run `./gradlew.bat :app:connectedDebugAndroidTest` afterward.
- Room close/reopen, deduplication and preferences also have Robolectric tests so local storage can be checked without a hardware emulator. These do not replace real-device acceptance checks.
- Reminders pass: debug and instrumentation APK compilation plus 15 JVM tests passed (3m14s).
- First full integration build: debug, instrumentation APK, 20 JVM tests, lintDebug, optimized unsigned release APK and unsigned AAB succeeded (4m54s). Lint: 0 errors, 34 warnings, primarily starter versions/resources and catalog placement. Correctness warnings (backup rules) addressed and versions/catalogs updated; repeat checks running. A lint SARIF quick-fix calculation exception occurred while source was changing, without failing lint; final lint must run against frozen source.
- Backup export/import validates complete files before transactional merge; five Robolectric tests passed (round trip, unsupported schema, duplicate IDs, fractional volume, atomic validation/idempotence).
- The first host Compose UI attempt timed out in a dialog because of [Robolectric #8460](https://github.com/robolectric/robolectric/issues/8460). Migrating to Compose test v2 alone did not fix it. Removing the explicit test width qualifier fixed it while retaining all UI interactions.
- Final frozen-source verification: `:app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:assembleRelease :app:bundleRelease :app:assembleDebugAndroidTest` **BUILD SUCCESSFUL** (2m52s), log `build-validation.log`.
- **24 tests passed, 0 failed, 0 skipped**. Includes actual MainActivity launch, water/milk logging through Compose, automatic total updates, navigation, dark mode and captured screenshots; Room close/reopen/CRUD/all categories/idempotence; DataStore persistence and status dismissal; calendar/DST/zone/unit/overflow calculations; history; backup validation/merge; unique WorkManager interval update/cancellation.
- Visually inspected generated light/dark screenshots in `app/build/screenshots`. Fixed default lavender container colors, dark on-primary/snackbar colors, and the narrow-width ring/text overlap; inspected the corrected dark screenshot.
- Final lint: **0 errors, 1 warning** (`ObsoleteSdkInt` for the redundant `mipmap-anydpi-v26` qualifier, harmless with minSdk 26). Vendor prebuilt native symbol-stripping and Java 25 test-library native/Unsafe warnings do not indicate Android app compiler failures.
- Release manifest audited with aapt: label HydroNudge, package app.hydronudge, min SDK 26, target 37, version 1 / 1.0.0. No INTERNET, billing, ads, location, accessibility, overlay, exact-alarm or foreground-service permission.
- Release ZIP alignment check `zipalign -c -P 16 4` passed. Both shipped native libraries have 16,384-byte ELF LOAD alignment for arm64-v8a and x86_64.
- `:app:connectedDebugAndroidTest` was attempted and **blocked**: `DeviceException: No connected devices!` (`device-tests.log`, 12s). Instrumentation APK compilation passed; instrumentation tests did not execute.
- See `docs/BUILD_VERIFICATION.md` for final artifact hashes and verification boundaries.

## Still required before release

- Real API 26/other OEMs; signed optimized release; offline-mode hardware testing
- Battery saver/Doze, reboot, permanent-denial/settings recovery, physical notification dismissal
- Widget small/large resize and multiple instances; tile locked-screen path; real midnight/zone changes
- Rotation with an unsaved editor; import/export providers; large datasets
- TalkBack, 200% fonts, measured contrast and tablet layouts
- Complete version-specific third-party license audit; final publisher/privacy contact and public policy URL
- Secure owner-generated upload key, signed release smoke test and explicit permission to upload

## Real Samsung QA — 2026-11-01

See [the current device QA report](qa-reports/2026-11-01/QA_REPORT.md). Samsung SM-S928B (Galaxy S24 Ultra), Android 17/API 37, 1080×2340, tested through user-authorized wireless ADB after unstable USB.

- Actual debug installation and cold launch passed (524 ms reported by Activity Manager).
- Real functional flow passed quick quantities, coffee/milk/protein/custom, observed totals, edit/delete/undo, goal/theme/history, rotation, actual notification permission/posting and native PendingIntent replay deduplication. Separate Room/DataStore device tests and chooser smoke passed.
- Final outcome: 12 distinct device methods passed, 28 host tests passed. Historical validation/harness failures are retained and resolved in the QA report.
- Fixed strict quick-amount parsing, singular history wording and default purple widget colors. Rebuilt, installed, retested and visually inspected.
- Wireless reconnect succeeded after another user-authorized pairing; all remaining practical device tests completed.
- No storage clear/uninstall. Known QA rows/widgets/tile cleaned up; original preferences/font/rotation/density and notification permission restored, including QA permission flags.
- Real process stop/relaunch persistence, launcher widget action, native QS tile and actual background WorkManager notification delivery passed. Periodic/Doze timing remains unverified.
- Debug startup observations: 524/498 ms. Small startup graphics sample had 2 janky frames of 5; no steady-state performance claim. Signed-release profiling remains open.
