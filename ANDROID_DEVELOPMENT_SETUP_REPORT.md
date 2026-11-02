# HydroNudge Android development workstation report

**Audit date:** 1 November 2026 (Europe/Ljubljana)  
**Project:** `C:\Users\tinif\Downloads\HydroNudge-main\HydroNudge-main`  
**Scope:** Workstation and project setup. No HydroNudge Kotlin business logic or manifest permissions were changed.

## Status overview

| Area | Result | Evidence / limitation |
|---|---|---|
| Android Studio opens the existing project | **Passed** | Existing folder opened; Gradle project model was imported and synchronized. No new project was created. |
| Debug build | **Passed** | `:app:assembleDebug`; APK at `app/build/outputs/apk/debug/app-debug.apk`. |
| JVM unit tests | **Passed** | 28 tests, 0 failures, 0 errors, 0 skipped. Robolectric API 35 runtime was downloaded after approval. |
| Android lint | **Passed** | `:app:lintDebug`; HTML report at `app/build/reports/lint-results-debug.html`. |
| Samsung wireless ADB | **Passed** | Exactly one authorized SM-S928B connection was selected at final check. Temporary pairing code is not stored. |
| Install and app launch | **Passed, with a data incident** | APK installed with `adb install -r`, then `MainActivity` returned `Status: ok`. The instrumentation task had removed the prior package first; this install therefore followed a fresh install and cannot prove prior app data survived. |
| Samsung instrumentation | **Failed** | Both selected tests failed with “No compose hierarchies found in the app.” See `app/build/reports/androidTests/connected/debug/index.html`. The Gradle connected-test task removed the app package during this run. No further phone instrumentation was attempted. |
| App data preservation | **Unverified; possible loss** | Android package removal was recorded, and the app disables Android backup. The post-install screen showed the default 2,500 mL goal and 0 mL today, matching the earlier saved QA screenshot, but that does not establish that all old records/preferences survived. |
| Codex CLI | **Passed** | Local Codex CLI reports an existing authenticated ChatGPT session and can access this local project. |
| JetBrains AI Assistant Codex authorization | **Requires user action** | Compatible AI Assistant plugin is installed; ChatGPT OAuth is supported, but no IDE Codex authorization was completed. No API key or paid service was configured. |
| Git | **Passed with setup pending** | Git for Windows 2.56.0.windows.4 installed per-user; new empty `main` repository created because no `.git` existed. No files were staged or committed. |
| Compose Preview rendering | **Not tested** | Compose sources compile and Android Studio tooling is available; no Preview canvas was opened for visual verification. |

## 1. Operating system and hardware

- Windows 11 Home, build 26300, 64-bit.
- AMD Ryzen 5 3600, 6 cores / 12 logical processors.
- 16 GiB installed RAM (about 2 GiB free at audit time); C: had about 485 GiB free.
- NVIDIA GeForce RTX 3060 Ti, 4 GiB reported video memory.
- Windows Hypervisor Platform acceleration (WHPX) reports usable. Android Emulator and an API 37 Google Play x86_64 system image are installed. No AVD has been created, so no emulator test was run.

## 2. Android Studio

- Installed at `C:\Program Files\Android\Android Studio`.
- Version shown by the IDE: Android Studio 2026.2.2 / Rabbit 2; product build `AI-262.10968.63.2622.16730706`.
- The existing HydroNudge folder was opened and imported. Android Studio logs show the project model update for the Gradle sync.
- The interface is already dark (`OCEANIC` theme). Existing UI font is Segoe UI 12; editor-specific font and line-number state were not changed or independently verified.
- The project SDK and IDE provide Kotlin analysis, Android/Compose support, Compose Preview, Logcat, Device Manager, Layout Inspector, Profiler and Gradle tools. Kotlin compilation, project sync and device deployment were verified; the Preview canvas itself was not.
- The project uses 2 GiB Gradle heap and at most 2 workers. These settings were left unchanged because they are reasonable for this machine. Gradle caching and Kotlin incremental compilation remain at project defaults.

## 3. Android SDK and JDK

