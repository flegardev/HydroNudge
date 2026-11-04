> CURRENT AUTHENTICATED INSPECTION — 2026-11-02: Play App Signing already exists for the confirmed draft app.hydronudge. Do not reenroll or replace it. See ../play-submission/CONSOLE_INSPECTION_20261102.md for downloaded public certificate fingerprints. No uploaded bundles/releases were found; upload certificate was not displayed and owner backup inquiry is pending. Earlier statements below about no Console access are historical. No owner key created or selected. The validator JVM-language argument was quoted for PowerShell; only syntax, public-certificate parsing and unsigned rejection were checked, not successful owner-signed validation.

# Release signing guide — Windows / Android Studio

2026-11-02. Status: OWNER ACTION — public signing identity unverified. PASS — unsigned optimized artifacts can be built without credentials. No upload key was created or replaced.

## Establish identity before choosing a key

Package: app.hydronudge; versionName 1.0.0; versionCode 1. No release signingConfig is checked in. Keep this package identity. The public Play listing lookup returned HTTP 404 and could not establish publication history; there is no authenticated Console evidence in this task. VersionCode 1 is valid for a genuinely unused first upload. Do not increment it merely because this is release preparation. If any track already used it, select a higher unused code after checking all releases.

Owner: inspect Play Console > App integrity and Latest releases and bundles, including drafts/internal/closed/archived tracks. Record the app-signing SHA-256 and upload SHA-256 certificates and locate the matching upload keystore. Do not choose a new identity until ownership and history are resolved. Resetting an upload key through Play recovery is a separate authorized action; it is not permission to replace a published app-signing key.

Android distinguishes the upload key (authenticates your uploaded AAB) from the app-signing key (signs APKs delivered to users). For new Play apps, use Play App Signing. Google can generate the app-signing key, or the owner can supply an eligible existing one. An upload-signed local APK need not update a Play-installed app if those certificates differ; test on a disposable emulator rather than uninstalling a user's app.

## Android Studio procedure — owner operates after identity decision

1. Open this existing project; select Build > Generate Signed App Bundle or APK > Android App Bundle > app.
2. For an existing app choose the verified existing upload keystore and alias. For a confirmed new identity, only after explicit authorization choose Create new. Store the keystore outside this repository and synced/public source folders, in an owner-controlled secure location; choose strong unique passwords in a password manager and accurate certificate identity. Use RSA at least 2048 bits and a long validity (at least 25 years recommended), following current Android guidance.
3. Select release. Avoid saving credentials in source or screenshots. Generate the signed AAB and note its output location. Studio can sign through its wizard without a checked-in Gradle signingConfig.
4. Securely back up keystore, alias, certificate fingerprints and recovery information, with separate protected copies. Losing an upload key may require Play recovery; losing an independently managed app-signing key may prevent updates.
5. Verify the signed bundle, final manifest, version and certificate before any owner-authorized upload. Test the optimized APK locally and later test a Play-generated build through an authorized track. Do not report an unsigned bundle or debug-signed test APK as an owner-signed release.

## Windows command-line alternative — instructions only, no key generation executed

Use Android Studio's bundled JDK. Open PowerShell in a secure owner-only directory outside the project. If the owner explicitly authorizes a new key after confirming there is no existing identity, this command prompts for passwords and identity; do not place passwords in command arguments or shell history:

```powershell
& 'C:\Program Files\Android\Android Studio\jbr\bin\keytool.exe' -genkeypair -v -keystore '.\hydronudge-upload.jks' -alias hydronudge-upload -keyalg RSA -keysize 3072 -validity 10000
```

This is a proposed command, not a completed action. Select your actual secure directory; the filename/alias are choices, not existing credentials. To inspect a selected existing identity without disclosing its private key:

```powershell
& 'C:\Program Files\Android\Android Studio\jbr\bin\keytool.exe' -list -v -keystore '.\hydronudge-upload.jks' -alias hydronudge-upload
```

Build unsigned release with existing wrapper:

```powershell
. ./scripts/_common.ps1
./gradlew.bat --no-configuration-cache --console plain --max-workers=1 testDebugUnitTest lintDebug lintRelease assembleRelease bundleRelease
```

