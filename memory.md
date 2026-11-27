# CallVault — Project Memory and Decision Log

**Purpose:** Durable handoff file for ChatGPT, coding assistants and contributors.  
**Last updated:** 26 November 2026  
**Memory version:** 1.3

## 1. Project identity

- Name: CallVault (working name; trademark/domain clearance not completed).
- Product: privacy-first audio library, playback, search, transcription, AI summaries and optional encrypted backup.
- Distribution goal: Google Play Store.
- Primary platform: Android.
- Development strategy: hybrid.
- Priority: working Android app, not web prototype.
- Primary physical compatibility target: OnePlus 10T 5G / Android 15.

## 2. User-approved decisions

| Area | Decision | Status |
|---|---|---|
| Product | CallVault | Working name |
| Platform | Android-first | Confirmed |
| Architecture | Hybrid Android + backend + companion web | Confirmed |
| Native language/UI | Kotlin + Jetpack Compose | Confirmed |
| Android architecture | MVVM with Clean Architecture boundaries | Confirmed |
| Local database | Room | Integrated foundation |
| Backend | Spring Boot modular monolith | Confirmed direction |
| Backend language | Java | Confirmed |
| Cloud relational DB | PostgreSQL | Confirmed direction |
| Web builder | Lovable | Confirmed |
| Web client | React/TypeScript | Target |
| Backup | Optional client-side encrypted audio backup | Requirement |
| Distribution | Google Play Store | Goal |
| Call types | Cellular SIM and internet/VoIP both desired, subject to feasibility | User preference |
| Recording modes | Manual and automatic both desired, subject to feasibility/policy | User preference |
| Product features | Playback, search, transcription, summaries, backup | User-selected scope |
| Design style | Modern Colorful premium audio interface | User-approved 26 Nov 2026 |
| Visual treatment | Blue/indigo/violet/magenta gradients with selective glassmorphism | Approved direction; exact tokens proposed |
| Primary navigation | Bottom navigation | Confirmed |
| Main destinations | Home, Library, Insights, Backup, Settings | Confirmed visual/navigation naming |

## 3. Critical product constraints

- Never promise universal SIM call recording.
- Never promise universal WhatsApp/third-party VoIP recording.
- Call-state detection does not imply call-audio access.
- Android restricts internal call-audio capture for ordinary third-party apps.
- Do not use Accessibility Services to bypass recording restrictions.
- Do not use root-only, privileged permission or hidden capture workarounds.
- Do not fake recording state or present imported/demo audio as CallVault-recorded.
- Recording must be transparent, visible and consent-aware.
- Cloud AI processing requires explicit user action and disclosure.
- Audio backup must be encrypted client-side before upload.
- Verify current Android and Google Play requirements before release; target SDK rules can change.

## 4. Product distinction

CallVault remains useful if cellular/third-party call recording is unavailable:
- Import and manage audio.
- Play and search local recordings.
- Transcribe user-selected files.
- Summarize conversations.
- Back up encrypted audio.
- Offer recording only on verified permitted configurations.

A CallVault-controlled VoIP feature would be a separate product decision with participant consent and legal/policy review.

## 5. Repository and Android project facts

- Repository: `HarshVDubey/CallVault`.
- Default working branch: `main`.
- Android project directory: `android/` inside repository root.
- Android package: `dev.callvault.app`.
- Gradle modules: single `:app`.
- Current package boundaries include core.designsystem.components, data.local (dao/database/entity), data.mapper, data.repository, domain.model/repository/usecase, feature.home/recordings/player/insights/backup/settings, generated ui.theme and MainActivity.kt.
- Package presence alone is not proof of feature implementation.

### Supplied build configuration

| Setting | Value |
|---|---|
| Gradle wrapper | 9.6.0 |
| AGP | 9.5.3 |
| Kotlin | 2.2.10 |
| Compose BOM | 2026.02.01 |
| compileSdk | 37 |
| targetSdk | 37 |
| minSdk | 26 |
| Java source/target | 11 |
| namespace/applicationId | dev.callvault.app |
| Module | :app |

Room/KSP catalog entries and plugin were added. The user encountered an AGP built-in Kotlin/KSP source-set registration error and applied `android.disallowKotlinSourceSets=false` in `gradle.properties`; subsequent builds succeeded. Treat this as a working compatibility workaround, not a long-term resolution. Reassess before upgrading AGP/Kotlin/KSP.

The user reported successful builds after Room setup, DAO cleanup methods, repository implementation and Home import integration. No clean CI build is verified. OnePlus 10T physical testing remains outstanding.

## 6. Design research supplied

