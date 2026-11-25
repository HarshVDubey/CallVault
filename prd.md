# CallVault — Product Requirements Document

**Version:** 1.0  
**Status:** Baseline for guided implementation; requirements remain subject to feasibility and policy gates  
**Date:** 25 November 2026

## 1. Product vision

CallVault helps users manage audio conversations they are legally and technically able to record or import. It organizes audio, provides playback and search, creates user-requested transcripts and AI-generated summaries, and optionally backs up selected content securely.

CallVault is Android-first. The web companion is secondary and is not required for local import, library management or playback.

## 2. Problem statement

Audio conversations and recordings can be scattered across device folders and applications. Users may struggle to organize, retrieve, review and securely preserve them. CallVault aims to provide one transparent library and optional conversation-intelligence tools.

The product must be honest about Android limitations: detecting a call does not mean the app can access call audio. SIM and third-party VoIP recording are device-, OS-, source-app- and policy-dependent.

## 3. Target users

### Primary
- Android users with a legitimate supported recording source or audio files they can import.
- Users who need to revisit and search long conversations.
- Users who prefer local-first storage and control over cloud synchronization.

### Secondary
- Users who want transcripts, summaries, key points and action items.
- Users who access explicitly synchronized content through a companion web dashboard.

## 4. Product principles

1. Privacy and transparency by default.
2. Local-first: core library and playback work without an account or network.
3. No covert recording.
4. No Android, third-party app or Google Play restriction circumvention.
5. Clear disclosure and user confirmation before recording or cloud AI processing.
6. Data minimization and user-controlled retention/deletion.
7. Imported, demo, unsupported and verified recorded audio must never be conflated.
8. A UI control is not proof that its underlying capability is available.

## 5. Goals

- Provide a dependable local audio library.
- Assess and communicate recording compatibility by source and device.
- Provide playback and organization.
- Offer opt-in transcription and AI summaries.
- Offer optional encrypted backup and restore.
- Provide a companion web interface using the same documented backend APIs.
- Meet applicable Android and Google Play requirements before release.

## 6. Non-goals

- Guaranteeing every cellular call can be recorded.
- Capturing WhatsApp or other third-party VoIP internal audio without a permitted mechanism.
- Recording secretly or suppressing system/user indicators.
- Using Accessibility Services to capture audio or bypass restrictions.
- Rooting devices or requiring privileged/system permissions for ordinary users.
- Replacing the phone dialer or becoming the default dialer in the initial release.
- Building a full VoIP calling service in the initial release.
- Training models on user audio without separate explicit consent.

## 7. Scope by release

### MVP — local Android
- Onboarding and privacy explanation.
- Device/source recording capability assessment.
- Explicit distinction between recorded, imported, unsupported, unverified and demo content.
- Local audio library.
- Playback, pause, seek and duration.
- Rename, delete and user-mediated share/export.
- Search by title/date/source; tags may follow after core library.
- Local metadata database.
- Settings and storage usage.
- Empty, loading, error, offline and unsupported states.
- Audio import through Android Storage Access Framework.

### Release 1 — AI
- User-initiated transcription of selected audio.
- Timestamps where the chosen provider supports them.
- Language selection/detection where supported.
- AI summary, key points, decisions and action items.
- Transcript search.
- Processing status, retry and failure handling.
- Explicit disclosure and confirmation before cloud processing.
- Clear distinction between source transcript and generated interpretation.

### Release 2 — cloud
- Account registration/sign-in for cloud features.
- Optional encrypted backup.
- Backup status, retry, restore and conflict handling.
- Cross-device metadata synchronization.
- Lovable-built web companion.
- Account and data deletion workflows.

### Future / feasibility-dependent
- Additional recording adapters only for device/source combinations with a permitted, tested capture path.
- VoIP recording only inside a separately scoped CallVault-controlled calling feature, if approved and legally/policy reviewed.
- On-device speech recognition if quality, language coverage and device resources permit.

## 8. Functional requirements

### FR-01 — Capability assessment
The app shall present recording support separately for cellular SIM and third-party VoIP sources. Each status must be Supported (verified), Unsupported or Unverified, with the tested device/OS/source scope stated.

### FR-02 — Recording
The app may start capture only through an allowed audio source after required disclosure/consent. It must provide visible status and a user-accessible stop action. If no permitted source exists, it must not create a fake recording.

