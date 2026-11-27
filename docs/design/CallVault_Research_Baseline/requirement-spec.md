# CallVault — Product Requirements Specification

**Document:** `requirement-spec.md`  
**Version:** 1.0  
**Date:** 27 November 2026  
**Status:** Research-informed baseline; pending product-owner approval and technical feasibility gates  
**Product:** CallVault  
**Platform:** Android-first hybrid product

---

## 1. Product definition

CallVault is a privacy-first conversation and audio workspace with three equally central product pillars:

1. **Capture** — record audio through legitimate, supported mechanisms; import audio from other sources; support call recording only when a permitted method is verified for the relevant device, OS, app and call source.
2. **Manage** — preserve original audio in a reliable, organized library with playback, search, export, deletion and optional backup.
3. **Understand** — turn recordings into transcripts, summaries, key points and action items; enable users to retrieve information from their conversations.

**Product promise:** *Your conversations. Captured, organized, understood.*

The product must not imply that all cellular or third-party VoIP calls can be recorded. CallVault must be useful even on devices where SIM or third-party call-audio capture is unavailable: users can record ordinary microphone audio and import files they are authorized to use.

### 1.1 Hybrid positioning decision

The user selected **Hybrid: all equally central** on 27 November 2026. Capture, management and AI understanding are equal pillars of the product identity and long-term value proposition. Delivery may be staged where engineering dependencies require it (for example, summaries depend on usable transcripts), but AI must not be treated as an incidental feature or excluded from the product definition.

## 2. Problem statement

Users' conversations and recordings can be fragmented across phone recorders, call-recording applications, meeting assistants, file folders and cloud services. They can experience:

- Uncertainty about whether a recording actually captured usable audio.
- Interrupted, empty, silent, missing or difficult-to-retrieve recordings.
- Difficulty managing, locating, exporting or restoring original audio.
- Transcription failures, partial processing, poor speaker/language handling or unclear processing status.
- Confusing usage limits, subscription terms and access to previously created content.
- Unclear recording, microphone, upload and data-retention behaviour.

CallVault's opportunity is to provide a trustworthy lifecycle from capture/import through preservation to useful understanding.

## 3. Research basis and evidence rules

This specification is informed by the user-provided competitor research and the existing CallVault project baseline.

### 3.1 Competitor sources

| Source ID | File | Competitor / scope | Pages |
|---|---|---|---:|
| S01 | `2026-11-27 15-53-59.pdf` | Otter feature walkthrough | 17 |
| S02 | `2026-11-27 15-55-21.pdf` | Cube ACR reviews and feature walkthrough | 18 |
| S03 | `Document 29.pdf` | Easy Voice Recorder reviews and feature walkthrough | 41 |
| S04 | `Document 30.pdf` | 1Transcribe / Transcribe Audio To Text onboarding, features, pricing, policy and reviews | 120 |
| S05 | `notta.pdf` | Notta reviews and feature walkthrough | 150 |
| S06 | `otter.pdf` | Otter reviews and onboarding | 56 |

The companion `CallVault_Competitor_Intelligence_Matrix.xlsx` contains six worksheets: Executive Summary, Review Evidence, Feature Observations, Feature Matrix, CallVault Backlog, and Source Register.

### 3.2 Research coverage

- 6 user-provided competitor PDFs.
- 5 competitor products represented in the feature matrix: Cube ACR, Easy Voice Recorder, 1Transcribe, Notta and Otter.
- 45 evidence-register entries: 43 individual review samples, 1 store-generated review summary and 1 policy/screenshot observation.
- 20 feature/UX observations.
- 16 proposed CallVault backlog requirements.

### 3.3 Interpretation rules

- A review is evidence of what one reviewer reported, not independent proof that the alleged defect occurred.
- Review counts in the research workbook are a curated sample, not a statistically representative sample of the competitor user base.
- Historical reviews must not automatically be treated as current defects.
- A feature not shown in supplied screenshots is **not established by this research**; that does not prove the competitor lacks the feature.
- Store-generated review summaries are not raw reviews and must not be counted as individual user reports.
- Competitor UI and paywall screenshots document the captured experience and may vary by country, date, app version, subscription tier or account.
- Priority assignments in the workbook are research recommendations, not measured complaint frequencies.

