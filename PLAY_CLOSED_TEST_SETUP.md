# Google Play closed-test checklist

Use the existing HydroNudge app (`app.hydronudge`) and Alpha track. Do not create a duplicate app, replace Play App Signing, or submit to production.

## Before upload

- [ ] Resolve Google's human support decision on whether this health/wellness app may use the existing Personal account.
- [ ] Resolve child suitability for the intended audience and make only evidence-backed Target audience and Families declarations. Do not declare unsupported ages or hide child targeting.
- [ ] Finish the main listing from `release-assets/listing/`, use the 512 px icon, 1024×500 feature graphic, and latest genuine screenshots in `release-assets/minimal-update/`. Enter the published policy URL: https://hydronudge-privacy.vercel.app/.
- [ ] Complete Health apps, Ads (no ads), App access (no restricted access), Data safety, Target audience, and the actual IARC questionnaire from current app behavior. Review every conditional question before saving.
- [ ] Identify the existing upload keystore and match its public certificate to Play Console App integrity. If no matching key exists, stop for owner approval before creating one.
- [ ] Build and validate the optimized owner-signed AAB. Confirm package, version code, permissions, structure, and upload-certificate SHA-256. The local unsigned AAB is not upload-ready.
- [ ] Get the owner's real test countries, consenting tester Google-account emails, and monitored feedback contact. Do not invent or commit tester addresses.

## Release and tester rollout

- [ ] Prepare a release on the existing Alpha closed-testing track with `play-submission/testing/release-notes-en-US.txt`.
- [ ] Show the owner the exact signed AAB/hash and Console release summary; wait for explicit upload/submission approval.
- [ ] Upload and submit only after approval. Fix genuine Console blockers; do not claim success until Console accepts the release and it becomes available.
- [ ] Copy the real opt-in URL from Console and send it with `PLAY_TESTER_INSTRUCTIONS.md`. Track actual continuous opt-ins in `play-submission/testing/participation-ledger-template.csv`; Console is authoritative.

## Current gates

See `PLAY_SUBMISSION_STATUS.md`, `play-submission/DECLARATIONS_OWNER_REVIEW.md`, and `play-submission/CONSOLE_INSPECTION_20261102.md` for current evidence and unresolved owner decisions.
