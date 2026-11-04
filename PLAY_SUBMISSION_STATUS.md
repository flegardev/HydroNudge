# HydroNudge Play closed-test status

**Updated:** 2026-11-03. The existing `app.hydronudge` app, Play App Signing enrollment, and Alpha track are confirmed. No bundle has been uploaded and no test release has been submitted.

## Current release candidate

- Version: 1.0.0 (versionCode 1); optimized local AAB: `app/build/outputs/bundle/release/app-release.aab`.
- Current local artifact SHA-256: `7B1305DD3B9E9706D7B57D7B84FE06B151084E01CFCAAC8A744FD3BA947F9DA1`. This AAB is unsigned and cannot be uploaded.
- Fresh debug build, 32 host tests, and optimized `:app:bundleRelease` succeeded with `--rerun-tasks`; bundletool 1.18.3 validation passed. Package `app.hydronudge`, version 1.0.0/code 1, minSdk 26/targetSdk 37, and permissions were checked. This does not establish owner signing or Play acceptance.
- Exact output: `qa-reports/closed-test/current-release-validation-20261103.log`.
- Store copy and current genuine screenshots: `release-assets/listing/` and `release-assets/minimal-update/`. Published policy: https://hydronudge-privacy.vercel.app/.

## Owner and policy gates

- The account is Personal. A human support question about health-app eligibility was submitted; its response has not been verified in this update. Do not treat automated guidance as approval or change account type to bypass policy.
- Intended audience includes children, teenagers, and adults. No age groups have been saved. The existing review found child and adolescent suitability gaps in goals, large/repeated entries, and caregiver guidance. Smallest correction: keep goals optional and caregiver-set without pediatric volume advice, add bounded accidental-entry safeguards, publish the reviewed caregiver privacy wording, then complete a guardian-led suitability check. Do not declare unsupported ages or relabel the audience as adults-only.
- The actual IARC questionnaire has not been answered or submitted. App content and Data safety declarations remain unsaved.
- The Play upload certificate and matching owner keystore are not identified. No new key has been created. Never use the debug key or replace Play App Signing.

## Test release state

The last authenticated Console inspection (2026-11-02) recorded no uploaded bundle, no active tester link, and no opted-in testers. The real tester email list and test countries have not been provided. Release notes are in `play-submission/testing/release-notes-en-US.txt`; owner must confirm the feedback contact and countries. No invitation or release submission has occurred.

## Essential records

- Console evidence: `play-submission/CONSOLE_INSPECTION_20261102.md`
- Declaration review: `play-submission/DECLARATIONS_OWNER_REVIEW.md`
- Signing guide: `docs/RELEASE_SIGNING_GUIDE.md`
- Latest release validation evidence: `qa-reports/closed-test/` and `qa-reports/release-preparation/`
