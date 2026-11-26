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

## 2026-11-26 — Home screen UI milestone

- Implemented initial Home screen with header, honest library overview, quick-action dialogs, recent-items empty state and privacy note.
- Added five selectable bottom navigation destinations in `MainActivity`; four destinations remain placeholders.
- User verified the Home screen and navigation render with an API 37 emulator screenshot.
- Import Audio is not yet connected to Android Storage Access Framework; no real audio data, playback or recording is implemented.
- Recorded temporary text-glyph navigation icons and status-bar contrast as polish items.

## 2026-11-26 — Room foundation and first audio import milestone

- Added Room recording entity, DAO, database singleton, mapper, domain model, repository interface and implementation.
- Added Room/KSP dependencies and plugin aliases. Build succeeds in the user's environment after setting `android.disallowKotlinSourceSets=false` in `gradle.properties`; retain this as a compatibility workaround to revisit, not a permanent architectural decision.
- Added `AudioImportManager` for MP3, M4A and WAV selection, app-private file copy, duration/size/MIME metadata and import provenance.
- Added imported-file cleanup helper and DAO deletion-by-ID method.
- Connected Home's Import Audio action to Android SAF `OpenDocument`; file import and Room insertion execute on `Dispatchers.IO`.
- Home now observes Room recordings and shows live item count, total duration and recent imported items.
- User reported successful builds during integration and supplied an API 37 emulator screenshot showing `sample-3s` imported successfully (1 item, 00:03, 51 KB).
- User subsequently confirmed the imported recording remained after force-stop/relaunch on API 37 emulator; restart persistence is now verified for the demonstrated flow. Playback, dedicated Library management, tests and OnePlus 10T validation remain outstanding.
- No SIM or third-party VoIP call-audio capture capability is claimed or implemented.
