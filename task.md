# CallVault — Engineering Task Backlog

**Baseline:** 27 November 2026  
**Rule:** Tasks are not complete until implementation and acceptance evidence are recorded.  
**Traceability:** CV-001…CV-016 refer to `requirement-spec.md`.

## Phase 0 — Documentation, feasibility and policy
- [x] T-001 Review and reconcile product requirements against existing PRD and memory. (P0) — reviewed 27 Nov 2026; reconciliation recorded in memory.md §13.
- [x] T-002 Create device/source compatibility test matrix. (P0, CV-016) — baseline matrix added at docs/feasibility/recording-compatibility-matrix.md; all call-capture rows remain Unverified.
- [x] T-003 Review current Android and Google Play requirements for each proposed capture path. (P0, CV-005/CV-016) — official-source review recorded in docs/feasibility/android-play-policy-review.md.
- [x] T-004 Record tested, unsupported and unverified capture sources separately. (P0, CV-002/CV-016) — statuses updated in docs/feasibility/recording-compatibility-matrix.md.
- [x] T-005 Define privacy disclosures, consent and upload data-flow diagrams. (P0, CV-005) — proposed copy and local/microphone/AI/backup flows documented in docs/feasibility/privacy-consent-data-flows.md.
**Exit:** no planned feature depends on hidden capture, Accessibility misuse, root, sideloaded helper or unsupported privileged access.

## Phase 1 — Android foundation and reliable local media
- [ ] T-006 Audit current Gradle/Kotlin/AGP configuration and document observed versions.
- [ ] T-007 Refactor composable-owned repository/database work into ViewModel/use-case boundaries.
- [ ] T-008 Define recording/import domain states and provenance. (P0, CV-002)
- [ ] T-009 Add safe temporary-file and atomic finalization strategy. (P0, CV-001)
- [ ] T-010 Validate output existence, size, duration and reopenability. (P0, CV-001)
- [ ] T-011 Preserve recoverable partial files after interruption. (P0, CV-006)
- [ ] T-012 Ensure stop is terminal and state/notification agree. (P0, CV-002)
- [ ] T-013 Add tests for start/stop/repeated stop/process death/finalization failure.
- [ ] T-014 Add tests for audio-focus conflict, screen-off and app switching. (P0, CV-006)
- [ ] T-015 Test import formats, corrupt files, large files and unsupported MIME types.
**Exit:** local media lifecycle has explicit success/failure/recovery and regression tests.

## Phase 2 — Library, playback and ownership
- [ ] T-016 Implement Library screen from Room Flow.
- [ ] T-017 Implement playback engine and player state. (P0)
- [ ] T-018 Add seek, duration, playback progress and navigation persistence.
- [ ] T-019 Implement rename and delete with file/database consistency.
- [ ] T-020 Implement sorting and title/date/source search. (P1)
- [ ] T-021 Add basic audio export/share via user-mediated Android intents. (P0, CV-004)
- [ ] T-022 Add transcript text export when transcripts exist. (P0, CV-004)
- [ ] T-023 Show source, provenance, file location/status and storage usage. (P0, CV-003)
- [ ] T-024 Verify library/playback offline and after force-stop/restart.
**Exit:** users can import, retrieve, play, organize, export and delete original audio without an account.

## Phase 3 — Recording compatibility and permitted capture
- [ ] T-025 Implement honest capability matrix UI. (P0, CV-016)
- [ ] T-026 Separate call-state detection from actual audio access in UI and domain model.
- [ ] T-027 Prototype only permitted system/OEM-supported capture paths.
- [ ] T-028 Test both sides of audio, silent output, Bluetooth/wired routes and interruptions.
- [ ] T-029 Publish per-device/source test evidence and last-tested date.
- [ ] T-030 Block or label unsupported/unverified sources without fake recordings.
**Exit:** no recording source is called Supported without repeatable test evidence and policy review.

