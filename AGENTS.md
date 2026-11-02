# HydroNudge development instructions

## Project goals and safety
- HydroNudge is an offline-first Android hydration app. Preserve its existing architecture, behavior, user records, preferences, Room schema history, and release constraints.
- Do not recreate the app or delete/reset project files, phone data, app data, widgets, permissions, or system settings.
- Make small, reviewable changes that solve a confirmed need. Avoid unrelated refactors and new dependencies.
- Never add ads, subscriptions, analytics, cloud requirements, secrets, or unnecessary Android permissions.
- Never claim a build, test, device run, screenshot, or manual check that was not actually completed.
- No destructive operation, data migration, app uninstall, clear-data action, signing credential change, or device reset without explicit user approval.

## Kotlin and Compose
- Follow Kotlin official style and the project's existing package/layout conventions.
- Prefer immutable UI state, lifecycle-aware collection, accessible semantics, and small Compose functions.
- Keep business rules in the existing domain/data layers; keep UI state and effects explicit.
- Preserve Room migrations and schema exports. Never add a destructive migration fallback.
- Keep permissions and Android integrations narrowly scoped to existing product behavior.

## Verification
- Use the checked-in Gradle Wrapper (`gradlew.bat` on Windows); do not depend on a global Gradle installation.
- For this project, use `--no-configuration-cache` with Gradle commands until the observed AAPT2 configuration-cache serialization issue is resolved.
- After relevant code changes, run focused tests and a debug build; run lint when changes affect Android/Kotlin code. Report exact commands and outcomes.
- Run instrumentation tests only on an explicitly selected target and inspect the test's data/settings cleanup behavior first.
- On this workstation, `connectedDebugAndroidTest` removed `app.hydronudge` from the Samsung during a failed run. App backup is disabled. Do not run connected tests on the Samsung again unless the user explicitly approves that risk or testing is moved to an emulator.
- Preserve unrelated uncommitted user work. Check for existing Git metadata before setup and never initialize over an existing repository. This project arrived without `.git`; create a local repository for requested checkpoints if appropriate, but do not commit or push without explicit permission.

## Handy commands
- `powershell -ExecutionPolicy Bypass -File scripts/build-debug.ps1`
- `powershell -ExecutionPolicy Bypass -File scripts/test-unit.ps1`
- `powershell -ExecutionPolicy Bypass -File scripts/lint.ps1`
- `powershell -ExecutionPolicy Bypass -File scripts/device.ps1 -Action list`
- `powershell -ExecutionPolicy Bypass -File scripts/device.ps1 -Action connect -Endpoint <ip:port>`
- `powershell -ExecutionPolicy Bypass -File scripts/device.ps1 -Action install -Serial <adb-serial>`
- `powershell -ExecutionPolicy Bypass -File scripts/device.ps1 -Action launch -Serial <adb-serial>`
- `powershell -ExecutionPolicy Bypass -File scripts/device.ps1 -Action screenshot -Serial <adb-serial>`
- `powershell -ExecutionPolicy Bypass -File scripts/device.ps1 -Action crashlog -Serial <adb-serial>`
- `powershell -ExecutionPolicy Bypass -File scripts/instrumentation.ps1 -DeviceSerial <adb-serial> -AllowPossibleTargetAppRemoval` (only after protecting phone data; prefer an emulator)
- `powershell -ExecutionPolicy Bypass -File scripts/qa-summary.ps1`
