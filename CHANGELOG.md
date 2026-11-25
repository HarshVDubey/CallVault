# CallVault Documentation Changelog

## 2026-11-25 — Documentation baseline 1.0

- Reconciled README, PRD, phases, architecture, TRD, memory, app flow, UI/UX brief and backend schema.
- Recorded observed Android project configuration separately from future technology targets.
- Recorded the actual single-module `:app` baseline and observed package tree.
- Corrected navigation terminology to Home, Library, Insights, Backup and Settings while preserving `feature.recordings` as the current package name.
- Recorded premium dark/violet/bottom-navigation direction as user-approved; marked individual design tokens as proposals.
- Added Mobbin research observations based on user-supplied screenshots/collections; explicitly treated references as patterns, not copied assets or proof of functionality.
- Added Home screen, reusable component and interaction specifications.
- Clarified that call recording, transcription, AI, backup, backend and web companion are not yet implemented unless separately verified.
- Added evidence/status governance: Confirmed, Proposed, Unverified and Blocked.
- Preserved recording feasibility, privacy, consent and Google Play constraints.

## 2026-11-26 — CV Design System 1.0 implementation update

- Implemented CallVault semantic colors and Material 3 dark/light color schemes; dark is the default and dynamic color is off by default.
- Implemented typography and shape foundations.
- Added reusable `CVPrimaryButton`, `CVSecondaryButton`, `CVCard`, `CVRecordingBadge` and `CVEmptyState` Compose components.
- Added `CVDesignSystemPreview` and temporarily wired it into `MainActivity`.
- User verified successful emulator launch and supplied screenshots of the corrected dark preview on Medium Phone API 37.0.
- Recorded Phase 1 as in progress; actual Home, five-tab navigation, Room, automated tests and OnePlus 10T validation remain outstanding.
- Recorded status-bar icon contrast/system-bar inset polish as pending.
