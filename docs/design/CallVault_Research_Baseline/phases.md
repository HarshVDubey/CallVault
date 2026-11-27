# CallVault — Phases and Delivery Roadmap

**Document:** `phases.md`  
**Version:** 1.0  
**Date:** 27 November 2026  
**Status:** Proposed roadmap; no phase is marked complete by this document  
**Product direction:** Hybrid — Capture + Manage + Understand are equally central

## 1. Delivery strategy

CallVault is a hybrid conversation workspace, not merely a call recorder or transcription utility. The three product pillars are equally important to its positioning:

- **Capture:** microphone recording, user-selected imports, and verified/permitted call-source integrations.
- **Manage:** durable local library, playback, organization, export and optional backup.
- **Understand:** transcription, searchable transcripts, summaries, action items and later conversation Q&A.

Implementation order follows technical dependencies and risk, not product importance. For example, AI understanding depends on valid source audio and a durable library; cloud backup depends on a reviewed key-management design. These dependencies do not demote any pillar in the final product.

### Non-negotiable gates

1. No call-audio support is assumed from call-state detection.
2. No universal cellular or third-party VoIP recording promise.
3. No Accessibility API misuse, hidden capture, root/privileged permission dependency or sideloaded helper APK workaround.
4. Original audio remains available if transcription, summary, upload or backup fails.
5. Local library and playback do not require an account or network.
6. No audio upload or sharing without an explicit, disclosed user action.
7. Research observations and reviewer allegations are not treated as independently reproduced defects.

## 2. Phase overview

| Phase | Focus | Primary outcome | Exit gate |
|---|---|---|---|
| 0 | Product validation, policy and technical discovery | Approved product baseline and feasibility evidence | Scope, risks, decision log and test plan agreed; unsupported capture excluded |
| 1 | Android foundation and data model | Reproducible Android app foundation | Build, navigation, Room and test scaffold pass |
| 2 | Capture + Manage local vertical slice | Reliable mic recording/import, local library, playback and export | Files validate, recover, reopen and play; offline workflows pass |
| 3 | Understand / AI processing | Transcription, searchable transcript, summary and action items | Opt-in processing, durable jobs, evaluation and failure recovery pass |
| 4 | Call-source compatibility experiments | Evidence-backed source matrix and optional permitted adapter | Each advertised call source has repeatable device/OS/app test and policy review |
| 5 | Backend, identity and encrypted backup | Optional cloud backup/sync and governed AI orchestration | Threat model, keys, restore, deletion, cost and access controls pass |
| 6 | Companion web dashboard | Authorized web access to synchronized content | Same API contracts, ownership and local-vs-cloud distinction work |
| 7 | Hardening and Google Play release | Release candidate and compliance package | Release checklist and closed testing exit criteria pass |

Do not set calendar dates until Phase 0 gates, project repository state and provider decisions are known.

## 3. Phase 0 — Product validation, policy and technical discovery

### Objectives
- Confirm the hybrid product definition and initial target-user hypotheses.
- Consolidate the six competitor PDFs and `CallVault_Competitor_Intelligence_Matrix.xlsx` as the research baseline.
- Prioritize real problems shown in the evidence: capture integrity, audio ownership/portability, privacy transparency, recoverable processing, pricing clarity and workflow simplicity.
- Review current Android and Google Play requirements relevant to audio recording, microphone permissions, foreground services, Accessibility API and user data.
- Design permitted microphone recording tests and a separate call-source feasibility matrix.
- Select an initial representative audio corpus for transcription evaluation, using appropriately licensed/consented sample audio.
- Define an initial unit-economics worksheet for AI minutes, storage and bandwidth.

### Deliverables
- Approved `requirement-spec.md`, `spec.md`, `phases.md` and `task.md`.
- Research baseline workbook and evidence traceability.
- Product decision log and open-question register.
- Android permission/policy review checklist with links/date checked.
- Device/source test plan.
- Speech provider evaluation matrix and usage-cost assumptions (provider selection may remain open).

### Exit criteria
- The product owner confirms the three-pillar hybrid definition.
- Scope and non-goals are explicit.
- A permitted microphone capture implementation path is identified for testing.
- Call-source experiments are separated from microphone recording and have no prohibited workaround dependency.
- Technical unknowns and validation steps are documented.
- No unsupported claim has been included in product copy or acceptance criteria.

**Status:** Planning documents prepared; market evidence collection has been completed to an initial qualitative level. Customer willingness-to-pay and device recording feasibility remain unvalidated.

## 4. Phase 1 — Android foundation and data model