User initially selected premium dark/violet styling, then on 26 November 2026 explicitly replaced that direction with a Modern Colorful visual identity based on supplied audio-app references: electric blue, indigo, violet and magenta gradients, selective glassmorphism, expressive artwork and an immersive player. This latest decision is the active visual baseline.

Mobbin references/screenshots supplied:
- Google Drive onboarding.
- Spotify onboarding, library and playback.
- Otter.ai onboarding.
- Apple Music player/system-capture/player patterns.
- Clubhouse voice-message hold/release/cancel interaction (not evidence of phone-call recording feasibility).
- Transcript examples including voice-message transcription and transcript reading/highlighting.
- AI summary examples including a YouTube “Ask about this video” flow.
- Cloud backup examples including iCloud backup settings/history and encrypted backup education.
- Privacy settings examples from multiple apps.

Research is used for interaction patterns, not copying exact layouts/assets. Initial color values in design docs are proposed tokens, not individually approved brand specifications.

## 7. Current implementation status

### Verified implemented
- GitHub repository connected/access available to assistant.
- Kotlin/Compose Android project in repository's `android/` directory.
- Gradle sync/build and API 37 emulator launch succeeded in user's environment.
- CV Design System foundation: semantic colors, Material 3 dark/light schemes, typography, shapes and reusable button/card/recording-badge/empty-state components.
- Home screen and five-destination bottom navigation shell implemented; Library, Insights, Backup and Settings are still placeholder destinations.
- Room foundation implemented:
  - `domain/model/Recording.kt` with `RecordingSource.IMPORTED` and future `CALL_RECORDING` category.
  - `data/local/entity/RecordingEntity.kt`.
  - `data/local/dao/RecordingDao.kt` with insert, update, delete, observe list/count, get by ID and delete by ID.
  - `data/local/database/CallVaultDatabase.kt`, database version 1.
  - `data/mapper/RecordingMapper.kt`.
  - `domain/repository/RecordingRepository.kt`.
  - `data/repository/RecordingRepositoryImpl.kt`.
- `AudioImportManager.kt` copies selected MP3, M4A or WAV audio into app-private `filesDir/recordings`, extracts duration with `MediaMetadataRetriever`, and returns a domain Recording marked `IMPORTED`.
- `AudioImportManager.deleteImportedFile()` cleanup helper restricts deletion to files directly inside the recordings directory.
- Home uses Android `OpenDocument` SAF picker with `audio/*`, performs import/database work on `Dispatchers.IO`, inserts through repository, observes recordings via Flow and displays count, total duration and recent imported items.
- If repository insertion fails after file copy, Home attempts to delete the copied file.
- User supplied successful build reports after each Room/repository/cleanup stage.
- User supplied screenshot showing a successful import: item title `sample-3s`, duration 00:03, size 51 KB, Home Audio items count 1 and total duration 00:03.

### Verified behavior boundaries
- The screenshot demonstrates one successful import on Medium Phone API 37.0 emulator.
- The imported item appears in the live Home UI after the picker flow.
- User confirmed the imported recording remained visible after force-stopping/restarting the app on the API 37 emulator; this verifies the demonstrated Room metadata and app-private file persistence path across that restart.
- Supported extension validation currently allows MP3, M4A and WAV only; the picker may display other audio types, which should produce an unsupported-format message.
- Import metadata is persisted via Room; actual playback has not been implemented.
- The app-private copied audio is not encrypted at rest yet.
- The UI currently creates repository/database access directly in the composable as an initial integration shortcut. Refactor into a ViewModel/presentation layer before expanding feature complexity.

### Not implemented / not verified

- Full Library screen, search, sorting, rename and delete interactions.
- Playback engine and player UI.
- Import size limits, content sniffing and broader corruption/large-file tests.
- Automated unit/instrumentation tests and CI.
- OnePlus 10T / Android 15 physical-device testing.
- SIM or third-party VoIP call-audio capture.
- Recording capability matrix.
- Transcription, AI summaries, backend, PostgreSQL migrations, encrypted cloud backup and Lovable web dashboard.
- Keystore-backed local file encryption.
- Final navigation icons and status-bar/inset polish.

### Current phase status — 26 November 2026
- **Phase 1 — Android foundation and first UI: IN PROGRESS.** Design system, Home/navigation shell, Room foundation and build baseline are present; automated tests and physical-device validation remain.
- **Phase 2 — Import, library and playback: IN PROGRESS.** SAF import, Room metadata insertion and persistence across app restart are demonstrated on API 37 emulator; Library management and playback remain.

