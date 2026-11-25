# CallVault — Development Phases and Delivery Plan

**Version:** 0.1  
**Status:** Proposed execution plan

## Delivery strategy

Android-first, with a strict feasibility gate for call audio capture. Build the local Android foundation and prove the device/source constraints before investing in AI and cloud features. Lovable is used for the companion web dashboard, not as a substitute for native Android capabilities.

## Phase 0 — Discovery and compliance

**Objectives**
- Review current Android audio capture APIs and restrictions.
- Review Google Play policies relevant to call recording, sensitive data, foreground services, permissions and user data.
- Define a test matrix for SIM calls, supported system recording paths, imported audio and third-party VoIP.
- Confirm that all proposed recording methods are legitimate and transparent.

**Deliverables**
- Recording feasibility report.
- Device/source compatibility matrix.
- Privacy and policy checklist.
- Updated technical decision record.

**Exit gate**
- No implementation plan depends on privileged permissions unavailable to ordinary apps, hidden capture, root, Accessibility misuse or third-party app circumvention.

## Phase 1 — Android foundation

**Objectives**
- Create native Kotlin/Compose project.
- Establish MVVM/Clean Architecture boundaries.
- Set up navigation and design system.
- Configure Room and migration strategy.
- Add basic app settings and test scaffolding.

**Deliverables**
- CallVault Android project.
- Home, Recordings, AI Insights, Backup and Settings navigation.
- Domain models and repository interfaces.
- Room database skeleton.
- Build/run instructions.

**Acceptance**
- App builds and launches on OnePlus 10T / Android 15.
- All navigation destinations work.
- No fake recording feature is presented as operational.
- Unit/instrumentation test baseline exists.

## Phase 2 — Audio library and recording feasibility

**Objectives**
- Implement audio import using Android Storage Access Framework.
- Implement local metadata persistence.
- Implement playback with Media3 or an appropriate Android media API.
- Build a capability assessment screen.
- Prototype only permitted recording mechanisms supported by test device and platform.

**Deliverables**
- Import flow.
- Local library and playback.
- Recording compatibility matrix.
- Verified recording prototype only if a permitted path exists.
- Clear unsupported/unverified UI states.

**Acceptance**
- Imported files are reliably listed and played.
- Audio metadata survives app restart.
- Recording status cannot be confused with call-state detection.
- Each claimed recording capability is verified on a documented device/OS/source combination.

**Hard gate**
If cellular or third-party VoIP audio cannot be captured through a permitted mechanism, the product remains useful as a recording manager/importer and must not claim otherwise.

## Phase 3 — Local security and library polish

**Objectives**
- App-private audio storage.
- Android Keystore-backed encryption design.
- Rename, delete, search, sort and filter.
- Storage management and file integrity checks.
- Export/share via Android-supported user-mediated flows.

**Deliverables**
- Secure local storage.
- Complete local library.
- Storage and privacy settings.
- Tests for delete, missing files and interrupted operations.

## Phase 4 — Lovable companion web dashboard

**Objectives**
- Build responsive web interface with Lovable.
- Establish shared design tokens and component conventions.
- Use mock data initially, clearly marked as demo.
- Connect only after backend API contract is stable.

**Deliverables**
- Dashboard.
- Recording library.
- Transcript and summary views.
- Backup status and settings.
- Responsive desktop/mobile layout.

**Acceptance**
- No browser UI pretends to access native call audio.
- All real data comes from authenticated APIs.
- Demo data is clearly distinguished from synchronized user data.

## Phase 5 — Backend foundation

**Objectives**
- Build Spring Boot modular monolith.
- Add authentication and authorization.
- Define PostgreSQL schema and migrations.
- Add recording metadata and transcript APIs.
- Add audit/security events without storing audio in the relational database.

**Deliverables**
- Backend project.
- OpenAPI specification.
- Database migrations.
- API integration tests.
- Secure token/session design.

## Phase 6 — AI transcription and summaries

**Objectives**
- Select a speech-to-text provider after evaluating cost, languages, retention and privacy.
- Implement explicit user-triggered processing.
- Store transcript and summary metadata.
- Generate key points and action items.
- Add cancellation/retry and provider failure handling.

**Deliverables**
- Transcription workflow.
- Summary workflow.
- Transcript search.
- AI disclosure and consent UX.

**Acceptance**
- User confirms before audio leaves device.
- AI output is labelled as generated.
- Provider secrets remain on backend, never embedded in app/web client.
- User can delete source and derived data.

## Phase 7 — Encrypted cloud backup and synchronization

**Objectives**
- Implement client-side audio encryption.
- Upload ciphertext to private object storage.
- Store object metadata and backup state in PostgreSQL.
- Implement restore and conflict resolution.
- Implement account/data deletion.

**Deliverables**
- Backup/restore.
- Sync status.
- Retry and resumable upload where supported.
- Key lifecycle and recovery design.

**Acceptance**
- Server/object storage does not receive plaintext audio.
- Restore integrity is verified.
- Lost-key and account-recovery limitations are explained to users.

## Phase 8 — Hardening and Google Play release

**Objectives**
- Security review and privacy review.
- Device compatibility test matrix.
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
- Data deletion and user support processes work.

## Suggested sequencing

Do not estimate calendar dates until Phase 0 and the recording feasibility gate are complete. Prioritize in this order:

1. Build and run Phase 1.
2. Import and playback in Phase 2.
3. Verify native recording capability.
4. Secure and polish local library.
5. Build backend and AI.
6. Build cloud backup and web companion.
7. Release hardening.

## Definition of done for every phase

- Code reviewed.
- Acceptance criteria passed.
- Tests added and passing.
- Error/empty/loading states handled.
- Security and privacy implications reviewed.
- Documentation updated.
- No unsupported capability represented as complete.