- SDK: `C:\Users\tinif\AppData\Local\Android\Sdk`.
- Platform: Android API 37.0, revision 2.
- Build Tools: 36.0.0.
- Platform-Tools / ADB: 37.0.1; ADB reports version 1.0.41.
- Emulator package and API 37 Google Play x86_64 image are present.
- Android SDK Command-line Tools (`cmdline-tools` / `sdkmanager`) are not installed. They were not needed for the verified Gradle builds or ADB workflow, so no extra large SDK download was made.
- JDK: Android Studio bundled JetBrains Runtime 25.0.4 at `C:\Program Files\Android\Android Studio\jbr`.
- User environment now has `JAVA_HOME` pointing to that JBR, `ANDROID_HOME` pointing to the SDK, and separate PATH entries for JBR `bin`, SDK `platform-tools` and Git `cmd`. A malformed combined PATH entry was removed. A simulated fresh terminal successfully resolved `java.exe`, `adb.exe` and `git.exe`.
- The project's Gradle JDK is Android Studio `jbr-25`; build scripts select the same JBR. No duplicate Java installation or global Gradle was added.

## 4. Gradle, Android Gradle Plugin and Kotlin

- Gradle Wrapper: 9.6.0, with the checked-in distribution checksum.
- Android Gradle Plugin: 9.5.0.
- Kotlin: 2.4.21.
- Java source/target compatibility: 17.
- Compile SDK / target SDK: API 37; min SDK: API 26.
- These project versions are mutually compatible in the installed toolchain, and the project compiled successfully.
- **Known Gradle workaround:** the first offline invocation was missing AAPT2 9.5.0. A normal build downloaded the required artifact and succeeded. This project also hit an AAPT2 configuration-cache serialization failure; the checked-in `org.gradle.configuration-cache=true` setting was not changed, but the repeatable workstation scripts pass `--no-configuration-cache`.
- Build output warned that configured NDK 28.2.13676358 was unavailable and native-library symbol stripping could not be performed. Packaging and debug build still succeeded; no NDK-dependent project source was identified or changed.

## 5. IDE plugins

No plugin was installed, removed or toggled during this setup. Android, Kotlin, Gradle, Compose, Git/VCS, Logcat, Device Manager and related development support are part of the existing Android Studio installation and were sufficient for sync/build/deployment.

Priority user-installed plugin folders already present in this Android Studio profile:

| Plugin | Version | Status / purpose |
|---|---:|---|
| JetBrains AI Assistant | 262.10968.238 | Present; official Codex agent can be authorized with ChatGPT OAuth. Authorization still needs the user. |
| SonarQube for IDE | 13.1.0.85964 | Present; local code-quality checks. No duplicate inspection plugin added. |
| Codex Launcher | 1.1.17 | Present from before setup; third-party launcher was not used or newly installed. |
| GitHub Copilot | 1.20.0-261 | Present from before setup. |
| Windsurf / Codeium | 2.12.28 | Present from before setup. |
| Amazon Q | 4.10.262 | Present from before setup. |
| Qoder CN | 2026.1030.84844251 | Present from before setup. |

The profile contains 61 user plugin directories, including other language and UI plugins. There is no `disabled_plugins.txt`; this is filesystem evidence of installation, not proof that every optional plugin loaded successfully. They were left unchanged to preserve the user's existing IDE setup. No new plugin was skipped for incompatibility; no full compatibility sweep of all 61 plugins was performed.

## 6. AI and Codex setup

- The OpenAI Codex CLI bundled with the installed VS Code Codex extension is version `0.164.0-alpha.11.1`. `codex login status` confirmed an existing authenticated ChatGPT session. The active local Codex environment can read and modify this project, run PowerShell/Gradle, access ADB and follow the new `AGENTS.md`.
- JetBrains AI Assistant 2026.2 documentation supports Codex authorization with an existing ChatGPT account. This route does not require entering an API key or buying a JetBrains AI subscription; ChatGPT plan usage limits still apply. The IDE plugin has not been authorized yet.
- To activate it: in Android Studio open **AI Chat**, choose **Codex**, send a short prompt, choose **Install and Continue**, select **ChatGPT** or **ChatGPT (device code)**, then finish the OAuth approval in the browser. Confirm the active provider under **Settings > Tools > AI Assistant > Providers & API Keys > Agent Authorization** so requests use the ChatGPT account. Do not select JetBrains AI or an API-key provider unless you intend to use that billing route.
- No credentials were written to the repository. Current local Codex workflow works without waiting for IDE authorization.

## 7. Git installation and project state