### 3.4 Research-derived problem themes

1. **Capture integrity and lifecycle reliability** — missing, silent, prematurely stopped, unexpectedly restarted or difficult-to-find recordings. Highest priority.
2. **Ownership and portability** — inability or difficulty retrieving, exporting, restoring or opening original audio/transcripts.
3. **Privacy and permission transparency** — concern about helper installation, microphone activity, background uploads, permissions and sharing.
4. **Transcription job reliability** — partial outputs, stuck jobs, failed uploads, unclear progress and missing transcripts.
5. **Transcript usability** — accuracy, language selection, speaker attribution, editability and retrieval.
6. **Pricing and entitlement clarity** — monthly vs per-file quotas, trial terms, renewal amounts, exports and access to existing work.
7. **Workflow simplicity** — ads or onboarding friction interfering with the primary recording task.

## 4. Target users and jobs to be done

These are initial product hypotheses, not validated market segments.

### 4.1 Primary user hypotheses

- **Professionals and freelancers:** capture or import client conversations, retrieve commitments and prepare follow-ups.
- **Students and researchers:** record or import lectures/interviews, locate sections and create study/research notes.
- **Everyday audio users:** keep voice notes and important recordings in an organized, searchable place.

### 4.2 Jobs to be done

- When I have an important conversation, I want to know exactly what CallVault can capture on my device and whether the recording was saved successfully.
- When I already have an audio file, I want to import it without losing or modifying the original.
- When I need to revisit a conversation, I want to find the recording and move to the relevant moment quickly.
- When I want a transcript or summary, I want to understand what will be processed, see its progress, and retain the source audio if processing fails.
- When I decide to leave or change devices, I want to export or restore my own content without being locked behind an AI subscription.

## 5. Product principles

1. **Trust over feature count:** do not show successful capture unless there is evidence of a completed, playable file.
2. **Original audio is durable:** AI outputs are derived data; the original recording remains independently accessible.
3. **Local-first core:** local library and playback should work offline and without an account.
4. **Privacy by design:** no covert recording, undisclosed upload, automatic sharing or unneeded permission.
5. **Honest capability claims:** Supported, Unsupported and Unverified must remain distinct.
6. **User control:** explicit initiation of recording, AI processing, backup, sharing and deletion.
7. **Transparent monetization:** do not paywall basic access to users' own original recordings, playback, deletion or basic portability.
8. **Recoverable workflows:** failures are visible, persisted and recoverable where possible.
9. **Platform integrity:** no root-only, privileged, hidden, sideloaded-helper or Accessibility API workaround for remote call-audio capture.
10. **Evidence-led scope:** competitor features alone do not justify implementation; each feature must serve a user problem or validated job.

## 6. Goals and non-goals

### 6.1 Goals

- Deliver one Android-first application spanning capture, manage and understand.
- Make microphone recording and user-selected audio import dependable.
- Build a local library that protects and exposes the original audio.
- Provide playback, navigation, organization and export.
- Provide opt-in transcription and AI understanding with visible job state.
- Evaluate legitimate call recording paths and publish only verified compatibility.
- Add optional encrypted backup and a companion web experience after the local and AI foundations are stable.
- Prepare for Google Play distribution with policy and privacy review.

### 6.2 Non-goals for the initial release

- Guaranteeing every SIM call or every WhatsApp/Telegram/Signal/Meet/Teams/Zoom call can be recorded.
- Recording secretly or suppressing Android indicators and notifications.
- Using Accessibility Services, root, `CAPTURE_AUDIO_OUTPUT`, restricted audio sources or sideloaded helper APKs to bypass platform restrictions.
- Becoming a dialer or building a full VoIP calling service.
- Building Otter-style team collaboration, calendar bots or meeting automation as an initial requirement.
- Unlimited cloud transcription, unlimited AI or unlimited backup without a validated cost model.
- Training models using user audio without separate, explicit consent.

## 7. Scope and release definition

### 7.1 Core hybrid MVP — Android local vertical slice

The first product slice must demonstrate all three pillars at a dependable baseline:

