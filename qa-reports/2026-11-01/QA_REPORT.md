# HydroNudge real-device QA — 1 November 2026

**Final result: 12 distinct device test methods passed, plus 28 host tests passed.** Three confirmed application issues were fixed. Earlier failing attempts are retained and explained below. The latest fixed debug APK is installed on the Samsung. **Public release is not yet approved.**

## Device and environment

- Samsung Galaxy S25 Ultra, SM-S928B; Android 17 / API 37.
- Display 1080 × 2340; physical density 450 dpi, existing effective density 420 dpi.
- Original/final font scale 1.08; global auto-rotation/user rotation 0 / 0, unchanged.
- Physical memory page size 4096 bytes. This was not a 16 KiB runtime-device test.
- JBR 25.0.4, Gradle 9.6.0, AGP 9.5.0; compile/target API 37, min API 26.
- USB disconnected repeatedly. User-authorized wireless pairing succeeded. A later wireless disconnect required another user-provided pairing code; testing then resumed. Pairing codes are not stored in this report.

## Safety and final phone state

- HydroNudge was initially absent. Every subsequent update used adb install -r.
- No uninstall, app-storage clear, reset, root, purchase, publication or data upload.
- Only known QA entry IDs were edited/deleted. Original entry-ID sets and preferences were preserved; temporary restart/widget/tile records were removed.
- Original preferences restored. Temporary reminder/status settings are off.
- POST_NOTIFICATIONS restored to its original ungranted state; QA user-set/user-fixed flags cleared. See logs/notification-restoration-final.txt.
- Font scale restored to 1.08; density and global rotation unchanged. See logs/device-restoration-final.txt.
- Only newly created QA widgets were removed. Original HydroNudge Quick Settings tile presence was preserved; the temporary tile is absent afterward.
- The app is left open on its dashboard. The instrumentation APK and app-owned QA capture files remain installed/stored; no uninstall was performed.
- Captures are app-window/dialog images or a tightly cropped HydroNudge widget. The Quick Settings panel was opened only to exercise the app's tile; no notification list or unrelated personal app content was inspected or captured.
- App-specific logs and relevant package-filtered diagnostics only. No broad personal-device log collection.

## Builds and tests

| Validation | Result | Evidence |
|---|---|---|
| Debug app and instrumentation APK | PASS | logs/build-device-tests.txt; logs/build-final.txt |
| Unit/Robolectric tests | 28 PASS, 0 failed/skipped | host-test-results/; logs/build-final.txt |
| Final Android test compilation / lint | PASS | logs/final-test-build-lint.txt |
| Android lint | 0 errors, 1 nonfunctional warning | logs/lint-final.txt |
| Optimized unsigned release APK / AAB | PASS | logs/build-final.txt |
| Release permissions audit | PASS | logs/release-permissions-final.txt |
| Release ZIP 16 KiB alignment | PASS | checked with zipalign -c -P 16 4 |
| Native arm64/x86_64 ELF alignment | PASS | logs/native-alignment-final.txt |
| Artifact SHA-256 hashes | Saved | logs/artifact-hashes.json |

The single lint warning is the redundant mipmap-anydpi-v26 qualifier with minSdk 26. It is not a functional defect. Vendor native symbol-stripping and Java 25 test-library warnings were not app compilation failures.

Connected tests were run directly with the installed AndroidJUnitRunner, analytics disabled, to retain both APKs and preserve app data rather than rely on managed-test teardown:

    adb -s PHONE shell am instrument -w -r -e disableAnalytics true -e class TEST_CLASSES app.hydronudge.test/androidx.test.runner.AndroidJUnitRunner

## Passed real-device coverage