### Objectives
- Create or audit the native Kotlin/Jetpack Compose project.
- Establish MVVM/Clean Architecture boundaries.
- Configure Gradle Kotlin DSL, dependency versions and reproducible build.
- Set up navigation, Material 3 design system, logging policy and test infrastructure.
- Create Room schema and migration tests for recordings, capability results, transcripts, summaries and processing jobs.
- Establish the managed app-private file storage abstraction.

### Deliverables
- Reproducible Android project.
- Navigation shell and core design tokens.
- Domain models and repository interfaces.
- Room entities/DAOs/migrations.
- Automated unit and instrumentation test scaffold.
- Build and device-run instructions.

### Exit criteria
- Clean clone/build succeeds.
- App launches on OnePlus 10T / Android 15.
- Navigation and database smoke tests pass.
- No real audio permission or unsupported call recording code is requested/added prematurely.

## 5. Phase 2 — Capture and Manage: local Android vertical slice

This phase must turn the app into a useful product without requiring account, cloud or AI provider availability.

### Capture objectives
- Implement user-visible microphone recording through permitted Android APIs.
- Use a foreground service only when required and correctly configured for the actual recording use case/target API.
- Implement explicit start, stop and supported pause/resume transitions.
- Persist session lifecycle and recover partial recordings.
- Detect permission denial, audio focus conflicts and unexpected interruption.
- Validate finalized file existence, readability, duration, size and playback initialization.
- Do not treat file metadata as proof that remote participant audio was captured.

### Manage objectives
- Import audio via Storage Access Framework.
- Validate supported formats and copy into app-private storage safely.
- Display a unified local library with source labels.
- Implement playback, seek, rename, delete, metadata search, sort/filter and user-mediated export/share.
- Make local access independent of sign-in and network.
- Keep source audio separate from any future transcript or summary.

### Deliverables
- Visible microphone recording workflow.
- Import workflow.
- Recording detail and playback.
- Local library and Room persistence.
- Integrity/finalization and recovery handling.
- Basic export/share and delete controls.
- Compatibility matrix UI with honest states, populated only with evidence.

### Exit criteria
- Repeated start/stop tests pass; stop is terminal and no unexpected restart occurs.
- Screen-off, app switching, interruption and microphone contention behave as documented.
- Empty/truncated/corrupt audio does not receive a normal Complete state.
- Saved items reopen and play.
- Import source is retained until copy validation succeeds.
- Library, playback, rename/delete and metadata search work offline.
- Users can export original audio without an AI subscription.

## 6. Phase 3 — Understand: transcription and AI outputs

### Objectives
- Select a speech-to-text solution after evaluating quality, supported languages, latency, file limits, retention, privacy and cost.
- Add explicit user-initiated transcription for a selected recording.
- Persist local processing job state and support safe retries.
- Separate upload, transcription and summary statuses.
- Display transcript and timestamps where supported.
- Support corrections and speaker labels as estimates when a provider offers diarization.
- Add transcript search with source/timestamp navigation.
- Generate summaries, key points, decisions and action items linked to the source transcript/audio.
- Label generated content and allow edit/delete.
- Validate output quality using a representative, consented audio test set.

### Deliverables
- Provider adapter/interface.
- Transcription jobs and result persistence.
- Transcript detail screen and editing.
- Search within transcript; cross-recording text search when indexed.
- AI summary/notes workflow.
- Usage/quota disclosure.
- Provider failure and retry report.

### Exit criteria
- No cloud upload without explicit confirmation.
- Original audio remains playable when AI fails or quota is exhausted.
- Jobs survive app restart and cannot silently duplicate provider calls.
- Partial outputs and stuck jobs are surfaced accurately.
- Transcript language, source quality and speaker-identity limitations are clear.
- A measured evaluation report exists for representative test audio.
- AI summaries are traceable to source transcript/audio and clearly labelled generated.

### Deferred within this phase
Conversation Q&A is deferred until transcript search/retrieval is reliable and per-request cost is understood. It must be grounded in the selected conversation and cite relevant timestamps/passages where available.

## 7. Phase 4 — Call-source compatibility experiments

This is a research/engineering gate, not a promise that call-source capture will ship.

### Objectives
- Test cellular SIM separately from every third-party VoIP app.
- Test each source on each device/OS/accessory configuration.
- Identify the exact audio source and permitted Android API/mechanism.
- Verify capture of both sides of a conversation when the user-facing product claim requires it.
- Record known restrictions and failure modes.
- Review current Android and Google Play policy before implementing any adapter.

### Device matrix baseline
- OnePlus 10T 5G / Android 15 — primary early target.
- One current Pixel device — comparison.
- One additional OEM/device — variation check.