**Capture**
- User-visible microphone recording using an allowed Android path.
- Import audio through Android's Storage Access Framework.
- Source labels that distinguish CallVault microphone recordings, imported files and any separately verified call-source recording.
- Call-source capability assessment; no active record action for Unsupported/Unverified sources.

**Manage**
- Local library, metadata, playback, seek, rename, delete, search by metadata and export/share.
- File integrity checks, interruption handling and user-visible finalization state.
- Local library and playback without network/account.

**Understand**
- A transcription path for a selected recording/file, after processing provider or on-device method is selected and cost/privacy are reviewed.
- Transcript attached to its source recording, with processing state and failure/retry path.
- A basic AI summary or structured notes flow only after transcript persistence and accuracy evaluation are implemented. The release must not present AI as infallible.

### 7.2 Subsequent capability expansion

- Transcript correction, speaker labels/diarization estimates and timestamp navigation.
- Cross-recording transcript search.
- AI summaries with overview, key points, decisions and action items.
- Conversation Q&A grounded in one selected recording/transcript.
- Optional encrypted cloud backup and restore.
- Account, synchronization and companion web dashboard.
- Additional call-source adapters only where a legitimate mechanism is proven on a defined device/OS/app configuration.

## 8. Functional requirements

Requirements below are normative for the target product. Where a feature is subject to platform, provider or policy feasibility, it must remain gated until verified.

### 8.1 Capture

**CV-FR-001 — Microphone recording**  
The app shall allow a user to start, pause where supported, resume and stop a visible microphone recording using a permitted Android API. The app shall display a persistent, user-accessible recording status/stop action where required by Android.

**Acceptance:** a successful session produces a finalized audio file, a persisted library entry and a playable result. Permission denial, microphone contention, interruption and finalization failure produce explicit states; they must not appear as completed recordings.

**CV-FR-002 — Source identity**  
Every audio item shall identify its origin as one of: `MICROPHONE`, `IMPORTED`, or a specifically named/verified call-source type. A call-state event or user-entered contact/call label shall not be treated as proof of audio capture.

**Acceptance:** UI, metadata and exports retain the correct origin label. Imported audio is never presented as recorded by CallVault.

**CV-FR-003 — Call recording capability assessment**  
The app shall present separate capability status for each evaluated source and current device configuration: `SUPPORTED`, `UNSUPPORTED`, `UNVERIFIED` or `NOT_APPLICABLE` where appropriate.

**Acceptance:** Supported status requires documented evidence on the current device/OS/source/app configuration and a permitted capture path. Unverified and Unsupported sources cannot show an enabled CallVault recording action. Evidence includes device model, Android version, source app/version if relevant, test date and method.

**CV-FR-004 — Consent and disclosure**  
Before recording, the app shall explain the source, what audio is captured, where it is stored, what user action stops it, and that participants must be informed/consent as applicable. It shall request only necessary runtime permissions in context.

**Acceptance:** no recording starts before required disclosure, permission and user action. The app does not claim to provide legal advice; jurisdictional obligations must be reviewed before release.

**CV-FR-005 — Recording state transparency**  
The app shall display the actual lifecycle state, elapsed time, source, interruption/error state and finalization state. Stop must be terminal for that session unless the user deliberately starts another session.

**Acceptance:** repeated start/stop, screen off, app switching, process interruption and audio focus contention are tested. A stopped session cannot silently restart.

**CV-FR-006 — Capture integrity**  
Before marking a recording Complete, the app shall verify file existence, readable container/codec, plausible duration and size, metadata persistence and successful reopen/playback initialization. It shall flag outputs that may be silent or incomplete where detectable.

**Acceptance:** empty, truncated, unreadable and metadata-only files do not receive a normal successful status. Metadata validation must not be represented as proof that remote participant audio is present; audio-source limitations remain explicit.

### 8.2 Import and library management

**CV-FR-007 — Audio import**  
The user shall select files through Android's Storage Access Framework. The app shall validate content and supported format, copy/reference safely into app-private storage according to the storage design, and retain the source until the imported copy is verified.

**Acceptance:** unsupported, corrupt, oversized or inaccessible files show actionable errors. An item is labelled `IMPORTED`.

**CV-FR-008 — Local library**  
The user shall view a single library containing eligible CallVault recordings and imported audio. Items shall show title, date/time, duration, source, file state and transcript/summary processing state where applicable.

