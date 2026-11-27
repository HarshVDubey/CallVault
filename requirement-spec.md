# CallVault — Research-Informed Requirement Specification

**Baseline:** 27 November 2026  
**Product direction:** Android-first hybrid; Capture, Manage and Understand are equally central.  
**Status:** Product baseline proposed from competitor research; not proof of implementation.

## 1. Product vision
CallVault helps users capture audio through legitimate, technically supported sources, manage original recordings in a dependable local library, and understand conversations through transcripts and AI-generated outputs.

The three pillars are equally important to the product proposition:
- **Capture:** transparent, reliable recording/import with honest source compatibility.
- **Manage:** local-first library, playback, organization, ownership and export.
- **Understand:** transcription, search, summaries, highlights and conversation Q&A.

Delivery order may follow dependencies: trustworthy audio persistence and playback must precede AI features.

## 2. Research basis and evidence rules
This specification uses six user-provided competitor PDFs and the CallVault Competitor Intelligence Matrix: 45 evidence-register entries (43 individual review samples, one store-generated review summary and one policy observation), 20 feature observations and 16 proposed backlog requirements. Competitors covered: Cube ACR, Easy Voice Recorder, 1Transcribe, Notta and Otter.

A review is a reviewer-reported experience, not a reproduced defect. Store-generated summaries are not raw review counts. Historical complaints may describe fixed versions. A feature not visible in supplied screenshots is **not established**, not necessarily absent. Priority labels are CallVault product judgments, not statistically measured market frequencies.

## 3. Product principles
1. Recording integrity before feature breadth.
2. Original audio remains user-owned and accessible independent of AI entitlements.
3. Local library and playback work offline.
4. No covert capture, hidden upload, sideloaded helper APK, root-only method or Accessibility misuse.
5. Call-state detection is not audio access.
6. Clearly distinguish verified recording, imported audio, unsupported/unverified source and development fixtures.
7. Explicit user action and disclosure before cloud processing or backup.
8. Never report success until the underlying operation is verified.

## 4. Scope
### Core release
- Android onboarding and point-of-use privacy disclosures.
- Source/device compatibility information with evidence and status.
- User-mediated audio import.
- Local audio library, metadata, playback, rename, delete, search/sort and export.
- Recording lifecycle state, interruption handling and recoverable partial files for permitted capture sources.
- Clear save/finalization confirmation and file validation.

### Understanding release
- User-initiated transcription of selected audio.
- Durable processing jobs, progress, retry and failure states.
- Editable transcript and speaker labels where provider capability supports them.
- Search within/across transcripts with timestamp navigation.
- AI summaries, key points, decisions and action items linked to source transcript/audio.
- Conversation Q&A only after retrieval quality and cost are validated.

### Cloud release
- Optional account and synchronization for cloud features.
- Optional client-side encrypted backup and restore.
- Explicit upload, retention, deletion and key-recovery controls.
- Companion web client limited to explicitly synchronized content.

### Feasibility-dependent
Cellular SIM and third-party VoIP audio capture are not guaranteed. Ship only source/device/OS combinations with a permitted, repeatable, documented test path and current policy review.

## 5. Functional requirements
| ID | Requirement | Acceptance summary | Priority |
|---|---|---|---|
| CV-001 | Capture integrity | Finalized output exists, has plausible metadata and can be reopened; limitations of metadata checks are disclosed. | P0 |
| CV-002 | Recording state transparency | Source, active/paused/interrupted/finalizing/saved/failed state and elapsed time are accurate; stop is terminal. | P0 |
| CV-003 | Local audio ownership | Original audio remains playable, renameable, deletable and accessible independent of AI subscription. | P0 |
| CV-004 | Export/portability | User can export original audio and basic transcript in tested standard formats with destination/completion confirmation. | P0 |
| CV-005 | Permission/processing transparency | Explain permissions, source, background operation, upload destination, retention and deletion at point of use. | P0 |
| CV-006 | Audio lifecycle resilience | Detect audio-focus conflicts, app switching, screen-off and interruptions; preserve recoverable partial output. | P0 |
| CV-007 | Recoverable processing | Persist separate upload/transcription/summary states; retry without recapturing and prevent duplicate jobs. | P1 |
| CV-008 | Transcript editing | Correct transcript and speaker labels; show language and uncertainty where supported. | P1 |
| CV-009 | Transcript search | Search transcript/library and navigate to matching timestamps. | P1 |
| CV-010 | Simple capture UX | Primary record/import action is immediately discoverable; no ad blocks capture. | P1 |
| CV-011 | Entitlement clarity | Show per-file and monthly quotas, supported languages, amount due, renewal and cancellation terms before purchase. | P1 |
| CV-012 | User-controlled backup | Backup is optional; local recording/playback does not depend on cloud. | P1 |
| CV-013 | Bookmarks/highlights | Timestamped bookmarks with optional notes. | P1 |
| CV-014 | AI summaries | Generated overview, key points and actions are editable and linked to source evidence. | P2 |
| CV-015 | Conversation Q&A | Questions are scoped to a selected recording and grounded in its transcript. | P2 |
| CV-016 | Supported call integrations | Only permitted, repeatably tested source/device combinations; publish limitations. | Feasibility gate |

## 6. Non-functional requirements
- Reliability: no silent success; preserve original audio when AI fails.
- Privacy: no upload without explicit action and disclosure; minimize metadata.
- Security: TLS in transit; client-side encryption for backup; Android Keystore-backed key protection.
- Offline: local library and playback usable without account/network.
- Accessibility: scalable text, contrast, content descriptions and usable touch targets.
- Performance: responsive library with large item counts; background work must not block UI.
- Data integrity: safe file finalization, Room migrations, idempotent jobs and export validation.

## 7. Release gates
- P0 recording/import and persistence tests pass on emulator and target physical device.
- Playback and export verified against actual files.
- Source compatibility claims backed by test matrix.
- No unresolved critical privacy, security or policy issue.
- AI features do not delete/replace source audio and have explicit failure/retry states.
- No feature is marked complete from screenshots or UI presence alone.

## 8. Open decisions
Transcription provider and cost; supported languages; retention defaults; backup storage/cost; encryption key recovery; account requirements; exact recording adapters; trademark/domain clearance; pricing and AI quota model.
