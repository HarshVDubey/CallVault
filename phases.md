# CallVault — Development Phases and Delivery Plan

**Version:** 1.1  
**Status:** Baseline delivery plan; phase exit gates are mandatory  
**Date:** 26 November 2026

## Delivery strategy

Android-first, with a strict feasibility gate for call-audio capture. Build the local Android foundation and validate device/source constraints before investing in cloud AI. Lovable is for the companion web dashboard, not a substitute for native Android capabilities.

## Phase 0 — Discovery, feasibility and compliance

**Current status (27 November 2026): COMPLETE.** T-001–T-005 are documented in task.md. Deliverables: requirements reconciliation in memory.md; device/source matrix and Android/Play policy review under docs/feasibility/; privacy disclosures and data-flow diagrams under docs/feasibility/. The exit gate is met for the current product plan: prohibited Accessibility/root/hidden/privileged-workaround paths are excluded, SIM-call internal capture is Unsupported for the ordinary Play-app model, third-party VoIP remains source-specific and Unverified, and microphone capture is only a separate proposed feature.

**Objectives**
- Review current Android audio capture APIs and restrictions.
- Review Google Play policies relevant to call recording, sensitive data, foreground services, permissions and user data.
- Define a test matrix for SIM calls, supported system recording paths, imported audio and third-party VoIP.
- Confirm all proposed methods are legitimate, transparent and consent-aware.

**Deliverables**
- Recording feasibility report.
- Device/source compatibility matrix.
- Privacy and policy checklist.
- Technical decision record.

**Exit gate**
- No plan relies on privileged permissions unavailable to ordinary apps, hidden capture, root, Accessibility misuse or third-party app circumvention.
- Every proposed recording source is marked verified, unsupported or unverified.

## Phase 1 — Android foundation and first UI

**Objectives**
- Maintain native Kotlin/Compose project.
- Establish package-based MVVM/Clean Architecture boundaries in the initial single Gradle module.
- Implement design system and five-destination navigation.
- Establish Room schema/migration approach.
- Add test scaffolding and app settings foundation.
- Implement Home screen using real local data where available.

**Deliverables**
- Android project and reproducible build.
- Home, Library, Insights, Backup and Settings navigation.
- Design tokens and reusable Compose components.
- Domain models/repository interfaces.
- Room foundation.
- Build/run instructions.

**Acceptance**
- App builds and launches on the OnePlus 10T / Android 15.
- Emulator smoke test passes.
- All five navigation destinations work.
- No fake recording feature is presented as operational.
- Unit/instrumentation test baseline exists.
- Documentation reflects actual code and test status.

**Current status (27 November 2026): IN PROGRESS.** Project builds and launches on `emulator-5554` (Medium Phone API 37.0). CV Design System 1.0 (no UI redesign performed), Home screen and five-destination navigation shell are implemented (non-Home tabs remain placeholders). Room recording entity/DAO/database, mapper and repository are implemented. T-007 is complete: `HomeViewModel` (`HomeUiState`), `ImportRecordingUseCase` and manual `HomeViewModelFactory` own state, Flow observation, import orchestration and `NonCancellable` rollback cleanup, while `HomeScreen` retains SAF picker launch and presentation. Verified via `:app:testDebugUnitTest` (5 tests passed: 4 in `ImportRecordingUseCaseTest`, 1 in `ExampleUnitTest`; 0 failures, 0 errors), `:app:assembleDebug`, and user-confirmed `emulator-5554` (API 37.0) regression (APK install/launch, valid MP3 import, persistence after force-stop/relaunch and unsupported-format rejection without crash). Instrumentation test baseline, playback, dedicated Library management and OnePlus 10T / Android 15 physical-device validation remain outstanding; no SIM-call or third-party VoIP capture capability is claimed.