### Important implementation paths
- `android/app/src/main/java/dev/callvault/app/feature/home/HomeScreen.kt`
- `android/app/src/main/java/dev/callvault/app/data/repository/AudioImportManager.kt`
- `android/app/src/main/java/dev/callvault/app/data/repository/RecordingRepositoryImpl.kt`
- `android/app/src/main/java/dev/callvault/app/data/local/dao/RecordingDao.kt`
- `android/app/src/main/java/dev/callvault/app/data/local/database/CallVaultDatabase.kt`
- `android/app/src/main/java/dev/callvault/app/data/local/entity/RecordingEntity.kt`
- `android/app/src/main/java/dev/callvault/app/data/mapper/RecordingMapper.kt`
- `android/app/src/main/java/dev/callvault/app/domain/model/Recording.kt`
- `android/app/src/main/java/dev/callvault/app/domain/repository/RecordingRepository.kt`

## 8. Delivery order

0. Discovery and recording feasibility.
1. Android foundation and first UI.
2. Import, library, playback and recording feasibility.
3. Local security and library polish.
4. Backend foundation.
5. AI transcription and summaries.
6. Encrypted backup and synchronization.
7. Lovable companion web dashboard.
8. Hardening and Google Play release.

See `phases.md` for exit criteria.

## 9. Working rules for AI coding assistants

- Read `README.md`, `memory.md`, `prd.md`, `architecture.md` and `trd.md` before architectural changes.
- Read the relevant flow/design specification before implementing a screen.
- Treat these documents as baseline, but do not convert unverified assumptions into facts.
- Do not silently change user-approved technology choices.
- Do not invent Android recording capabilities.
- Do not implement unsupported permissions or policy workarounds.
- Keep changes small and testable.
- Update docs when a decision or implementation status changes.
- Report implemented, partially implemented, blocked and not implemented requirements separately.
- Ask before major architecture/scope changes.
- Distinguish source-derived facts, technical inference and proposed design.
- A screenshot/mockup does not prove the feature is implemented.

## 10. Decision log

| Date | Decision | Reason |
|---|---|---|
| 2026-11-25 | CallVault working name | Project identity |
| 2026-11-25 | Android-first | User priority |
| 2026-11-25 | Hybrid architecture | Native Android + Spring backend + Lovable web |
| 2026-11-25 | OnePlus 10T / Android 15 | Primary physical compatibility target |
| 2026-11-25 | Modular monolith backend | Avoid premature microservices |
| 2026-11-25 | Local-first | Core library works offline |
| 2026-11-25 | Premium dark + violet + bottom navigation | User-approved visual direction |
| 2026-11-25 | Home first | First UI implementation milestone |
| 2026-11-25 | Documentation-first governance | Prevent architectural drift and unsupported claims |
| 2026-11-26 | Room-backed local recording metadata | Persist imported audio metadata separately from private audio files |
| 2026-11-26 | SAF-based audio import | User-mediated file selection without broad storage permission |
| 2026-11-26 | First emulator import demonstrated | `sample-3s` appeared in Home with duration and file size |\n| 2026-11-26 | Import persistence verified across restart | User confirmed imported item remained after force-stop/relaunch on API 37 emulator |

## 11. Latest design decision — 26 November 2026

**Decision:** Modern Colorful visual direction approved.

**Reference interpretation:** Blue/indigo gradient foundations, violet and magenta highlights, cyan audio accents, layered translucent surfaces, expressive audio artwork and an immersive full player. The reference is a visual inspiration source only; do not copy SoundWave branding, protected assets or exact layouts.

**Screen personality:** Home and Player can be expressive; Library should remain organized and searchable; Insights should emphasize structured AI output and source linkage; Backup and Settings should use calmer, more functional surfaces.

**Status boundary:** The current Android implementation still uses the earlier dark/violet theme. The new visual direction is approved for design and documentation, not yet implemented in Kotlin/Compose. Existing architecture, import flow and recording feasibility constraints are unchanged.

**Affected documents:** README, ui-ux-design-brief, docs/design/design-system, docs/design/screen-specifications, docs/design/component-library, docs/design/interaction-patterns, phases and CHANGELOG.

## 12. Research-informed baseline — 27 November 2026

**Product positioning:** Hybrid, with Capture, Manage and Understand equally central. Implementation order remains dependency-driven; dependable local audio and ownership precede advanced AI.

**Research inputs:** Six user-provided competitor PDFs and `CallVault_Competitor_Intelligence_Matrix.xlsx`: 45 evidence-register entries (43 individual review samples, one store-generated summary and one policy observation), 20 feature observations and 16 research-proposed backlog items. Competitors: Cube ACR, Easy Voice Recorder, 1Transcribe, Notta and Otter.

**Evidence handling:** Reviews are user reports, not reproduced defects. Historical reports may describe older versions. A feature not shown in a screenshot is not established as absent. Priority labels are product judgments, not statistically measured market frequencies.