Prefer Android Studio's signed-bundle wizard for the owner signing step. For CI later, configure a dedicated release signingConfig only after key selection, sourcing keystore path/alias/passwords from an ignored local file or secret store; fail clearly if credentials are missing. Never silently fall back to debug signing. Current Gradle configuration intentionally remains credential-free and unsigned. Do not commit passwords, certificates containing private keys, keystores or local machine paths. Existing .gitignore covers common signing files but cannot detect every arbitrary secret name.

Verify a signed AAB with the JDK jarsigner -verify -verbose -certs command and keytool -printcert -jarfile. A verified self-signed upload certificate is expected; compare its SHA-256 with Console rather than treating trust-chain warnings as identity verification. APK verification uses SDK apksigner verify --verbose --print-certs; run zipalign -c -P 16 -v 4 on the signed APK and retain output. Check ELF alignment separately. Do not sign an AAB with apksigner (it is for APKs).

## Local optimized-release test signature

This task may copy the unsigned release APK into ignored qa-reports/release-preparation and sign that copy with the workstation's already existing Android debug certificate. No new key is needed. This exercises minified code/resources while leaving the unsigned AAB/APK intact. The result is a LOCAL TEST RELEASE, not an upload candidate, owner release or Play app-signing identity. Never upload it. The existing debug key is for testing only; standard debug-key passwords are not a secure release configuration.

No Gradle signing identity was persisted. The owner-signed and Play-generated paths remain PENDING until the owner's identity is selected and the exact signed binary is tested. Never uninstall/clear HydroNudge on Samsung to overcome a certificate mismatch.

Official references checked 2026-11-02: [Android app signing](https://developer.android.com/studio/publish/app-signing), [app/version setup](https://support.google.com/googleplay/android-developer/answer/9859152?hl=en), [page-size checks](https://developer.android.com/guide/practices/page-sizes).


## Closed-test reproducibility update — 2026-11-02
Run from the project root in Windows PowerShell:

```powershell
powershell -ExecutionPolicy Bypass -File scripts/prepare-closed-test.ps1
```

This executes the checked-in Wrapper with `--no-configuration-cache`, host tests, debug/release lint, debug and optimized unsigned release APK/AAB builds. It neither signs nor accesses a device. Output `app/build/outputs/bundle/release/app-release.aab` is unsigned until an owner signing operation produces the actual upload artifact.

After confirming Console history and finalizing the policy, use Android Studio's Generate Signed Bundle / APK flow with the correct owner upload key. Keep the signed artifact in a clearly named private local release folder, separate from the unsigned build and emulator APK. Record its version and SHA-256. Do not check secrets, credentials or keystores into Git.

Read-only preflight for the actual signed bundle:

```powershell
powershell -ExecutionPolicy Bypass -File scripts/validate-owner-bundle.ps1 `
  -BundlePath '<actual owner-signed AAB path>' `
  -ExpectedUploadSha256 '<public upload certificate SHA-256 from App integrity>' `
  -BundletoolPath '<local official bundletool-all JAR path>'
```

These are literal placeholders, not credentials. The expected fingerprint is PUBLIC certificate metadata. Obtain bundletool only from https://github.com/google/bundletool/releases if needed. A verified local version 1.18.3 was available in ignored QA tools for this audit; no new tool installation is required here. The script rejects unsigned bundles, remaining owner/draft markers, the known local debug identity and an unexpected upload certificate; it verifies the JAR signature, validates the bundle and inspects its package/manifest. It does not establish account eligibility, prior version-code availability, complete policy compliance or Play runtime behavior. No owner-signed bundle was available to exercise its successful-owner path. Current unsigned-bundle rejection was verified.

For the Samsung, `scripts/inspect-installed-certificate.ps1 -Serial '<exact online Samsung serial>'` only pulls the installed base APK into ignored local QA storage and prints its public certificate. Select the serial yourself; no phone is connected in this session. Compare against Play's APP-SIGNING certificate. Never uninstall or clear data as part of certificate inspection.

Completed: Android Studio Generate Signed App Bundle or APK wizard opened in the existing HydroNudge project with Android App Bundle selected. Stopped on the artifact-type page, before Next, keystore selection/creation or password entry. No signed build generated. Owner-key backup inquiry remains pending.