**Acceptance:** local library opens offline, supports empty/loading/error/no-results states and handles at least 1,000 metadata entries without unacceptable UI degradation on representative test devices.

**CV-FR-009 — Playback**  
The user shall play, pause, seek and view elapsed/total duration for supported audio. Playback must correctly release media resources across navigation and lifecycle transitions.

**Acceptance:** playback works offline for local content; missing, corrupt or unsupported files show recoverable errors rather than crashing the app.

**CV-FR-010 — Organization and retrieval**  
The user shall rename, delete, sort and filter audio items and search by title/date/source. The user shall be able to add timestamped bookmarks/highlights to a recording, with an optional note, after reliable playback is available. User tags and cross-transcript search may be delivered in staged increments.

**Acceptance:** search results navigate to the correct recording; bookmarks seek to the correct playback position; delete behaviour clearly identifies whether it affects audio, transcript, summary and cloud backup.

**CV-FR-011 — Export and portability**  
The user shall be able to export/share the original audio through Android-supported user-mediated mechanisms. Basic transcript text export shall be available when a transcript exists. The destination and completion/cancellation state shall be clear.

**Acceptance:** sharing never begins without explicit user action; supported formats are documented and regression-tested; export of original audio/basic transcript is not blocked solely by an AI subscription.

### 8.3 Understanding / AI processing

**CV-FR-012 — User-initiated transcription**  
The user shall select a specific recording and explicitly request transcription. Before cloud processing, the app shall disclose the audio selected, processing destination, relevant data handling and applicable usage limits.

**Acceptance:** no upload begins without confirmation. The original audio remains available if upload or transcription fails. Job state survives process/app restart.

**CV-FR-013 — Recoverable processing jobs**  
The app shall track upload, transcription and summary as distinct states, including queued, uploading, processing, completed, failed, cancelled and retryable states as supported by the provider.

**Acceptance:** retry does not require re-recording and does not create duplicate billable jobs unintentionally. Stuck jobs are detectable and the user receives a useful message.

**CV-FR-014 — Transcript display and editing**  
The app shall display the generated transcript linked to its source audio. Timestamps shall be included when provided. Users shall be able to correct transcript text and speaker labels when the selected pipeline supports them.

**Acceptance:** AI-generated text is visibly labelled; uncertain speaker identity is not presented as verified identity; corrections persist locally and sync according to the selected data policy.

**CV-FR-015 — Searchable transcripts**  
The user shall search within a transcript and, in a later increment, across transcripts. Search results shall identify the source recording and navigate to the relevant segment/timestamp when available.

**Acceptance:** lexical search results are distinct from semantic/AI search. The interface shall not claim semantic search until implemented and evaluated.

**CV-FR-016 — AI summaries and action items**  
The app shall generate summaries from a persisted transcript or supported audio workflow. The output may include overview, topics, key points, decisions and action items. Each output shall be marked AI-generated and editable/deletable.

**Acceptance:** summaries remain linked to the source recording/transcript; uncertainty and missing context are communicated; hallucinated facts must not be framed as verified.

**CV-FR-017 — Conversation Q&A**  
The app may allow questions about one selected conversation after transcript persistence and retrieval accuracy are validated.

**Acceptance:** answers must be grounded in the selected recording/transcript, cite relevant transcript passages/timestamps where available, and clearly state when the recording does not contain an answer. This is P2, not an MVP dependency.

### 8.4 Privacy, billing and data controls

**CV-FR-018 — Permission and processing transparency**  
The user shall be able to inspect microphone, notification, network, background service, cloud processing, backup and deletion behaviour in context and in Settings.

**Acceptance:** no hidden recording, upload or sharing; logs do not contain raw audio, transcript contents, credentials or tokens.

**CV-FR-019 — User-controlled backup**  
Cloud backup shall be opt-in, separate from local recording and playback. Audio shall be encrypted client-side before upload according to a reviewed key design.

**Acceptance:** backup states, retry, restore, integrity checks and key-loss consequences are visible. Cloud is not required for core local use.

