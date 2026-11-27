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

## 2026-11-26 — Modern Colorful visual direction approved

- User selected a new visual direction based on supplied audio-app references: vivid electric-blue/indigo/violet/magenta gradients, selective glassmorphism, expressive audio visuals and an immersive player.
- Superseded the previous premium dark/violet-only visual direction as the active target.
- Updated UI/UX brief, design system, screen specifications, component library and interaction patterns with the new visual baseline.
- Updated README and project memory/decision log to identify the new direction and affected documents.
- Clarified that Home and Player may be more expressive while Library, Backup and Settings should remain calmer and information-focused.
- Existing Android Compose implementation still uses the earlier dark/violet theme; no UI redesign or code implementation is claimed by this documentation change.
- Technical architecture, recording feasibility constraints and implemented import/persistence status remain unchanged.

## 2026-11-27 — Competitor research baseline 1.0

- Added `requirement-spec.md` with hybrid Capture/Manage/Understand positioning and research-informed functional/non-functional requirements.
- Added `spec.md` covering the Android-first hybrid architecture, domain model, audio lifecycle, AI job lifecycle and privacy boundaries.
- Added `task.md` with phased engineering tasks and traceability to CV-001–CV-016.
- Updated `phases.md` with research-aligned delivery sequencing and P0 exit gates.
- Updated `README.md` document index and `memory.md` decision log.
- Incorporated six user-provided competitor PDFs and the Competitor Intelligence Matrix. Reviewer allegations remain unverified reports; no new implementation is claimed.
- Preserved the existing Android/Kotlin, Spring Boot/Java, PostgreSQL and Lovable/React direction and existing verified implementation status.


## 2026-11-27 — T-007 Home ViewModel and import-rollback verification

- Extracted `ImportRecordingUseCase` to coordinate audio import, `RecordingRepository` persistence and `NonCancellable` file rollback on persistence failure while attaching cleanup failures as suppressed exceptions so the original persistence error is preserved.
- Updated `HomeViewModel` (`HomeUiState`) to own recording observation, import execution and UI state via `ImportRecordingUseCase` (decoupled from concrete `AudioImportManager`) and to rethrow `CancellationException`.
- Retained Android SAF picker ownership and presentation in `HomeScreen`; `HomeViewModelFactory` constructs dependencies manually.
- Added the Lifecycle ViewModel Compose and KTX 2.6.1 dependencies.
- Added four JVM unit tests in `ImportRecordingUseCaseTest`; `:app:testDebugUnitTest` passed (`BUILD SUCCESSFUL`, 5 total tests: 4 in `ImportRecordingUseCaseTest`, 1 in `ExampleUnitTest`; 0 failures, 0 errors).
- `:app:assembleDebug` completed (`BUILD SUCCESSFUL`); APK installed and launched on `emulator-5554` (Medium Phone API 37.0), with user-confirmed valid MP3 import, persistence after force-stop/relaunch and unsupported audio rejection without crash.
- Limitations unchanged: OnePlus 10T / Android 15 physical-device validation is outstanding; playback and dedicated Library management are not implemented; no SIM-call or third-party VoIP audio capture capability is claimed; no UI redesign was performed; Phase 1 remains in progress.