- Git was absent initially. After approval, official Git for Windows 2.56.0.windows.4 x64 was downloaded from the official Git for Windows release and installed per-user at `C:\Users\tinif\AppData\Local\Programs\Git`. SHA-256 and Authenticode signature were verified before installation. Git reports `2.56.0.windows.4`.
- Global defaults: `init.defaultBranch=main`, `core.autocrlf=true`, `core.safecrlf=warn`. Git identity (`user.name` / `user.email`) remains unset rather than being guessed.
- The project contained no `.git` directory. A new local `main` repository was initialized after checking; **nothing has been staged, committed or pushed**. All project files currently appear as untracked files until the owner chooses a first commit.
- `.gitignore` now ignores Gradle/build output, local IDE settings, `local.properties`, signing keystores/certificates, credential property files and `.env` secrets. `git check-ignore` confirmed representative build, local-properties, signing and `.idea` paths are excluded.
- Android Studio's existing process predates the Git PATH update. Restart Android Studio once (after saving any open editor work) for its integrated terminal/VCS to inherit the updated user PATH.

## 8. Wireless Samsung ADB

- Selected connection: Samsung Galaxy S24 Ultra, model `SM-S928B`, Android 17.
- Final `adb devices -l` showed exactly one authorized usable device connection. The second duplicate mDNS transport was disconnected safely. No unrelated phone apps, photos, messages or accounts were accessed.
- The existing wireless pairing was reused; the temporary code was not saved or included here. For reconnecting, open **Device Manager > Pair Devices Using Wi-Fi**, use the phone's current connection endpoint with `scripts/device.ps1 -Action connect -Endpoint <ip:port>`, then confirm with `-Action list`. A new code is required only if Android revokes the computer pairing.

## 9. HydroNudge debug build and installation

- Existing app ID: `app.hydronudge`, version `1.0.0` / version code 1.
- Debug APK build succeeded, including when rerun through `scripts/build-debug.ps1`.
- After the instrumentation task removed the package, the same debug APK was installed and launched to restore the app package. `MainActivity` returned `Status: ok`; no HydroNudge crash lines were found after launch. The debug APK was then installed again with `adb install -r` while the fresh installation was present; this update succeeded without clearing that new installation's data.
- Screenshot: `qa-reports/local-device/HydroNudge-20261101-214206.png` (captured with HydroNudge confirmed in the foreground). PID-filtered app logs are in `qa-reports/local-device/HydroNudge-app-logcat.txt`; the crash scan result is `qa-reports/local-device/HydroNudge-crashlog-20261101-214824.txt`.

## 10. JVM unit tests

- Command: `.\gradlew.bat --no-configuration-cache --console plain testDebugUnitTest --rerun-tasks`.
- Result: **28 passed, 0 failed, 0 skipped** across the existing nine test classes. Robolectric's API 35 runtime was downloaded after the user approved the first-time download.
- Repeat command: `powershell -ExecutionPolicy Bypass -File scripts/test-unit.ps1`.

## 11. Instrumentation tests and data incident

- Selected tests: `DashboardTest.dashboardAndDrinkChooserAreAccessible` and `QuickAmountValidationTest.malformedFiveTokenInputMustNotBeSilentlyAccepted`.
- Result: **2 failed, 0 errors**. Both reported `IllegalStateException: No compose hierarchies found in the app`; they did not pass.
- During this `connectedDebugAndroidTest` task, Android's package manager log recorded `app.hydronudge` removal. A later package check showed no installed APK and the app's `firstInstallTime` reset after reinstall. The fresh install now runs, but the prior private app database/preferences cannot be proven intact.
- The manifest sets `android:allowBackup="false"` and excludes app databases/preferences from backup. No Android cloud restore is available through the app's configured backup rules. The new screen showed 0 mL today and a 2,500 mL goal, matching the earlier QA screenshot; this is not enough to certify that every prior user record survived. If the owner had manually exported HydroNudge JSON, they can restore it using the app's import flow; no personal folders were searched.
- Because of this behavior, `scripts/instrumentation.ps1` now stops by default and requires an explicit risk switch. Do not run connected instrumentation on the Samsung again. Prefer a disposable emulator. No AVD exists yet, although WHPX and the API 37 Google Play image are ready.

## 12. Android lint

- Command: `:app:lintDebug`.
- Result: **Passed**; report generated at `app/build/reports/lint-results-debug.html` (SARIF also available beside it).
- Repeat command: `powershell -ExecutionPolicy Bypass -File scripts/lint.ps1`.