**CV-FR-020 — Entitlement and quota transparency**  
Before a purchase or subscription, the app shall clearly show price, billing cadence, amount due now, renewal amount/date, trial duration, cancellation behaviour, per-file limit, monthly quota, supported languages and feature restrictions that apply.

**Acceptance:** no hidden quota or renewal term; users can still access, play, export and delete their original audio irrespective of AI entitlement.

**CV-FR-021 — User deletion and retention**  
The user shall be able to delete original audio and derived transcript/summary data separately, and remove backed-up copies using a clear deletion workflow.

**Acceptance:** the app communicates local and server-side deletion states, retry/confirmation and any provider retention limitation. Account deletion shall be implemented before public cloud release.

### 8.5 Companion web

**CV-FR-022 — Web companion**  
A React/TypeScript companion dashboard may display and manage only data explicitly synchronized and authorized through the backend. It shall not claim to record cellular or third-party calls through the browser.

**Acceptance:** authenticated APIs enforce user ownership; local-only items are clearly distinguished from synchronized content; no database credentials or provider API secrets are shipped to the browser.

## 9. Non-functional requirements

- **Reliability:** protect against partial writes, interrupted recording, process death, playback resource leaks and failed background jobs.
- **Data integrity:** original audio is the durable asset; metadata and processing outputs must not corrupt or replace it.
- **Offline operation:** local library, local playback, rename/delete and metadata search work without a network connection.
- **Security:** TLS in transit; Android Keystore-backed key protection; vetted cryptographic libraries; client-side encryption for cloud audio; private object storage; secure token handling.
- **Privacy:** data minimization, explicit cloud processing, user-directed sharing, deletion and retention controls.
- **Performance:** library supports at least 1,000 metadata entries on representative mid-range hardware; long recordings must not block UI thread.
- **Accessibility:** TalkBack labels, adequate contrast, scalable text, clear language controls and comfortable touch targets.
- **Maintainability:** domain/data/UI boundaries, stable interfaces, migrations, automated tests, pinned dependencies and API documentation.
- **Battery:** no persistent polling; use platform-supported foreground service only for active user-visible recording and WorkManager for deferrable tasks.
- **Compatibility:** primary test device is OnePlus 10T 5G / Android 15; add Pixel and another OEM before broad claims.

## 10. Success metrics

Metrics must reflect actual quality, not feature presence:

- Successful recording finalization rate for permitted microphone capture.
- Percentage of completed audio items that reopen and play successfully.
- Interrupted-session recovery rate.
- Import success rate by supported format.
- Playback start success and crash-free sessions.
- Transcription completion, partial-output and failure rates by language/source quality.
- Search success/use and user corrections, where measurable with privacy-safe telemetry.
- Backup/restore integrity rate after cloud feature release.
- User understanding of recording source, cloud processing and billing terms.
- Zero unresolved critical security/privacy issues at release.

Do not use number of SIM/VoIP calls recorded as a success metric until a permitted method is verified.

## 11. Release gates

1. **Capture gate:** microphone recording can be stopped, finalized, reopened, played and recovered reliably.
2. **Library gate:** import, local persistence, playback, rename/delete and export work offline.
3. **AI gate:** transcription is opt-in, durable, recoverable and evaluated against representative audio; source audio remains available.
4. **Cloud gate:** encryption/key recovery, account ownership, deletion, cost limits and privacy terms are reviewed.
5. **Call-source gate:** every supported call source has repeatable device/OS/app evidence and a permitted implementation path.
6. **Play release gate:** current Google Play/Android policies, target SDK, permissions, foreground-service requirements, Data Safety and store claims are reviewed before submission.

## 12. Open decisions

- Confirm app name and trademark/domain availability; CallVault remains working name until cleared.
- Confirm minimum SDK and current target SDK at implementation/release time.
- Select speech-to-text and LLM providers after quality, language, privacy, retention and cost review.
- Decide whether transcription is on-device, cloud-based or both.
- Finalize local audio encryption at rest and key recovery model.
- Select backend auth/session implementation, object storage provider and database migration tool.
- Define exact local retention, backup retention and provider deletion SLAs.
- Determine whether any call-source integration is permitted and viable for Play distribution.
- Validate initial customer segment and willingness to pay; no interview/survey validation is currently evidenced.
