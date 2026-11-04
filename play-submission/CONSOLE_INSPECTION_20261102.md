# Authenticated Play Console inspection

2026-11-02. Initial Console inspection was read-only. Follow-up with owner approval: privacy page published to the separate Vercel project, IARC terms accepted to prepare the content-rating questionnaire, and the account-type clarification request was submitted through Play support. No Play declaration/form was saved, no AAB uploaded, no release created/rolled out, or account changed.

Account: TiniFlegar, PERSONAL, account ID `9217981365657493882`. App: HydroNudge, Console app ID `4974248207644269449`, package `app.hydronudge`, draft.

## Account and eligibility

- Completed: account details explicitly show personal account; contact email, contact phone and public developer email display verified indicators. Private address and phone are deliberately omitted here.
- Not verified: account creation date, completed identity-document verification and physical-device verification. Home shows no outstanding verification task; identity tab displays legal identity sourced from the developer account but no explicit completion result. Do not infer every verification is complete from that absence.
- Completed: Android developer verification lists HydroNudge's package as Registered with three signing keys, updated 2 November 2026. No registration was performed by this session.
- Blocked: app dashboard requires completing app setup before closed testing. It explicitly displays zero opted-in testers and 12 testers continuously opted in for 14 days. Apply for production is disabled. Production page explicitly says access has not yet been granted; open testing also requires production access.
- Blocked: personal-account suitability for this water/wellness app remains unresolved pending Google's human reply. Current official guidance says an organization account is required for health apps; its category guide describes health and fitness apps as including trackers for personal health goals in wellness and nutrition. That makes HydroNudge's manual water-intake journal plausibly within the health-app scope even though it is not medical or research software. The account-type page does not define a non-medical wellness exception. The human support request is submitted and queued; Console's generated AI suggestion is not formal policy approval. Sources checked 2026-11-02: https://support.google.com/googleplay/android-developer/answer/13634885 and https://support.google.com/googleplay/android-developer/answer/13996367.

## Releases, package and versions

- Completed: Home associates the draft with `app.hydronudge`; no public-listing inference is used.
- Completed: Latest releases and bundles has no releases, no active filters/search entered and latest bundles = None. All app bundles shows the empty introductory state, without uploaded artifact rows.
- Completed: Internal testing is inactive (0/3 setup tasks), no releases. Closed-testing inventory contains the existing Alpha track only. Alpha is inactive (0/4 setup tasks), no releases. Open/production pages are access-gated as described above.
- Ready for owner review: retain local versionName 1.0.0 and versionCode 1; no use of code 1 was found in this account/app's visible bundle/release inventory. Recheck immediately before upload. This is scoped Console evidence, not a guarantee about other accounts or future concurrent uploads.

## Play signing identity — preserve it

App integrity redirects to Protect with Play. Expand Play Store protection, then Manage Play App Signing. The signing page shows an in-use app-signing key, 0.0% install base, and a previous-key row dated 2 November 2026 17:55 with a quantum-ready beta label. No key-change control was used.

Downloaded the in-use key's PUBLIC certificate ZIP through Console. It contains three certificates; no private key material:

| Certificate file | SHA-256 |
|---|---|
| deployment_cert.der | `54:6F:21:33:33:07:C7:D0:B1:D6:4B:A7:52:83:AC:AF:E2:82:44:E6:3F:E9:55:CF:B9:28:28:EE:53:68:94:67` |
| hybrid_classical_cert.der | `15:6C:B4:FA:BD:DC:16:8C:C4:BF:16:BD:6B:C6:CF:74:A5:46:B7:7C:69:7A:8A:A3:92:59:9D:28:5C:BD:BA:1E` |
| hybrid_pqc_cert.der | `F8:ED:B0:00:E6:4D:13:AC:D7:B0:85:19:FE:3D:5B:F4:0B:FF:8E:6B:05:16:0C:DD:32:6A:29:0F:50:04:65:C0` |

