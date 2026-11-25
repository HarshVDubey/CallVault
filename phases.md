# CallVault — Development Phases and Delivery Plan

**Version:** 1.0  
**Status:** Baseline delivery plan; phase exit gates are mandatory  
**Date:** 25 November 2026

## Delivery strategy

Android-first, with a strict feasibility gate for call-audio capture. Build the local Android foundation and validate device/source constraints before investing in cloud AI. Lovable is for the companion web dashboard, not a substitute for native Android capabilities.

## Phase 0 — Discovery, feasibility and compliance

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
- Implement Home screen using explicit demo fixtures during development only.

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

**Current status (26 November 2026): IN PROGRESS.** Project generated; initial build/sync succeeded; starter app launched on API 37 emulator. CV Design System 1.0 foundations and reusable Compose components have been implemented, and the user verified the design-system preview on the API 37 emulator with screenshots. The app entry point currently displays the design-system preview. The actual Home screen, five-destination navigation, Room foundation, test baseline and OnePlus physical-device verification remain outstanding.

**Phase 1 progress breakdown**
- [x] Kotlin/Compose starter project builds and launches on API 37 emulator.
- [x] Initial CV color, typography, shape and Material 3 theme foundation implemented.
- [x] Reusable primary/secondary buttons, card, recording badge and empty state implemented.
- [x] Design-system preview wired into `MainActivity` and visually checked on emulator.
- [ ] Correct status-bar icon contrast and validate system-bar insets.
- [ ] Implement actual Home screen and five-destination navigation.
- [ ] Establish Room schema/migration foundation.
- [ ] Add unit/instrumentation test baseline.
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