| Feature | What actually ran |
|---|---|
| App launch/dashboard | Actual MainActivity rendered; baseline totals checked |
| Quick add 150/250/330/500 mL | Each tap persisted Water with the selected amount; observed total updated correctly |
| Coffee, milk, protein shake, custom | Logged through the real drink editor; saved categories/amounts checked |
| Edit/delete/Undo | Edited QA coffee 180 → 210 mL; deletion and Undo restored the same stable ID and total |
| Daily goal | Changed through Settings to 3,100 mL; preference and progress checked |
| Light/dark mode | Changed through Settings; actual screenshots captured |
| History/statistics | Real stored totals, daily bars, category breakdown; 2,265 mL QA total verified |
| Rotation | Landscape layout and total survived activity rotation; original orientation restored |
| Larger text | 150% font dashboard and logging action checked; original 1.08 restored |
| Background/foreground | Actual Home/launcher transition passed after harness correction |
| Real process restart | Seed committed, app force-stopped externally, relaunched, same record ID/volume/daily total verified; seed then removed |
| Room/DataStore | Native storage CRUD, deduplication, close/reopen and preference persistence tests |
| Notification permission allowed | Real Android prompt allowed; status notification posted |
| Notification permission denied | Real prompt denied; blocked explanation shown, no status post, app usable |
| Notification quick action | Same genuine PendingIntent delivered twice; one 250 mL Room record |
| WorkManager schedule | Unique periodic reminder work actually enqueued at 15 minutes |
| Reminder worker delivery | Genuine ReminderWorker ran via Android WorkManager while app was backgrounded and posted a notification |
| Samsung home-screen widget | Real pin confirmation, rendering, +250 mL action, progress update to 10%, launcher stayed foreground; newly created instance removed |
| Quick Settings tile | Native SystemUI add/click persisted configured volume once; no other tile activated; original tile presence restored |
| Quick-amount validation regression | Malformed five-token input rejected; Save disabled after fix |

The reminder delivery test used a one-time WorkManager request for the actual production ReminderWorker. Records were naturally eligible; no timestamps or user records were modified to force delivery. It proves execution/posting through the real worker, not exact periodic timing or Doze reliability.

Final successful runner logs:

- logs/device-retest.txt — 6 methods passed.
- logs/device-restart-seed.txt and device-restart-verify.txt — 1 method each passed, with a real external process stop/relaunch between them.
- logs/device-system-final.txt — widget plus permission-denial and tile methods, 3 passed.
- logs/device-reminder-delivery.txt — actual background worker method, 1 passed.

## Confirmed bugs, fixes and retests

### QA-001: malformed quick-add tokens silently discarded — medium, fixed

Reproduction: Settings → quick amounts → enter “150, 250, invalid, 330, 500”. Save incorrectly remained enabled. The old mapNotNull parser discarded invalid tokens, leaving four valid amounts.

Fix: domain/QuickAmounts.kt now requires exactly four valid finite positive amounts and rejects the entire input if any token is invalid. Settings shows an error/explanation and disables Save. Added four host regression tests.

Verification: original device assertion failed in logs/device-validation-before.txt; the same device regression passed after installation of the fix in logs/device-retest.txt. Viewed before/after screenshots, including device-final/10-invalid-quick-amounts.png.

### QA-002: singular history wording — low, fixed

Actual screenshot showed “Average on 1 recorded days”. Replaced hardcoded plural text with Android quantity resources and “Average across 1 recorded day”. Goal-day counts use the same quantity resource.

Verification: rebuilt, installed, reran history flow and visually inspected corrected device-final/04-history-statistics.png.

### QA-003: widget default purple button text — low, fixed

The real Samsung widget used a default Material purple on-primary color while the app used ocean/aqua colors.

Fix: widget and app now share the same complete light/dark color schemes.

Verification: rebuilt, installed, reran actual widget pin/quick-action/removal and inspected device-final/11-widget-medium-crop.png. The button text now uses the app's dark ocean color; no default purple remains.

### Test harness failures, resolved without disguising them as app bugs

- The first accessibility run rendered 150% text correctly but timed out monitoring the original ActivityScenario after a component-only background launch. The restoration wait also checked a stale activity configuration. The test now uses a normal MAIN/LAUNCHER intent and verifies the actual system setting. Retest passed; original font value independently confirmed.
- The first widget pin succeeded but the first tap was consumed in Samsung's post-pin selection/resize state. The test now exits that state before tapping. The unchanged callback then passed, and passed again after the palette fix. Only the newly created widget was removed each time.

Historical failing logs remain: logs/device-storage-accessibility.txt, logs/device-validation-before.txt, logs/device-widget.txt. There are no unresolved failures in the final practical test set.

## Screenshots and visual inspection

