# CallVault — System Architecture

**Version:** 1.1  
**Architecture style:** Android-first hybrid, local-first client, modular backend monolith  
**Status:** Target architecture with initial Android data/import path implemented

## 1. Architecture goals

- Keep native audio/device functionality inside Android.
- Keep core library and playback usable offline.
- Separate UI, business rules and persistence.
- Keep backend scope small until cloud needs are established.
- Make web and Android clients consume the same backend contracts.
- Avoid premature microservices.
- Design privacy and encryption into data flows.
- Never imply that a browser or call-state API grants access to call audio.

## 2. High-level system

```mermaid
flowchart TB
  subgraph Android["CallVault Android — Kotlin"]
    UI["Compose UI"]
    VM["ViewModels — target"]
    DOMAIN["Domain use cases — planned"]
    REPO["Repository interface / implementation"]
    ROOM["Room database — recordings foundation"]
    FILES["App-private audio storage — import implemented"]
    CAP["Capability assessment / permitted audio adapters"]
    PLAYER["Playback engine — planned"]
    WORK["WorkManager jobs — planned"]
    UI --> REPO
    REPO --> ROOM
    UI --> FILES
    DOMAIN --> CAP
    DOMAIN --> PLAYER
    WORK --> DOMAIN
  end

  subgraph Cloud["CallVault Backend — Spring Boot modular monolith"]
    API["REST API"]
    AUTH["Identity and access"]
    META["Recording metadata"]
    AI["AI processing"]
    BACKUP["Backup orchestration"]
    AUDIT["Security/audit"]
    API --> AUTH
    API --> META
    API --> AI
    API --> BACKUP
    API --> AUDIT
  end

  subgraph Storage["Cloud infrastructure"]
    PG[("PostgreSQL")]
    OBJ[("Private object storage")]
    STT["Speech-to-text provider"]
    LLM["LLM provider"]
  end

  Android -->|"TLS; user-authorized sync — future"| API
  API --> PG
  BACKUP --> OBJ
  AI --> STT
  AI --> LLM

  subgraph Web["Companion Web — Lovable / React"]
    WEBUI["Web dashboard"]
  end
  WEBUI -->|"Authenticated API"| API
```

## 3. Android application

### Presentation
- Jetpack Compose and Material 3.
- Navigation Compose is the planned navigation library; verify dependency/version before adding.
- MVVM with immutable UI state is the target.
- UI should send events to ViewModels and must not directly own Room/file access.
- Current Home import is an initial integration shortcut: repository/database objects are created in the composable. Refactor this into a ViewModel/presentation layer before expanding the feature.

### Domain
- `Recording` domain model and `RecordingSource` enum implemented.
- `RecordingRepository` interface implemented.
- Dedicated use cases for importing, listing/searching, playback, deleting, transcription/summaries and backup remain future work.
- Domain models remain independent of Room entities and API DTOs.

### Data
Implemented recording metadata path:
- `RecordingEntity` Room entity.
- `RecordingDao` with insert, update, delete, observe list/count and lookup operations.
- `CallVaultDatabase` version 1.
- Entity/domain mappers.
- `RecordingRepositoryImpl`.
- `AudioImportManager` for SAF-selected MP3/M4A/WAV files.
- Imported audio is copied to app-private `filesDir/recordings`; relative path and metadata are saved in Room.
- No transcript/summary entities or cloud API data sources are implemented.

### Current Android code organization

The project uses one Gradle module, `:app`, and package boundaries beneath `dev.callvault.app`:
- `core.designsystem.components`
- `data.local.dao`
- `data.local.database`
- `data.local.entity`
- `data.mapper`
- `data.repository`
- `domain.model`
- `domain.repository`
- `domain.usecase`
- `feature.home`
- `feature.recordings`
- `feature.player`
- `feature.insights`
- `feature.backup`
- `feature.settings`
- Generated `ui.theme` remains.
- `MainActivity.kt`.

Package folders do not prove all listed features are implemented. Do not create Gradle modules unless a later decision justifies them.

### Audio subsystem

Keep these concepts separate:
1. Call-state observation (metadata only).
2. Recording capability assessment.
3. Actual audio capture through an allowed mechanism.
4. Imported audio.
5. Playback.

A call being active does not grant access to internal call audio. Only a tested, permitted source adapter may create a CallVault-recorded item. Current Room model includes `CALL_RECORDING` as a future source category; no call-audio capture implementation is verified.

### Background work
WorkManager may handle eligible deferred backup/transcription jobs. It is not a mechanism for continuous covert recording or bypassing foreground-service/background restrictions.

## 4. Backend

Start as a modular Spring Boot application with internal modules/packages:
- identity
- recordings
- transcription
- summaries
- backup
- audit
- shared configuration/security

Use REST APIs and OpenAPI. Use PostgreSQL for structured metadata. Use private object storage for encrypted audio objects. Do not store audio blobs in PostgreSQL.