### Deliverables
- Compatibility matrix with test date, device build, source app/version, method, permissions, test outcome and limitation.
- Policy review record.
- Optional narrowly scoped adapter design, only where permitted and repeatably verified.

### Exit criteria
- Each source marked Supported has a reproducible, end-to-end test record and a policy-approved method.
- Unsupported and Unverified sources are clearly communicated.
- No sideloaded helper APK, Accessibility workaround, root, privileged permission or concealed capture dependency exists.
- If no permitted path is found, call-source recording remains excluded while the hybrid product continues with microphone and import capture.

## 8. Phase 5 — Backend, identity and encrypted backup

### Objectives
- Confirm whether and why cloud account features are needed for the initial commercial release.
- Establish Spring Boot modular monolith and PostgreSQL schema/migrations.
- Implement authentication, ownership, API validation and OpenAPI contracts.
- Store metadata, job references and backup manifests; do not store raw audio blobs in PostgreSQL.
- Choose private object storage.
- Complete threat model and key lifecycle design.
- Implement client-side encryption before backup upload.
- Implement backup/restore, integrity checks, retries and deletion.
- Add provider orchestration for cloud AI if not already implemented.

### Deliverables
- Backend project and migrations.
- Auth/session design.
- REST API/OpenAPI.
- Private object storage integration.
- Client-side encrypted backup/restore.
- Deletion and account export/delete flows.
- Cost and abuse-control limits.

### Exit criteria
- Local app functionality does not depend on backend availability.
- User ownership enforced for every cloud resource.
- Server/object storage receives ciphertext for backed-up audio.
- Restore integrity verified; lost-key implications explained.
- Deletion propagation and audit trail verified.
- Provider secrets remain server-side.
- Per-user/per-plan usage and cost ceilings exist.

## 9. Phase 6 — Companion web dashboard

### Objectives
- Build responsive React/TypeScript UI with Lovable.
- Consume the same authenticated API contracts as Android.
- Display synchronized recordings, transcript, summaries and backup state.
- Keep local-only and cloud-backed status distinct.
- Support user-owned exports/deletion where authorized.

### Deliverables
- Authenticated dashboard.
- Recording list/detail/player.
- Transcript and summary views.
- Backup/account settings.
- Responsive layout and accessibility check.

### Exit criteria
- Every displayed resource is authorized to the signed-in user.
- No browser UI implies it can record Android cellular/VoIP calls.
- No secrets/database credentials are present in frontend assets.
- Web and Android data ownership/deletion semantics are consistent.

## 10. Phase 7 — Hardening and Google Play release

### Objectives
- Run full regression, security, privacy, accessibility and performance tests.
- Revalidate current Play policy, target SDK, permissions, foreground service declarations and Data Safety answers.
- Complete privacy policy, consent copy, support process and store listing.
- Test app upgrade/migration and crash recovery.
- Run closed testing on representative devices.
- Remove test data, placeholder content and demo-only claims.

### Deliverables
- Release candidate APK/AAB.
- Device compatibility and regression report.
- Security/privacy review record.
- Google Play listing, privacy policy and Data Safety declarations.
- Rollout and incident-response plan.

### Release gate
- All public feature claims have supporting test evidence.
- No unverified call source is advertised as supported.
- No prohibited capture method or hidden upload exists.
- Local data and export behaviour work as stated.
- Critical crash, data loss, privacy or billing defects are resolved.

## 11. Definition of done for every phase

- Scope and acceptance criteria are met.
- Code review completed.
- Automated tests added and passing.
- Error, empty, loading, offline and permission states handled.
- Privacy/security implications reviewed.
- Documentation and decision log updated.
- Status is backed by test or implementation evidence; no task is marked complete merely because code was generated.

## 12. Current status snapshot

| Area | Status at 27-Nov-2026 | Evidence / remaining work |
|---|---|---|
| Product concept | Defined as hybrid | User selected all three pillars equally central |
| Competitor research | Initial qualitative baseline complete | Six PDFs; 45 evidence entries; 20 feature observations; willingness-to-pay remains unvalidated |
| Project architecture | Initial baseline exists | Existing docs specify Android-first hybrid architecture; implementation repo not audited here |
| Android project/build | Unknown | No source repository/build verification was part of this document task |
| Microphone recording | Not verified | Requires on-device lifecycle/integrity testing |
| Call recording | Not verified | Separate compatibility/policy gate |
| Local library/import/playback | Not verified | Requires source/test evidence |
| AI pipeline | Not verified | Provider, quality and cost decisions remain open |
| Backend/cloud/web | Not verified | Implement only after local and AI/cloud requirements are validated |
| Google Play release readiness | Not assessed | Policy and release checklist required |
