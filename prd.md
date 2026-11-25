# Product Requirements Document (PRD)

**Product:** CallVault  
**Version:** 0.1  
**Status:** Draft for implementation planning  
**Date:** 25 November 2026

## 1. Product vision

CallVault helps users manage audio conversations they are legally and technically able to record or import. It organizes audio, provides playback, creates searchable transcripts and summaries, and optionally backs up user-selected content securely.

CallVault is Android-first. The web companion is secondary and must not be required for local recording or playback.

## 2. Problem statement

Users may have audio recordings scattered across device folders or other applications, making them difficult to organize, search, revisit and summarize. CallVault aims to provide one transparent library and optional AI assistance.

The product must be honest about Android limitations: detecting a call does not mean the app can access the call's audio. Recording of SIM calls and third-party VoIP calls is device-, OS-, app- and policy-dependent.

## 3. Target users

### Primary
- Android users who have a legitimate, supported way to record conversations or who import recordings.
- Users who need to retrieve information from long conversations.
- Users who want local-first storage and control over cloud synchronization.

### Secondary
- Users who want transcripts, summaries, action items and searchable conversation history.
- Users who access their synchronized library through a companion web dashboard.

## 4. Product principles

1. Privacy and transparency by default.
2. Local-first: core library and playback work without an account or network.
3. No covert recording.
4. No bypassing Android, third-party app or Google Play restrictions.
5. User consent and clear disclosure before recording or sending audio to a cloud AI service.
6. Data minimization and user-controlled retention/deletion.
7. Never represent demo, imported or unsupported data as a live CallVault recording.

## 5. Goals

- Provide a dependable local audio library.
- Verify and expose recording compatibility by device and source.
- Provide reliable playback and organization.
- Offer opt-in transcription and summaries.
- Offer optional encrypted backup and restore.
- Provide a companion web interface using the same documented backend APIs.
- Meet applicable Google Play policy and Android platform requirements before release.

## 6. Non-goals

- Guaranteeing recording of every cellular call.
- Capturing WhatsApp or other third-party VoIP internal audio without a permitted mechanism.
- Recording secretly or suppressing system/user indicators.
- Using Accessibility Services to capture audio or bypass restrictions.
- Rooting devices or requiring privileged/system permissions for ordinary users.
- Replacing the phone's dialer or becoming the default dialer in the initial release.
- Building a full VoIP calling service in the initial release.
- Training models on user audio without separate, explicit consent.

## 7. Scope by release

### MVP (local Android)
- App onboarding and privacy explanation.
- Device/app recording capability assessment.
- Recording/import status clearly differentiated.
- Local recordings library.
- Audio playback, pause, seek and duration.
- Rename, delete, share/export using Android-supported mechanisms.
- Search by title/date/source and user tags.
- Local metadata database.
- Basic app settings and storage usage.
- Clear empty, loading, error and unsupported states.

### Release 1 (AI)
- User-initiated transcription of selected audio.
- Transcript with timestamps where provider supports them.
- Language selection/detection where supported.
- AI summary, key points and action items.
- Transcript search.
- Processing status, retry and failure handling.
- Explicit disclosure and confirmation before cloud processing.

### Release 2 (cloud)
- Account registration/sign-in.
- Optional encrypted backup.
- Backup status, retry, restore and conflict handling.
- Cross-device metadata synchronization.
- Web companion dashboard.
- Account and data deletion workflows.

### Future / subject to feasibility
- Additional recording adapters for devices with permitted recording support.
- VoIP recording only within a CallVault-controlled calling feature, if a separate product decision is made and all participants are informed.
- On-device speech recognition where quality, language coverage and device resources permit.

## 8. Functional requirements

### FR-01: Capability assessment
The app shall present recording support as a capability matrix by source and device, not as a blanket promise. It shall state when a source is unsupported or unverified.