Actual pixels were viewed for dashboard, both themes, chooser, custom entry, history/statistics, settings, landscape, 150% font, validation errors, notification denial and the widget. Key files:

- screenshots/00-launch.png — initial foreground dashboard including system bars.
- device-final/01-dashboard-light.png and 06-dashboard-dark.png.
- device-final/02-drink-selection.png and 03-custom-logging.png.
- device-final/04-history-statistics.png and 05-settings.png.
- device-final/07-landscape.png and 08-font-150.png.
- device-final/09-after-process-restart.png.
- screenshots/10-invalid-before.png and device-final/10-invalid-quick-amounts.png.
- screenshots/11-widget-before-crop.png — original purple text; device-final/11-widget-medium-crop.png — corrected colors and real 10% progress.
- device-final/12-notifications-denied.png.

Scrollable content partially outside a viewport was not mislabeled as fixed-layout clipping. No fixed-control/system-bar overlap or unreadable normal/150% text was observed in the inspected captures. This is not a full TalkBack or measured-contrast certification.

## Crash and performance review

All seven final app-PID logcat files in device-final/ had zero matches for FATAL EXCEPTION, ANR, SQLite exceptions, out-of-memory or application background-update failure. The initial launch log also had none. A nonfatal vendor libpenguin.so loading message occurred without a corresponding app crash; the app does not directly depend on that library.

Debug cold launch observations: initial 524 ms, final 498 ms (Activity Manager COLD). Final fresh-process PSS 140,226 KiB, RSS 259,632 KiB. These are single debug observations, not release benchmarks.

The startup graphics sample contained only 5 frames, 2 marked janky (40%). That negative observation is retained in logs/final-graphics.txt. The sample is too small and startup/debug-biased to characterize steady-state rendering; release Macrobenchmark/profiling remains recommended. No claim of jank-free rendering is made.

Source review found suspend Room operations and DataStore IO, import/export explicitly dispatched to IO, lifecycle-bound observed state, a foreground-only date ticker, and asynchronous external-surface refresh. Large-history performance is still untested.

## Privacy, background behavior and release configuration

- Main/release APK has no INTERNET, billing/ads, location, accessibility, overlay, exact-alarm or foreground-service permission.
- POST_NOTIFICATIONS/VIBRATE are optional notification features. WorkManager contributes WAKE_LOCK, RECEIVE_BOOT_COMPLETED and ACCESS_NETWORK_STATE; AndroidX adds an app-scoped signature receiver permission.
- Ordinary status notifications, immutable quick actions, unique Room IDs and WorkManager are used; no foreground-service or unlock/accessibility workaround.
- Room schema exported, no destructive migration fallback; DataStore preferences; explicit document-picker backups; Android automatic backup/device transfer excluded.
- No cloud/account/ads/analytics dependency found in the app configuration. UI Automator is instrumentation-only; test analytics were disabled.
- Release APK/AAB compile unsigned; no upload key was generated, no signing secrets committed, nothing published.

## Remaining limits and Google Play assessment

**Suitable for further internal device testing; not yet approved for public release.**

- Older supported Android/API 26 hardware, additional OEMs/tablets, 200% fonts, TalkBack and measured contrast remain unverified.
- Actual periodic delivery under Doze/Samsung battery management, reboot, permanent notification-denial/settings recovery, widget small/large resize and multiple instances remain unverified. Timing is never promised exact.
- Export/import document providers, long-history performance, long-duration battery behavior and a signed optimized release installation were not tested here.
- The Samsung uses 4 KiB pages. Static native/ZIP 16 KiB alignment checks do not replace runtime testing on a 16 KiB device.
- Complete version-specific third-party notices and owner upload-key/signed-release smoke testing are still required.
- Finalize publisher/contact details, host the privacy policy and expose the complete policy/link appropriately in the app and Play listing. Current Settings has a summary, and the repository contains a draft; this is a release gap. Complete Health apps declaration/Data safety/content rating accurately. [Google Play health policy](https://support.google.com/googleplay/android-developer/answer/16679511), [health declaration](https://support.google.com/googleplay/android-developer/answer/14738291).
- Target API 37 exceeds the API 36 minimum currently required for ordinary new phone apps/updates; recheck at submission. [Official target API requirements](https://support.google.com/googleplay/android-developer/answer/11926878).