**Phase 1 progress breakdown**
- [x] Kotlin/Compose starter project builds and launches on API 37 emulator.
- [x] Initial CV color, typography, shape and Material 3 theme foundation implemented.
- [x] Reusable primary/secondary buttons, card, recording badge and empty state implemented.
- [x] Design-system preview wired into `MainActivity` and visually checked on emulator.
- [ ] Correct status-bar icon contrast and validate system-bar insets.
- [x] Implement initial Home screen and five-destination Compose navigation shell.
- [ ] Replace temporary text-glyph navigation icons with consistent Material icons and refine selected/unselected styling.
- [x] Establish Room recording entity, DAO, database, mapper and repository foundation.
- [x] Refactor Home import into ViewModel and `ImportRecordingUseCase` boundaries (`T-007`), verified by `:app:testDebugUnitTest` (5 tests passed), `:app:assembleDebug` and `emulator-5554` (API 37.0) regression.
- [x] Add initial JVM unit-test coverage for T-007 (5 tests passing).
- [ ] Establish broader unit/instrumentation test baseline.
- [ ] Verify build and UI on OnePlus 10T / Android 15.

A screenshot-based visual check is not a substitute for automated tests or physical-device validation.

## Phase 2 — Audio import, library and playback; recording feasibility

**Objectives**
- Implement audio import using Android Storage Access Framework.
- Persist local metadata.
- Implement playback with Media3 or suitable platform API.
- Build capability assessment.
- Prototype only permitted recording mechanisms supported by test device/platform.

**Deliverables**
- Import flow.
- Local library and playback.
- Recording compatibility matrix.
- Verified recording prototype only if a permitted path exists.
- Unsupported/unverified UI states.

**Acceptance**
- Imported files are reliably listed and played.
- Metadata survives restart.
- Import provenance is preserved.
- Recording status cannot be confused with call-state detection.
- Each claimed capability is verified on a documented device/OS/source combination.

**Current status (26 November 2026): IN PROGRESS.**
- [x] SAF picker connected to Home's Import Audio action.
- [x] Audio import manager copies supported MP3, M4A and WAV selections into app-private storage.
- [x] Duration, file size, MIME type, title and import timestamp are represented in the recording model.
- [x] Imported metadata is inserted through the Room repository.
- [x] Home observes Room recordings and displays item count, total duration and recent imported items.
- [x] User demonstrated successful import of `sample-3s` on Medium Phone API 37.0 emulator (1 item, 00:03, 51 KB).
- [x] User confirmed imported item remains after force-stop/relaunch on API 37 emulator.
- [ ] Validate unsupported/corrupt/empty file handling and large-file behavior.
- [ ] Implement Library list, search, sorting, rename and delete.
- [ ] Implement audio playback and lifecycle/resource handling.
- [ ] Add import/database/file-operation tests.
- [ ] Verify on OnePlus 10T / Android 15.
- [ ] Complete recording capability assessment; no SIM or third-party VoIP capture is currently verified.

**Hard gate:** If cellular or third-party VoIP audio cannot be captured through a permitted mechanism, CallVault remains useful as a recording manager/importer and must not claim otherwise.

## Phase 3 — Local security and library polish

**Objectives**
- App-private audio storage.
- Android Keystore-backed encryption design and implementation review.
- Rename, delete, search, sort and filter.
- Storage management and file integrity checks.
- Export/share via Android-supported user-mediated flows.

**Deliverables**
- Local storage implementation.
- Complete local library.
- Storage and privacy settings.
- Tests for deletion, missing files and interrupted operations.

## Phase 4 — Backend foundation

**Objectives**
- Build Spring Boot modular monolith.
- Add authentication/authorization.
- Define PostgreSQL schema and migrations.
- Add recording metadata and transcript APIs.
- Add audit/security events without storing audio in relational database.

**Deliverables**
- Backend project.
- OpenAPI specification.
- Database migrations.
- API integration tests.
- Secure token/session design.

## Phase 5 — AI transcription and summaries

**Objectives**
- Select speech-to-text provider after cost, language, retention and privacy evaluation.
- Implement explicit user-triggered processing.
- Store transcript and summary metadata.
- Generate key points, decisions and action items.
- Add cancellation/retry and provider failure handling.