Split into services only when independent scaling, deployment, ownership or reliability requirements justify operational complexity.

## 5. Web companion

Lovable generates a React/TypeScript web client. It is not the Android application and cannot access Android call audio. It may:
- Show synchronized recordings and metadata.
- Play audio only when the user has synchronized and authorized access.
- Show transcripts and summaries.
- Manage backup and account settings.

It must not create fake recording controls implying the browser can record cellular or third-party VoIP calls.

## 6. Data flow — local import (implemented first path)

1. User taps Import Audio on Home.
2. Android Storage Access Framework opens via `ActivityResultContracts.OpenDocument` with `audio/*`.
3. User selects a file; no broad storage permission is requested for this flow.
4. `AudioImportManager` reads the display name and currently accepts MP3, M4A and WAV filename extensions.
5. Selected content is copied to app-private `filesDir/recordings`.
6. `MediaMetadataRetriever` extracts duration; MIME type, size, title and timestamp are captured.
7. A domain Recording with source `IMPORTED` and relative private path is created.
8. `RecordingRepositoryImpl` inserts the metadata through Room.
9. Home observes the Room Flow and updates count, total duration and recent items.
10. If database insertion fails, Home attempts to remove the copied file.

User demonstrated a successful 3-second, 51 KB MP3 import on the API 37 emulator. Force-stop/relaunch persistence, content validation, large-file and corruption cases are not yet verified. The dedicated Library and playback are not implemented.

## 7. Data flow — permitted recording

1. User views capability and privacy explanation.
2. App verifies source and capture mechanism are supported.
3. User starts through visible control or permitted disclosed automatic rule.
4. App displays recording state and stop control.
5. Audio is written to app-private storage.
6. On stop, file is finalized and validated.
7. Metadata is persisted.
8. Item appears in library.

If no permitted source exists, fail closed and show an explanatory unsupported/unverified state.

## 8. Data flow — AI transcription

1. User selects a recording.
2. App explains destination and processing/data handling.
3. User explicitly confirms.
4. App creates a processing job.
5. Audio is transmitted over TLS using authorized, time-limited upload.
6. Backend invokes selected speech-to-text provider.
7. Transcript/timestamps are persisted according to approved privacy design.
8. App fetches and displays result.
9. User may delete transcript and request provider-side deletion where supported.

## 9. Data flow — encrypted backup

1. User enables backup and completes key setup.
2. Android encrypts audio locally using a per-file data encryption key.
3. The data key is wrapped using an approved user/device-controlled key-encryption design.
4. Only ciphertext is uploaded to private object storage.
5. Backend stores object metadata and integrity hashes.
6. Restore downloads ciphertext, verifies integrity and decrypts locally.
7. If key recovery is unavailable, explain that backups may be unrecoverable.

Key derivation, recovery and multi-device sharing require a separate security design review before implementation.

## 10. Security boundaries

- Android Keystore protects cryptographic key material where supported.
- Audio files remain app-private by default; current imported audio is not encrypted at rest.
- Backend credentials and AI provider secrets never ship in mobile/web clients.
- All network communication uses TLS.
- Access tokens use secure platform storage.
- Object storage is private; use short-lived authorized access.
- Audit logs avoid transcript/audio contents.
- Logs redact tokens, secrets and sensitive content.

## 11. Offline behavior

Offline now:
- Import audio through system picker when source file is locally available.
- Store imported audio in app-private storage.
- Observe locally persisted recording metadata on Home.

Planned offline behavior:
- Browse full local library.
- Play local files.
- Rename/delete local metadata and files.
- Search local metadata and locally stored transcripts.

Online (future):
- Request cloud transcription/summaries.
- Upload/download encrypted backup.
- Synchronize account metadata.

## 12. Architecture decisions

| Decision | Choice | Status |
|---|---|---|
| Native client | Kotlin + Compose | Confirmed |
| Android organization | Single :app module; package boundaries | Confirmed current baseline |
| UI architecture | MVVM + Clean Architecture boundaries | Target; Home import needs ViewModel refactor |
| Local DB | Room | Recording metadata foundation implemented |
| Local audio | App-private filesDir storage | Import path implemented; encryption not implemented |
| Backend | Spring Boot modular monolith | Confirmed direction |
| Cloud DB | PostgreSQL | Confirmed direction |
| Audio storage | Private object storage | Confirmed direction |
| Web | Lovable-generated React client | Confirmed direction |
| Sync | Explicit user-controlled sync | Confirmed direction |

## 13. Architecture risks/open decisions

- Permitted call-audio capture for each target source/device.
- Exact foreground-service and permission requirements if any recording adapter qualifies.
- Room migration strategy and test coverage.
- Media3/platform playback dependency and lifecycle design.
- Refactor Home data access into ViewModel.
- Import content validation, size limits and recovery from partial writes.
- Backup key recovery and multi-device model.
- AI provider and data retention.
- Authentication model and cloud authorization.