Inspected with Android Studio JBR keytool; first two are 4096-bit RSA, third ML-DSA-65. The page's Digital Asset Links JSON also lists the deployment certificate fingerprint. Local copies are in ignored `qa-reports/closed-test/play-public-certificates/`.

Not verified: upload certificate. The inspected signing page does not display an upload-certificate section. This is not proof that no upload key exists. Owner was asked about secure backups; no owner key has been selected or generated. These Google public certificates cannot sign an upload AAB. Do not pass the app-signing fingerprint as ExpectedUploadSha256 in a real owner-bundle validation.

## Alpha closed-test configuration

Track ID: `4700678043107400713`. Reuse this existing track; no duplicate is needed. Tester tab has no email lists, feedback field is empty, and Save is disabled. Console says the joining link appears when the app is published; Copy link is disabled. A disabled URL preview is not an active opt-in endpoint. Tester instructions intentionally retain the missing-link marker.

Requires owner information: intended age groups, consenting testers' Google-account emails, test countries/regions, and confirmation that the public support mailbox will also receive tester feedback. Do not store private tester emails in Git.

## Local evidence inspected, not rerun

Existing JUnit XML: 31 tests, 0 failures/errors/skips in 10 suites. Existing lint XML: debug and release each 0 errors/fatal issues and 2 warnings. Existing runtime log and cleanup JSON support the reported bounded emulator smoke checks and record/settings restoration; this session did not repeat them. APK alignment log ends Verification successful. Saved final build log reports success with mostly up-to-date tasks.

Current release AAB has no JAR signature blocks and embeds the published policy text and actual HTTPS URL; it remains local and unsigned. Its SHA-256 and the release APK/local test APK hashes are recorded in PLAY_SUBMISSION_STATUS.md. Current icon, feature graphic and five screenshots meet inspected PNG dimension/color requirements; listing character counts are 10/30, 69/80 and 2299/4000. Older screenshots are excluded.

Fresh read-only ADB inventory shows only emulator-5554. No Samsung connected. Its installed certificate and private backup are Not verified; no Samsung action was performed. Before Play installation, identify its actual serial, export and verify its private JSON records (preferences excluded), then compare installed certificate with Play app-signing certificates and stop on incompatibility.

## Official sources checked in this session

- Account type: https://support.google.com/googleplay/android-developer/answer/13634885?hl=en-GB
- Testing: https://support.google.com/googleplay/android-developer/answer/14151465?hl=en
- Health declaration: https://support.google.com/googleplay/android-developer/answer/14738291?hl=en
- Health policy: https://support.google.com/googleplay/android-developer/answer/16679511?hl=en
- Data safety definitions, local processing and user-initiated sharing exception: https://support.google.com/googleplay/android-developer/answer/10787469?hl=en
- Listing text limits: https://support.google.com/googleplay/android-developer/answer/9859152?hl=en-EN
- Graphics: https://support.google.com/googleplay/android-developer/answer/9866151?hl=en
- Ratings: https://support.google.com/googleplay/android-developer/answer/9859655?hl=en
- Signing: https://developer.android.com/studio/publish/app-signing

## Actual declarations inspected

Dashboard app setup is 0/11 tasks. Health form step 1 includes Nutrition and Weight Management, medical/research categories, Other and No health features; none selected/saved. IARC start form required contact email, Game/Social & Communication/All Other App Types category and explicit IARC terms acceptance. With owner approval, contact email and All Other App Types were entered and terms accepted; questionnaire page 2 is open. No answers saved and no rating generated.

Support ticket routing: submitted the approved request through Developer account > General developer account > Other, in English, without attachments. Console lists the case in the support queue and says a reply will be sent by email within two business days (some tickets may take longer). An automated suggestion claims a manual wellness tracker can use a personal account and recommends Nutrition and Weight Management; it is explicitly caveated as error-prone and is not treated as formal policy approval. Await Google's written support reply.

Completed: Android Studio Generate Signed App Bundle or APK wizard opened in the existing HydroNudge project with Android App Bundle selected. Stopped on the artifact-type page, before Next, keystore selection/creation or password entry. No signed build generated. Owner-key backup inquiry remains pending.