Manual and automatic recording are product preferences, not a guarantee of platform capability. Automatic behavior may be implemented only where the source, OS, legal requirements and Play policy permit it.

### FR-03 — Import
The user shall select audio using Android's Storage Access Framework. Imported audio must be labelled as Imported.

### FR-04 — Library
The library shall show title, date, duration, source type, item status and backup/processing status where relevant.

### FR-05 — Playback
The user shall play, pause, seek and view playback progress. Unsupported or corrupt files must produce recoverable errors.

### FR-06 — Organization and search
The user shall rename, delete, search, sort and filter library items. Search fields and indexing scope must be documented; transcript search is introduced with the AI release.

### FR-07 — Transcription
The user shall select an audio item and explicitly initiate transcription. The app shall show cloud-processing disclosure, progress and failure/cancellation state where supported.

### FR-08 — Summary
The app shall generate a summary from a supported transcript/audio-processing workflow. It shall label AI-generated content and allow correction/deletion.

### FR-09 — Backup
Backup shall be opt-in. Audio shall be encrypted client-side before upload. The app shall show backed-up items and support deletion/restore. Key recovery limitations must be explained before activation.

### FR-10 — Privacy controls
The user shall review permissions, AI/cloud settings, retention, export and deletion options.

### FR-11 — Web companion
The web companion shall use authenticated backend APIs and access only data explicitly synchronized by the Android app. It cannot record Android cellular or third-party app audio.

## 9. Non-functional requirements

- **Security:** TLS in transit; client-side encryption for audio backups; Android Keystore-backed key protection; secure token storage.
- **Privacy:** minimum metadata; no audio upload without explicit action and disclosure.
- **Reliability:** local library and playback operate offline.
- **Performance:** library scrolling remains responsive with at least 1,000 metadata entries on representative mid-range devices.
- **Accessibility:** content descriptions, contrast, scalable text and comfortable touch targets.
- **Maintainability:** repository abstraction, automated tests and documented APIs.
- **Battery:** no persistent polling; use platform scheduling for deferred work.
- **Compatibility:** test OnePlus 10T / Android 15 and a representative supported-device range.
- **Data integrity:** Room migrations; backup manifests; validate copied/imported media before marking complete.

## 10. User stories and acceptance criteria

### US-01 — View library
- New install shows an honest empty state.
- Items show title, date and duration.
- Imported items are visibly labelled.
- No fake sample recording appears in production.

### US-02 — Play audio
- Play/pause and seek work for supported files.
- Playback state is preserved across in-app navigation.
- Missing/corrupt files show a useful error.

### US-03 — Understand compatibility
- SIM and third-party VoIP are separate.
- Unverified is never displayed as supported.
- Unsupported sources explain the limitation.
- Call-state detection is never described as audio access.

### US-04 — Transcribe
- User sees disclosure before cloud processing.
- User confirms the selected item.
- Progress/failure state is visible.
- Transcript remains linked to its source item.

### US-05 — Back up
- Backup is off until configured.
- Upload uses TLS and client-side encryption.
- User sees backup status and can request deletion.

## 11. Home screen requirements — first UI milestone

The first designed screen is Home. It should provide:
- CallVault identity and greeting/profile action.
- Local library count, total audio duration and storage usage.
- Quick action for importing audio.
- Entry point to recording compatibility information.
- Recent recordings.
- A small AI-insights preview when real insights exist.
- Mini-player only when actual media is playing.
- Five primary destinations: Home, Library, Insights, Backup and Settings.

The initial implementation may use clearly identified development fixtures. Production must not show fabricated recordings, playback state, storage statistics or AI results.

## 12. Success metrics

- Crash-free sessions.
- Successful local playback rate.
- Import success rate.
- Verified recording compatibility coverage by device/source.
- Transcription completion/failure rates.
- Backup/restore success rate.
- User-reported privacy clarity.
- No unresolved critical security or policy issues at release.

Do not use “number of calls recorded” as a success metric until a permitted recording mechanism is verified and supported.

## 13. Open product decisions

- Trademark/domain clearance for CallVault.
- Whether account sign-in is required only for cloud features (recommended, not yet approved).
- Transcription provider, cost, languages, retention and processing region.
- Object-storage provider and operating cost.
- Backup key recovery and multi-device sharing.
- Retention defaults.
- Whether any device-specific recording adapter can be shipped in a Play-distributed build.
- Exact search scope in MVP versus AI release.
