# CallVault — Engineering Task Breakdown

**Document:** `task.md`  
**Version:** 1.0  
**Date:** 27 November 2026  
**Status:** Planned backlog; tasks are unchecked unless verified in the codebase  
**Source of truth:** `requirement-spec.md`, `spec.md`, `phases.md`, and `CallVault_Competitor_Intelligence_Matrix.xlsx`

## 1. How to use this task list

- Complete tasks in dependency order; do not jump to lower-priority AI/cloud work while core audio integrity is unproven.
- All three product pillars—Capture, Manage and Understand—remain equally central. Sequencing reflects dependency, not reduced product importance.
- All tasks start unchecked. The implementation repository was not audited while preparing these documents, so existing code may satisfy some tasks. Audit and attach evidence before changing status.
- Mark a task complete only when its acceptance criteria pass and test evidence is recorded.
- Do not implement a call-audio integration merely because a competitor advertises it or a call-state event is observable.
- Do not change approved technology decisions without a documented decision and owner approval.

### Priority definitions

- **P0:** required for the core local product and trust/safety gates.
- **P1:** required for useful AI capability and a credible paid product.
- **P2:** advanced/optional capability or later expansion.

### Task status

- `[ ]` Not verified / not complete.
- `[x]` Complete only after acceptance evidence is attached.
- `BLOCKED` Use a note in the task row or project decision log; do not silently skip.

---

## 2. Phase 0 — Product, evidence and feasibility

### Research and product baseline

- [ ] **CVT-001 — Freeze competitor research baseline** (P0)
  - Confirm the six PDFs and `CallVault_Competitor_Intelligence_Matrix.xlsx` are stored with the project documentation.
  - Preserve source IDs S01–S06, evidence IDs CUBE-R01…OTTER-R07 and feature observation IDs FO-01…FO-20.
  - Confirm 45 entries are classified as 43 individual review samples, one store-generated synopsis and one policy/screenshot observation.
  - Acceptance: source register and page references remain traceable; no review allegation is rewritten as a verified defect.

- [ ] **CVT-002 — Approve hybrid product positioning** (P0)
  - Record Capture, Manage and Understand as equally central pillars.
  - Define initial user hypotheses: professionals/freelancers, students/researchers and everyday audio users.
  - Confirm the core promise: capture/import, preserve and retrieve/understand conversations.
  - Acceptance: product copy does not promise universal call recording; no pillar is removed from product strategy.

- [ ] **CVT-003 — Review current Android and Google Play requirements** (P0)
  - Verify current requirements for microphone permission, foreground services, notification behaviour, sensitive data, Accessibility API, target SDK and Data Safety.
  - Record official source links and date checked.
  - Acceptance: permission/policy checklist reviewed before any call-source implementation or Play release.

- [ ] **CVT-004 — Define device/source compatibility test protocol** (P0)
  - Include OnePlus 10T / Android 15, one Pixel device and one additional OEM device.
  - Separate microphone, cellular SIM and each third-party VoIP source.
  - Record device model/build, Android version, source app/version, route/accessory, method, permissions, test data and result.
  - Acceptance: a test record template exists; no source is marked Supported without controlled end-to-end evidence and policy review.

- [ ] **CVT-005 — Select representative test audio corpus** (P0)
  - Define consented/licensed samples covering quiet/noisy audio, single/multiple speakers, different speaking rates and intended languages.
  - Define transcript quality criteria before provider comparison.
  - Acceptance: test corpus and evaluation rubric are documented; no private competitor/user review audio is reused as training data.

- [ ] **CVT-006 — Model AI and storage costs** (P1)
  - Estimate per-minute STT cost, LLM tokens per summary, retries, storage per hour/month and bandwidth.
  - Include free, typical and heavy-user scenarios.
  - Acceptance: provider selection and price hypotheses include realistic variable-cost estimates and safety margins.

### Product and technical decisions