**Deliverables**
- Transcription workflow.
- Summary workflow.
- Transcript search.
- AI disclosure and consent UX.

**Acceptance**
- User confirms before audio leaves device.
- AI output is labelled.
- Provider secrets remain on backend.
- User can delete source and derived data.
- Provider retention/deletion terms are documented.

## Phase 6 — Encrypted cloud backup and synchronization

**Objectives**
- Implement client-side audio encryption.
- Upload ciphertext to private object storage.
- Store object metadata and backup state in PostgreSQL.
- Implement restore and conflict resolution.
- Implement account/data deletion.

**Deliverables**
- Backup/restore.
- Sync status.
- Retry/resumable upload as needed.
- Key lifecycle and recovery design.

**Acceptance**
- Server/object storage receives ciphertext, not plaintext audio.
- Restore integrity is verified.
- Lost-key limitations are disclosed.

## Phase 7 — Lovable companion web dashboard

**Objectives**
- Build responsive React/TypeScript web client using Lovable.
- Reuse approved design tokens and component conventions where practical.
- Use mock data initially and label it as demo.
- Connect only after backend API contract is stable.

**Deliverables**
- Dashboard.
- Synchronized recording library.
- Transcript and summary views.
- Backup status and settings.
- Responsive desktop/mobile layout.

**Acceptance**
- No browser UI pretends to access native call audio.
- Real data comes from authenticated APIs.
- Demo data is clearly distinguished from synchronized user data.
- No database credentials or provider secrets in browser bundle.

## Phase 8 — Hardening and Google Play release

**Objectives**
- Security/privacy review.
- Device compatibility tests.
- Accessibility and performance checks.
- Crash and upgrade testing.
- Store listing, privacy policy and Data Safety declarations.
- Closed testing and production rollout.

**Deliverables**
- Release candidate.
- Test report.
- Privacy policy and disclosures.
- Play Console submission package.

**Release gate**
- All advertised features work as described.
- No policy bypass.
- Permissions are necessary and justified.
- Data deletion and support processes work.

## Sequencing rules

1. Complete Phase 1 foundation.
2. Implement import and playback.
3. Validate recording feasibility.
4. Secure and polish local library.
5. Build backend and AI.
6. Build encrypted backup and web companion.
7. Complete release hardening.

Calendar estimates are intentionally not assigned until feasibility and provider decisions are resolved.

## Definition of done for every phase

- Code reviewed.
- Acceptance criteria passed.
- Tests added and passing.
- Error/empty/loading/unsupported states handled.
- Security and privacy implications reviewed.
- Documentation updated.
- No unsupported capability represented as complete.

## Research-informed product baseline — 27 November 2026

The product positioning is **hybrid, with Capture, Manage and Understand equally central**. Delivery sequencing still follows technical dependencies: durable capture/import and local audio ownership precede transcription and advanced AI.

The research-informed requirements are maintained in `requirement-spec.md`; the architecture and technical boundaries are in `spec.md`; the task-level implementation backlog is in `task.md`.

### Updated phase alignment
- **Phase 0:** competitor evidence, recording-source feasibility, Android/Play policy and privacy review.
- **Phase 1:** reliable audio lifecycle, explicit state, finalization and recoverable interruption.
- **Phase 2:** local library, playback, organization and export.
- **Phase 3:** only permitted, repeatably tested recording-source integrations.
- **Phase 4:** durable transcription jobs, editing and transcript search.
- **Phase 5:** summaries, bookmarks and conversation Q&A.
- **Phase 6:** backend, client-side encrypted backup and restore.
- **Phase 7:** synchronized companion web client.
- **Phase 8:** hardening, physical-device validation and Play release.

### Research-derived P0 gates
Capture integrity; truthful recording state; local original-audio ownership; basic export/portability; permission and processing transparency; and interruption recovery. See CV-001–CV-006 in `requirement-spec.md`.

All existing phase exit gates remain mandatory. The research backlog does not mark any implementation task complete.