## 13. Repeatable scripts and validation

Scripts under `scripts/`:

- `build-debug.ps1` — assemble debug using the Wrapper.
- `test-unit.ps1` — run JVM unit tests.
- `lint.ps1` — run Android lint.
- `device.ps1` — list/connect ADB, install with `-r`, launch, foreground screenshot, or capture app-specific crash lines.
- `instrumentation.ps1` — selected instrumentation classes; blocked unless the possible target-app removal is explicitly acknowledged.
- `qa-summary.ps1` — summarize actual Gradle XML results and current ADB connection without recording the phone's IP/serial.
- `_common.ps1` — project-root, JBR, SDK and Wrapper setup.
- `README.md` — beginner-oriented usage and the Samsung instrumentation safety warning.

PowerShell syntax was parsed for every script. Build, unit-test, lint, ADB-list, install, launch, screenshot, crash-log and QA-summary scripts were exercised successfully. The instrumentation script's safety gate was verified to stop by default; its task itself is not reported as passing. Wi-Fi reconnect was not repeated because the current connection remained valid.

## 14. Changes performed

- Created this report and the project `AGENTS.md` instructions.
- Added safe, repeatable PowerShell scripts and usage notes.
- Hardened `.gitignore` for build outputs, local IDE settings, secrets and signing files.
- Installed per-user Git, set Windows-friendly global defaults, corrected the development PATH, and initialized an empty Git repository without committing.
- Created a pre-setup source/config checkpoint ZIP at `C:\Users\tinif\Documents\HydroNudge-setup-backups\HydroNudge-before-workstation-setup-2026-11-01.zip` (SHA-256 `483AFD53DA96EB360BE1E5AC2CC98CC5E98B0190825E71D6DEC94385E8DAD111`).
- No Kotlin business logic, app permissions, Room schemas or Gradle dependency versions were changed.

## 15. Remaining issues and user actions

1. **Review the phone data state.** The connected-test task removed the app package. Android backup is disabled, so old app-only data may have been lost. If there is a manually exported HydroNudge JSON backup, import it in the app. I did not inspect personal storage.
2. **Authorize JetBrains Codex if desired.** Follow the ChatGPT OAuth steps in section 6; this is a one-time browser authorization and uses the existing ChatGPT plan.
3. **Restart Android Studio once.** Save open work first so the running IDE and integrated terminal see the updated Git/JBR/ADB PATH. The IDE was not restarted to preserve the open project/editor state.
4. **Set Git identity before the first commit.** After choosing the name/email you want attached to your commits, run `git config --global user.name "Your Name"` and `git config --global user.email "you@example.com"`. No identity was guessed and no commit was created.
5. **Use an emulator for instrumentation.** No virtual device exists yet. Create one in Device Manager using the installed API 37 Google Play image before running `connectedDebugAndroidTest`; do not target the Samsung.

## 16. Beginner daily workflow

1. Open Android Studio and choose **Open**, then select this existing HydroNudge folder.
2. Use local Codex (or, after ChatGPT OAuth, select Codex in Android Studio AI Chat). The project `AGENTS.md` explains its safety rules.
3. If Samsung Wi-Fi debugging disconnected, run `scripts/device.ps1 -Action list`; reconnect with the current endpoint from Device Manager if needed.
4. Build and install: run `scripts/build-debug.ps1`, then `scripts/device.ps1 -Action install -Serial <ADB-serial>`, then `scripts/device.ps1 -Action launch -Serial <ADB-serial>`.
5. Let Codex run `scripts/test-unit.ps1` and `scripts/lint.ps1` after relevant code changes. Use an emulator for instrumentation; the phone test task is safety-gated.
6. If a build fails, keep the output and ask Codex to inspect it. Do not uninstall HydroNudge, clear app data, delete Gradle caches, or accept migrations just to make an error disappear.

## External references

- [JetBrains Codex agent and ChatGPT authorization](https://www.jetbrains.com/help/ai-assistant/codex-agent.html)
- [JetBrains agent activation and ChatGPT OAuth](https://www.jetbrains.com/help/ai-assistant/activate-agents.html)
- [Official Git for Windows downloads](https://git-scm.com/install/windows)
- [Git for Windows 2.56.0.windows.4 release and published SHA-256](https://github.com/git-for-windows/git/releases/tag/v2.56.0.windows.4)