- [ ] **CVT-007 — Resolve open platform/build decisions** (P0)
  - Confirm minimum SDK and release target SDK.
  - Pin compatible Kotlin, Compose, Gradle, Android plugin and dependency versions.
  - Choose backend build tool and migration tool when backend work begins.
  - Acceptance: version catalog/build configuration is reproducible and decisions are logged.

- [ ] **CVT-008 — Audit current implementation before coding** (P0)
  - Inspect repository structure, build status, current screens, services, permissions, database, tests and uncommitted changes.
  - Map existing implementation to requirements CV-FR-001…CV-FR-022.
  - Acceptance: an implemented/partial/missing/blocked audit is produced; no existing work is overwritten on assumption.

---

## 3. Phase 1 — Android foundation and data model

- [ ] **CVT-009 — Establish reproducible Android build** (P0)
  - Set up Kotlin, Jetpack Compose, Material 3 and Gradle Kotlin DSL.
  - Pin dependency versions and configure debug/release variants.
  - Acceptance: clean build and install pass on OnePlus 10T / Android 15; build instructions documented.

- [ ] **CVT-010 — Establish architecture boundaries** (P0)
  - Define app/presentation, domain and data packages or modules.
  - Add repository interfaces and dependency injection setup.
  - Acceptance: Compose screens do not access Room, files or network clients directly.

- [ ] **CVT-011 — Implement navigation shell and design tokens** (P0)
  - Add Home, Recordings, AI Insights, Backup and Settings destinations.
  - Create semantic theme, typography and shared components.
  - Include loading, empty, error, offline and unsupported states.
  - Acceptance: navigation is testable and screen state is not represented by fake production data.

- [ ] **CVT-012 — Create local domain models and Room schema** (P0)
  - Add Recording, CapabilityResult, Transcript, Summary, ProcessingJob and Tag models.
  - Add entities, DAOs, converters, indexes and migration path.
  - Acceptance: DAO tests and migration tests pass; timestamps and IDs follow the documented conventions.

- [ ] **CVT-013 — Implement managed app-private file repository** (P0)
  - Create, finalize, list, resolve, rename where supported and delete managed audio files.
  - Keep Room file references stable and private.
  - Acceptance: interrupted writes can be identified/recovered; missing paths produce domain errors instead of crashes.

- [ ] **CVT-014 — Add automated test scaffold and CI build check** (P0)
  - Add unit tests, instrumentation test dependencies and a reproducible build command.
  - Acceptance: at least one unit test, one Room instrumentation test and CI build check pass.

---

## 4. Phase 2 — Capture and Manage local vertical slice

### Microphone capture and integrity

- [ ] **CVT-015 — Implement contextual microphone permission flow** (P0; CV-FR-004/CV-018)
  - Request `RECORD_AUDIO` only when the user starts a microphone recording.
  - Explain purpose and data handling before the system permission prompt.
  - Acceptance: denial, permanent denial and permission revocation produce clear states; no recording begins without permission and explicit action.

- [ ] **CVT-016 — Implement visible recording session service** (P0; CV-FR-001/CV-FR-005/CV-002)
  - Use a user-visible foreground recording service only as required by the target API/policy.
  - Show source, elapsed time, status and stop action.
  - Persist session ID/state and handle lifecycle events.
  - Acceptance: repeated start/stop works; Stop is terminal; notification reflects the real session; no hidden capture.

- [ ] **CVT-017 — Implement interruption and audio-focus handling** (P0; CV-FR-005/CV-FR-006)
  - Detect microphone contention, audio focus loss, app switching, screen lock and interruption where possible.
  - Persist and expose interrupted/paused/failed states.
  - Acceptance: no silent failure; recoverable partial audio is retained; app does not silently restart recording.

