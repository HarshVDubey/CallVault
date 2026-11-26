# CallVault — Project Memory and Decision Log

**Purpose:** Durable handoff file for ChatGPT, coding assistants and contributors.  
**Last updated:** 26 November 2026  
**Memory version:** 1.2

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
| Design style | Premium | Confirmed |
| Theme | Dark | Confirmed direction |
| Accent | Violet | Confirmed |
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

User selected premium dark visual direction, violet accent and bottom navigation.

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
- Force-stop/relaunch persistence verification.
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
- **Phase 2 — Import, library and playback: IN PROGRESS.** First SAF import path and Room metadata insertion are demonstrated on emulator; persistence after restart, Library management and playback remain.

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
| 2026-11-26 | First emulator import demonstrated | `sample-3s` appeared in Home with duration and file size; restart persistence still to verify |