### FR-02: Recording
The app may start a recording only through an allowed Android audio source and after required disclosure/consent. It shall provide visible status and a user-accessible stop action. If no permitted source exists, it shall not fake a recording.

### FR-03: Import
The user shall be able to select audio files through Android's Storage Access Framework. Imported audio shall be labelled as imported.

### FR-04: Library
The user shall see recordings/imports with title, date, duration, source type, processing state and backup state where relevant.

### FR-05: Playback
The user shall be able to play, pause, seek and view playback progress. Unsupported/corrupt files shall produce a recoverable error.

### FR-06: Organization
The user shall be able to rename, delete, search, sort and filter library items. Tags may be added in a later iteration.

### FR-07: Transcription
The user shall explicitly select an audio item and initiate transcription. The app shall display upload/processing status and handle cancellation/failure where supported.

### FR-08: Summary
The app shall generate a summary from a transcript or supported audio-processing workflow. It shall label AI-generated content and allow users to correct or delete it.

### FR-09: Backup
Backup shall be opt-in. Audio shall be encrypted client-side before upload. The app shall show what is backed up and support deletion/restore.

### FR-10: Privacy controls
The user shall be able to review permissions, AI/cloud settings, retention settings and account/data deletion options.

### FR-11: Web companion
The web companion shall use authenticated backend APIs and shall only access data explicitly synchronized by the Android app.

## 9. Non-functional requirements

- **Security:** TLS in transit; client-side encryption for audio backups; Android Keystore-backed key protection; secure token storage.
- **Privacy:** collect minimum metadata; no audio upload without explicit user action and disclosure.
- **Reliability:** local library and playback should work offline.
- **Performance:** library scrolling should remain responsive for at least 1,000 metadata entries on representative mid-range devices.
- **Accessibility:** content descriptions, adequate contrast, scalable text and touch targets.
- **Maintainability:** modular code, repository abstraction, automated tests and documented APIs.
- **Battery:** no persistent background polling; use platform scheduling for deferred work.
- **Compatibility:** test on the OnePlus 10T / Android 15 and a representative range of supported Android versions.
- **Data integrity:** avoid losing metadata during app upgrades; use Room migrations and backup manifests.

## 10. User stories and acceptance criteria

### US-01: View library
As a user, I can open the app and see my saved audio items.
- Empty state appears for a new install.
- Items show title, date and duration.
- Imported items are visibly labelled.
- No fake sample recording appears in production.

### US-02: Play audio
As a user, I can listen to a saved item.
- Play/pause works.
- Seek works for supported formats.
- Playback state survives navigation within the app.
- Missing/corrupt files show a useful error.

### US-03: Understand compatibility
As a user, I can see whether a call source is supported.
- SIM and third-party VoIP are reported separately.
- Unverified is not shown as supported.
- Unsupported sources explain the limitation.
- The app does not imply call-state detection grants audio access.

### US-04: Transcribe audio
As a user, I can request a transcript.
- A clear disclosure appears before cloud processing.
- The user confirms the specific audio item.
- Progress and failure states are visible.
- Transcript is linked to the source recording.

### US-05: Back up audio
As a user, I can opt into encrypted backup.
- Backup is off until configured.
- Upload uses TLS.
- Audio is encrypted client-side before upload.
- User can see backup status and delete backed-up content.

## 11. Success metrics

- Crash-free sessions.
- Successful local playback rate.
- Import success rate.
- Verified recording compatibility coverage by device/source.
- Transcription completion and failure rates.
- Backup/restore success rate.
- User-reported privacy clarity.
- No unresolved critical security or policy issues at release.

Do not use “number of calls recorded” as a success metric until a permitted recording mechanism is verified and supported.

## 12. Open product decisions

- Final app name and trademark clearance.
- Whether sign-in is optional or required for cloud features only.
- Initial transcription provider and supported languages.
- Initial cloud storage provider and operating cost model.
- Retention defaults and user-configurable limits.
- Whether any supported device-specific recording adapter can be included in a Play-distributed build.