- [ ] **CVT-018 — Implement finalization and capture-integrity validation** (P0; CV-FR-006/CV-001)
  - Flush/close output, validate container readability, plausible size/duration and metadata, then attempt playback initialization.
  - Preserve suspect/failed outputs with a visible diagnostic state where safe.
  - Acceptance: empty, truncated or unreadable files cannot be marked normal Complete. Do not infer remote-party audio from metadata alone.

- [ ] **CVT-019 — Implement session recovery and crash-safe temporary files** (P0; CV-FR-006)
  - Detect stale in-progress sessions at app restart.
  - Offer recovery/discard for recoverable partial files.
  - Acceptance: process kill/reboot test does not silently discard recoverable audio or mark an incomplete file complete.

### Import, library and playback

- [ ] **CVT-020 — Implement Storage Access Framework audio import** (P0; CV-FR-007)
  - Support initial tested formats selected from M4A/AAC, MP3, WAV and OGG/Opus.
  - Validate content, readable stream and size; copy to private storage; preserve source until verified.
  - Acceptance: valid imports appear as `IMPORTED`; corrupt/unsupported files show actionable errors.

- [ ] **CVT-021 — Implement unified local recording library** (P0; CV-FR-008/CV-003)
  - Display microphone recordings and imports with title, date/time, duration, source and file/processing status.
  - Add empty, search-empty, loading and error states.
  - Acceptance: library loads offline and does not show demo data as real user content.

- [ ] **CVT-022 — Implement reliable local audio playback** (P0; CV-FR-009)
  - Implement play, pause, seek, elapsed/total duration and lifecycle resource release.
  - Acceptance: playback works offline; missing/corrupt media produces recoverable errors; repeated navigation does not leak player resources.

- [ ] **CVT-023 — Implement rename, delete, sort and metadata search** (P0; CV-FR-010)
  - Support rename, delete, sort by date/title and filters by source/status.
  - Add search by title/date/source initially.
  - Acceptance: metadata and file deletion semantics are explicit; search opens the correct item.

- [ ] **CVT-059 — Add timestamped bookmarks and highlights** (P1; CV-FR-010/CV-013)
  - Allow users to add, rename and delete a bookmark at the current playback position; allow a short optional note.
  - Show bookmarks in recording detail and seek to the corresponding timestamp.
  - Acceptance: timestamps remain stable after playback/reopen; bookmarks are attached to the correct recording and do not modify the original audio.

- [ ] **CVT-024 — Implement basic audio export/share** (P0; CV-FR-011/CV-004)
  - Export original audio using Android-supported user-mediated mechanisms.
  - Show destination and result/cancellation.
  - Acceptance: no share action occurs automatically; exporting original audio does not require AI subscription.

- [ ] **CVT-025 — Implement compatibility matrix data model and screen** (P0; CV-FR-003/CV-016)
  - Add Supported, Unsupported, Unverified and Not Applicable statuses.
  - Include tested device/OS/source/app version, test date, evidence and limitations.
  - Acceptance: only documented repeatable tests can create Supported status; no unsupported source displays an active call-record button.

### Phase 2 quality gate

- [ ] **CVT-026 — Run local capture/library regression suite** (P0)
  - Test repeated sessions, screen off, app switching, phone call/audio focus interruption, process kill, low storage, invalid import, deletion and export.
  - Acceptance: test report includes device/OS, results, defects and unresolved limitations.

---

## 5. Phase 3 — Understand: transcription and AI

- [ ] **CVT-027 — Evaluate speech-to-text providers** (P1)
  - Compare accuracy on test corpus, languages, diarization, latency, limits, price, retention, data residency and deletion.
  - Consider on-device and cloud options separately.
  - Acceptance: decision record includes measured results and cost per audio minute; provider not selected by marketing claims alone.

- [ ] **CVT-028 — Define processing consent and quota UX** (P1; CV-FR-012/CV-020)
  - Show selected recording, destination/provider category, what is uploaded, limits and retention summary.
  - Acceptance: explicit confirmation occurs before cloud transfer; usage units and pricing are unambiguous.