## Phase 4 — Transcription and durable AI jobs
- [ ] T-031 Compare providers for quality, language coverage, cost, latency, retention and API support.
- [ ] T-032 Implement explicit upload disclosure and confirmation.
- [ ] T-033 Create durable processing-job model and idempotency. (P1, CV-007)
- [ ] T-034 Implement upload/transcription progress as separate states.
- [ ] T-035 Implement retry/cancel/failure handling without deleting source audio. (P1, CV-007)
- [ ] T-036 Store transcript and timestamped segments linked to recording.
- [ ] T-037 Implement transcript editing and speaker-label correction. (P1, CV-008)
- [ ] T-038 Implement transcript search and timestamp navigation. (P1, CV-009)
**Exit:** processing is recoverable, measured and preserves source audio.

## Phase 5 — Understanding features
- [ ] T-039 Add timestamped bookmarks and notes. (P1, CV-013)
- [ ] T-040 Implement grounded AI summaries, key points, decisions and action items. (P2, CV-014)
- [ ] T-041 Link generated sections to transcript/audio timestamps where possible.
- [ ] T-042 Evaluate summary quality on a representative test set.
- [ ] T-043 Prototype conversation Q&A scoped to a single recording. (P2, CV-015)
- [ ] T-044 Add citations/source snippets and refuse unsupported answers.
**Exit:** AI outputs are clearly labelled, editable, source-grounded and cost-bounded.

## Phase 6 — Backend, privacy and optional backup
- [ ] T-045 Define Spring Boot modular boundaries and REST contracts.
- [ ] T-046 Implement cloud metadata schema and migrations in PostgreSQL.
- [ ] T-047 Implement authentication for cloud-only features.
- [ ] T-048 Define client-side encryption format and Android Keystore integration.
- [ ] T-049 Define key recovery and device migration before enabling backup.
- [ ] T-050 Implement optional encrypted backup/restore. (P1, CV-012)
- [ ] T-051 Implement deletion propagation and retention controls.
- [ ] T-052 Model storage, bandwidth and AI processing cost per active user.
**Exit:** cloud services are optional, secure, costed and have tested restore/deletion behavior.

## Phase 7 — Companion web client
- [ ] T-053 Define web use cases and synchronized data boundary.
- [ ] T-054 Build React/TypeScript dashboard through Lovable.
- [ ] T-055 Integrate authenticated backend APIs.
- [ ] T-056 Verify web cannot initiate or claim Android call-audio capture.
**Exit:** web access is limited to explicitly synchronized user content.

## Phase 8 — Hardening and release
- [ ] T-057 Add unit, integration and instrumentation coverage for critical flows.
- [ ] T-058 Run physical-device matrix, including OnePlus 10T / Android 15.
- [ ] T-059 Run security/privacy review and verify Play policy compliance.
- [ ] T-060 Verify accessibility, performance, battery, crash and recovery behavior.
- [ ] T-061 Validate pricing/quota copy and subscription disclosures before monetization.
- [ ] T-062 Prepare store listing, privacy policy, data safety declarations and support path.
- [ ] T-063 Release candidate checklist and rollback plan.
**Exit:** release claims match tested functionality; no critical reliability, privacy or policy issue remains.

## Research traceability
- Capture integrity: CV-001/CV-002/CV-006 — Cube R04/R05; Easy R02/R03/R09/R10; Notta R09/R10/R14; Otter R05/R06.
- Original audio ownership/export: CV-003/CV-004 — Easy R06/R07/R08; Notta R04/R13/R14; Otter R02/R05.
- Privacy/permissions: CV-005 — Cube R01/R03; 1Transcribe R05; Otter R01 (review allegation, not verified).
- Processing reliability: CV-007/CV-008 — 1Transcribe R03; Notta R06/R10/R14; Otter R02/R03.
- Search/understanding: CV-009/CV-014/CV-015 — Notta FO-15/FO-16; Otter FO-17/FO-19.
- Pricing clarity: CV-011 — 1Transcribe R01/R02/R04; Notta R01/R02/R05/R08/R15; Otter R02.
- Call compatibility: CV-016 — Cube R01–R07 and FO observations; do not generalize beyond tested configuration.

## Status
All tasks start unchecked. This file records planned work, not completed implementation.