**New planning documents:** `requirement-spec.md`, `spec.md`, `phases.md` and `task.md`. These supplement rather than erase the existing PRD, architecture and TRD. The research baseline does not change verified implementation status.

**Research-derived P0:** capture integrity; accurate recording state and stop behavior; local original-audio ownership; basic export/portability; permission/processing transparency; interruption recovery.

**Important unchanged constraints:** no universal SIM/VoIP recording promise; call-state detection is not audio access; no Accessibility misuse, hidden capture, root-only workaround, privileged-permission assumption or sideloaded helper dependency. Supported capture claims require a repeatable device/OS/source test and current policy review.

**Status:** Documentation baseline committed to GitHub on 27 November 2026. No new feature implementation is implied.


## 13. Requirements reconciliation — 27 November 2026

**Task:** T-001 — Review and reconcile product requirements against the existing PRD and project memory.

**Outcome: no material product-scope conflict found.** The 27 November requirement-spec.md refines and makes testable the existing product direction; it does not replace the PRD or override the user's recorded decisions.

- The PRD's Android-first, local-first, privacy-first hybrid product remains authoritative for release scope and product intent.
- The user's preference for both cellular SIM and internet/VoIP recording, and for manual and automatic modes, remains a preference subject to technical feasibility, consent, law and current platform/store policy. It is not a delivery promise.
- CV-001–CV-006 add measurable P0 integrity, state, local-ownership, export, transparency and interruption requirements. These are consistent with the PRD's import, library, playback, privacy and reliability requirements.
- CV-007–CV-015 refine the previously planned AI, transcript, bookmark, summary, Q&A, entitlement and backup capabilities. They remain future scope, not implemented features.
- CV-016 formalizes the existing rule that call integrations are supported only for repeatably tested and policy-reviewed device/OS/source combinations.
- Existing verified implementation facts in this file remain unchanged. The research baseline does not upgrade any feature's implementation status.

**Governance clarification:** prd.md remains the product/release-scope baseline; requirement-spec.md adds research traceability and acceptance detail; memory.md records explicit user decisions and verified project facts; spec.md defines technical boundaries; phases.md controls sequencing and exit gates; task.md tracks execution evidence. If a conflict appears, preserve explicit user decisions and verified facts, document the resolution here, and do not silently infer a capability.

**Evidence:** Compared the current prd.md, requirement-spec.md, spec.md, phases.md, task.md and this project memory on 27 November 2026. No application code was changed for T-001.


## 14. Recording compatibility matrix — 27 November 2026

T-002 completed: added docs/feasibility/recording-compatibility-matrix.md. The matrix records the API 37 emulator MP3 import as Verified for import only, not recording or playback. SIM, third-party VoIP, microphone capture and OEM/system recording-file paths remain Unverified or not implemented as explicitly scoped. OnePlus 10T / Android 15 remains physically untested. No call-audio capability is claimed. The matrix defines the evidence required before any source status can be changed.


## 15. Android and Google Play policy review — 27 November 2026

T-003 and T-004 completed. Official Android and Google Play documentation was reviewed and recorded in docs/feasibility/android-play-policy-review.md. Key decisions: ordinary Play-distributed CallVault will not attempt SIM-call internal audio capture through public third-party-app APIs; Accessibility-based remote call recording is prohibited by Google Play, and privileged pre-installed capture is unavailable to this app. Third-party VoIP capture remains source-specific and Unverified because Android playback capture is conditional on source-app audio usage and capture policy. A user-started microphone recorder may be evaluated separately with RECORD_AUDIO, microphone foreground-service requirements, visible controls and policy review; it is not call-audio capture. Continue SAF import without broad all-files access. The compatibility matrix now separates these statuses. This is an engineering/policy screening, not jurisdiction-specific legal advice or a Play approval guarantee.


## 16. Privacy and consent data-flow baseline — 27 November 2026

T-005 completed: added docs/feasibility/privacy-consent-data-flows.md. It defines proposed point-of-use disclosures and separate local-import, proposed microphone-recording, future AI-processing and future encrypted-backup flows. It explicitly distinguishes microphone capture from internal call audio and leaves provider, region, retention, cost, legal-market and key-recovery facts as release blockers until decided. The document is a proposed UX/data-flow baseline, not evidence that these features are implemented.


## 17. Repository source availability blocker — 27 November 2026

While beginning T-006, the current GitHub main root listing was inspected. It contains the documentation files and docs/ directory but no android/ directory, Gradle wrapper, settings/build scripts or version catalog. Therefore the Android toolchain values recorded earlier in memory.md and trd.md remain user-reported historical observations, not independently auditable from the current repository state. T-006 remains unchecked/blocked until the Android project source is pushed to main or supplied for inspection. Do not make code or dependency changes based only on the historical version table.