- [ ] **CVT-029 — Implement durable processing job model and queue** (P1; CV-FR-013/CV-007)
  - Persist queued/uploading/processing/completed/failed/cancelled state and retry metadata.
  - Use idempotency for cloud jobs.
  - Acceptance: job state survives app restart and retry does not create unintended duplicate billable jobs.

- [ ] **CVT-030 — Implement audio upload/transcription adapter** (P1; CV-FR-012)
  - Add provider abstraction and explicit processing request.
  - Keep secrets on backend if cloud processing is used.
  - Acceptance: original audio remains locally available when upload or provider processing fails.

- [ ] **CVT-031 — Implement transcript persistence and display** (P1; CV-FR-014)
  - Store transcript, language, timestamps and provider/method metadata.
  - Label transcript as AI-generated.
  - Acceptance: transcript links to source recording and can be deleted independently.

- [ ] **CVT-032 — Add transcript correction and speaker-label editing** (P1; CV-FR-014/CV-008)
  - Allow edits to transcript text and diarization labels when supported.
  - Show speaker identification as an estimate, not verified identity.
  - Acceptance: edits persist; uncertainty/unsupported language is visible.

- [ ] **CVT-033 — Implement transcript search with timestamp navigation** (P1; CV-FR-015/CV-009)
  - Search within a transcript, then across recordings after indexing.
  - Acceptance: a result opens the correct source and seeks to timestamp when available; lexical vs semantic search is distinguished.

- [ ] **CVT-034 — Implement AI summaries and action items** (P1/P2; CV-FR-016/CV-014)
  - Generate overview, topics, key points, decisions and action items from transcript.
  - Include source references/timestamps where available.
  - Acceptance: output is labelled AI-generated, editable and deletable; uncertainty is not hidden.

- [ ] **CVT-035 — Evaluate AI output quality and cost** (P1)
  - Score transcript quality and summary faithfulness on the test corpus.
  - Calculate cost per typical user and heavy user.
  - Acceptance: thresholds, failure cases and provider cost assumptions documented before paid launch.

- [ ] **CVT-036 — Implement conversation Q&A only after retrieval gate** (P2; CV-FR-017/CV-015)
  - Scope Q&A to one selected conversation initially.
  - Ground answers in transcript/audio references and return an explicit no-answer state.
  - Acceptance: evaluation detects unsupported answers; cost and privacy controls exist. Do not implement before transcript/search reliability is measured.

---

## 6. Phase 4 — Call-source feasibility and adapters

- [ ] **CVT-037 — Test cellular SIM capture feasibility** (P0 gate, optional product feature)
  - Evaluate only legitimate available mechanisms on each documented device/OS/carrier configuration.
  - Test remote-party and local-party audio separately with participant knowledge/consent.
  - Acceptance: either documented supported evidence is produced or source remains Unsupported/Unverified. No bypass workaround.

- [ ] **CVT-038 — Test each third-party VoIP source separately** (P0 gate, optional product feature)
  - Test each named source app independently; do not extrapolate from one app to another.
  - Review source app playback-capture policy and Android restrictions.
  - Acceptance: supported status requires a permitted, repeatable end-to-end method and both-party audio evidence where claimed.

- [ ] **CVT-039 — Implement an adapter only for a verified permitted source** (P2, conditional)
  - Keep source adapter isolated from microphone and import recording.
  - Add runtime capability checks and exact limitations.
  - Acceptance: policy review passed; no Accessibility workaround, root, hidden capture, restricted permission assumption or sideloaded helper APK.

- [ ] **CVT-040 — Publish compatibility matrix and user explanations** (P1; CV-FR-003/CV-016)
  - Show current device-specific status, test date and known limitations.
  - Acceptance: status cannot be generalized to untested devices/app versions.

---

## 7. Phase 5 — Backend, identity and encrypted backup

- [ ] **CVT-041 — Finalize backend/cloud requirements and threat model** (P0 before cloud implementation)
  - Define users, metadata sync, AI orchestration, upload, backup, restore, export, deletion and audit requirements.
  - Acceptance: data flow diagram, threats, retention and key-loss behaviour reviewed.

- [ ] **CVT-042 — Establish Spring Boot modular monolith** (P1)
  - Use Java and Spring Boot 3.x; pin exact version; select Gradle or Maven and Flyway or Liquibase.
  - Acceptance: reproducible build, modular package boundaries and integration tests.

- [ ] **CVT-043 — Implement authentication and resource ownership** (P1)
  - Select OIDC/OAuth-compatible or backend-managed session design.
  - Enforce ownership for every API resource.
  - Acceptance: cross-user access tests fail closed; credentials are not stored insecurely.

- [ ] **CVT-044 — Implement PostgreSQL metadata and API contracts** (P1)
  - Add migrations, request validation, error model and OpenAPI.
  - Store metadata/job/object references only; audio blobs remain in object storage.
  - Acceptance: migration, authorization and API contract tests pass.

- [ ] **CVT-045 — Select object storage and encryption/key design** (P0 before backup)
  - Choose private object store, client-side encryption format, key wrapping/recovery and rotation.
  - Use vetted crypto APIs; no custom cipher.
  - Acceptance: threat/security review signs off before live user audio is backed up.

- [ ] **CVT-046 — Implement encrypted backup and integrity verification** (P1; CV-FR-019/CV-012)
  - Encrypt on device; upload ciphertext; persist hash/size/state; retry idempotently.
  - Acceptance: server does not receive plaintext audio; restored file integrity is verified.

- [ ] **CVT-047 — Implement restore, conflict handling and deletion propagation** (P1; CV-FR-021)
  - Restore ciphertext and decrypt locally; manage key loss and duplicate/conflicting objects.
  - Acceptance: tests cover restore, interrupted transfer, deletion and unrecoverable-key messaging.

- [ ] **CVT-048 — Implement account export/deletion and audit events** (P1)
  - Provide user-requested export/delete workflows; log sensitive actions without content.
  - Acceptance: local/cloud/provider data deletion states are documented and exercised.

- [ ] **CVT-049 — Add cloud usage controls and cost limits** (P1)
  - Add rate limits, per-user quotas, upload limits, AI usage metering and abuse protection.
  - Acceptance: no unlimited feature is advertised without a validated cost model.

---

## 8. Phase 6 — Companion web dashboard

- [ ] **CVT-050 — Define shared API contract and web auth handoff** (P1)
  - Align Android and web resource ownership and auth/session behaviour.
  - Acceptance: API schema and error contract are stable before web integration.

- [ ] **CVT-051 — Build Lovable React/TypeScript web shell** (P2)
  - Dashboard, library, detail, transcript/summary and account/backup settings.
  - Acceptance: responsive and accessible; no browser recording controls for cellular/third-party calls.

- [ ] **CVT-052 — Integrate synchronized library and playback** (P2)
  - Read only authenticated and authorized cloud data.
  - Acceptance: local-only and synchronized states are clearly distinguished; revoked access is handled.

- [ ] **CVT-053 — Implement web data export/delete controls** (P2)
  - Use documented APIs and show job status.
  - Acceptance: behavior matches Android and backend ownership rules.

---

## 9. Phase 7 — Release readiness

- [ ] **CVT-054 — Complete Android compatibility and regression matrix** (P0)
  - Test supported Android versions, OEMs, formats, accessories, screen-off and lifecycle cases.
  - Acceptance: release claims are limited to actually tested configurations.

- [ ] **CVT-055 — Complete security, privacy and permission review** (P0)
  - Review threat model, logging, data minimization, local encryption, network traffic, AI disclosure, deletion and backup.
  - Acceptance: no unresolved critical/high privacy or security issue.

- [ ] **CVT-056 — Complete Play Store compliance package** (P0)
  - Recheck current target SDK, permissions, foreground service declarations, Data Safety, store listing and privacy policy.
  - Acceptance: all declarations match app behaviour; no misleading call-recording claims.

- [ ] **CVT-057 — Complete accessibility, performance and crash tests** (P1)
  - Test TalkBack, text scaling, contrast, 1,000-item library, long playback, memory and battery behaviour.
  - Acceptance: release thresholds and known limitations documented.

- [ ] **CVT-058 — Prepare release candidate and closed test** (P0)
  - Remove demo data, validate upgrade/migration, gather crash feedback and prepare staged rollout.
  - Acceptance: release checklist signed off and rollback/support process ready.

---

## 10. Traceability to research backlog

| Research backlog ID | Requirement / task mapping | Original signal |
|---|---|---|
| CV-001 Capture integrity | CV-FR-006; CVT-018, CVT-019 | Cube R05; Easy R03/R09/R10; Otter R05/R06; Notta R10/R14; 1Transcribe R03 |
| CV-002 Recording state transparency | CV-FR-001/005; CVT-016/017 | Cube R02-R05; Easy R02/R09/R10; Otter R01 |
| CV-003 Local original-audio ownership | CV-FR-008/011/021; CVT-021/024/047 | Easy R06/R07; Notta R13/R14; Otter R02/R05 |
| CV-004 Export / portability | CV-FR-011; CVT-024 | Easy R07/R08; Notta R04/R13; Otter R02 |
| CV-005 Permission and processing transparency | CV-FR-004/018; CVT-015/028/055 | Cube R01-R03; Otter R01; 1Transcribe R05 |
| CV-006 Audio focus / background lifecycle | CV-FR-005/006; CVT-017/019/026 | Easy R02/R09/R10; Notta R09 |
| CV-007 Recoverable processing jobs | CV-FR-013; CVT-029/030 | 1Transcribe R03; Notta R07/R10/R14; Otter R05 |
| CV-008 Transcript editing and confidence | CV-FR-014; CVT-032 | Notta R06/R11; Otter R02/R03/R04 |
| CV-009 Transcript search | CV-FR-015; CVT-033 | Notta feature pp.19-26; Otter walkthrough pp.4-5, 10-12 |
| CV-010 Simple capture UX | CV-FR-001; CVT-016/021 | Easy R01/R04 and Easy feature screenshot p.21 |
| CV-011 Clear entitlement and AI quota screen | CV-FR-020; CVT-028/049 | Notta R01/R02/R05/R08/R15; 1Transcribe R01/R02/R04; Otter R02 |
| CV-012 User-controlled backup | CV-FR-019; CVT-045/046/047 | Easy R06; Cube feature p.8; Notta sync onboarding pp.12-15 |
| CV-013 Bookmarks / highlights | CV-FR-010; CVT-059 | Notta feature pp.27-29; Otter walkthrough pp.8-9 |
| CV-014 AI summaries and action items | CV-FR-016; CVT-034/035 | Notta pp.22-26; Otter walkthrough pp.2-4, 11-12 |
| CV-015 Conversation Q&A | CV-FR-017; CVT-036 | Otter walkthrough pp.10-11 |
| CV-016 Supported call integrations | CV-FR-003; CVT-004/037/038/039/040 | Cube R01-R07; Cube feature pp.6, 8, 11-12 |

## 11. Final task completion rules

A task is complete only when:
- Code exists in the repository and passes relevant tests.
- Acceptance criteria are demonstrated on the documented device/configuration.
- Failure and edge cases are covered.
- Security/privacy implications are reviewed.
- Documentation and source-of-truth status are updated.
- Any external feature claim has an evidence record.

Never mark a task complete because a UI mock, code stub, generated screen or unverified API probe exists.
