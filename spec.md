# CallVault — Product and Technical Specification

**Baseline:** 27 November 2026  
**Status:** Architecture baseline; target design, not a claim that all components exist.

## 1. Product structure
Hybrid product with three equally central pillars:
1. Capture — permitted recording sources and user-selected import.
2. Manage — local library, playback, organization, export and backup.
3. Understand — transcription, transcript search, summaries and Q&A.

## 2. Technology baseline
### Android
- Kotlin, Jetpack Compose, Material 3.
- MVVM with Clean Architecture boundaries.
- Room for local metadata.
- App-private storage for original audio.
- Android Storage Access Framework for user-mediated import.
- WorkManager for durable deferred processing where appropriate.
- Android Keystore for protecting encryption keys.

### Backend
- Java with Spring Boot 3.x modular monolith.
- PostgreSQL for cloud metadata.
- Object storage for encrypted backup objects.
- REST API; avoid premature microservices.

### Web companion
- React/TypeScript generated through Lovable.
- Authenticated backend APIs only.
- Access only content explicitly synchronized by Android.
- Browser cannot capture Android cellular or third-party app audio.

## 3. Architecture boundaries
Android owns microphone/device integrations, file picking, local storage, playback and capability tests. Backend owns authentication for cloud features, metadata synchronization, backup orchestration and AI job coordination. AI provider integration is behind an interface so providers can be changed without changing domain logic.

Local library/playback must not require backend availability. Original audio is the durable source asset. Transcript, summary, embeddings and AI outputs are derived records and must never replace the original.

### Implemented Home import boundary (27 November 2026)
- `HomeScreen` owns Android Storage Access Framework (SAF) picker interaction and Compose UI presentation.
- `HomeViewModel` owns `HomeUiState` (`recordings`, `isImporting`, `importResultMessage`) and Room Flow observation, invokes import off the main thread and rethrows `CancellationException`.
- `ImportRecordingUseCase` coordinates audio import, `RecordingRepository` persistence and `NonCancellable` rollback file cleanup without masking the original persistence exception.
- `RecordingRepository` remains the domain persistence boundary.
- `HomeViewModelFactory` currently performs manual dependency construction (no DI framework is used; playback, dedicated Library management, AI processing, backend, backup and SIM/VoIP call-audio capture remain unimplemented).

## 4. Core domain model
- **Recording:** id, title, sourceType, sourceLabel, createdAt, duration, mimeType, filePath, fileSize, status, checksum (optional), metadata.
- **RecordingSource:** IMPORTED, MICROPHONE, VERIFIED_CALL_SOURCE, DEMO (development only), plus LEGACY_UNVERIFIED in Android domain code for migrated pre-v2 `CALL_RECORDING` or fail-closed unrecognized persisted source values.
- **CaptureStatus:** IDLE (session-only, never persisted), RECORDING, PAUSED, INTERRUPTED, FINALIZING, SAVED, FAILED. Stop is terminal (`FINALIZING` cannot revert to `RECORDING`/`PAUSED`); `SAVED` and `FAILED` are terminal.
- **Transcript:** id, recordingId, language, provider, status, text/segments, createdAt, updatedAt.
- **TranscriptSegment:** start/end timestamps, text, speakerId (optional), confidence (optional).
- **ProcessingJob:** id, recordingId, jobType, status, progress, attempt, idempotencyKey, errorCode, timestamps.
- **Summary:** id, recordingId, transcriptId, generated sections, source references, model/provider, createdAt.
- **Bookmark:** id, recordingId, timestamp, label, note.
- **BackupRecord:** recordingId, object reference, encryption metadata, status, lastSyncedAt.

### Implemented Android domain & Room v2 boundary (28 November 2026, T-008)
- `Recording` uses `createdAt` mapped to Room's preserved `importedAt` SQLite column (no column rename or table rebuild) and carries optional in-memory `rawSourceType`/`rawStatus` fields so unrecognized future database values survive read/title-update round trips without overwriting SQLite or promoting unknown sources to `VERIFIED_CALL_SOURCE`.
- `RecordingEntity` v2 adds `sourceLabel`, `status` and `checksum` with `@ColumnInfo(defaultValue = ...)` aligned with `CallVaultDatabase.MIGRATION_1_2`, with schemas `1.json` and `2.json` exported via KSP (`exportSchema = true`).
- Unstructured `metadata` remains deferred until later tasks establish concrete fields; `T-009` (atomic temporary files) and `T-010` (full container/reopenability checks) remain unimplemented.

## 5. Audio lifecycle
1. User chooses an explicitly supported source or imports a file.
2. Explain source/permission and obtain required consent.
3. Start capture/import and show confirmed active state.
4. Persist safely to temporary/app-private storage.
5. On stop, finalize and validate existence, plausible size/duration and reopenability.
6. Persist metadata and mark SAVED only after finalization checks pass.
7. If interrupted, preserve partial file and mark INTERRUPTED/RECOVERABLE.
8. Never infer that both sides of a call were captured from metadata alone.

## 6. AI processing lifecycle
- User selects recording and sees what will be uploaded and processed.
- User explicitly confirms cloud processing.
- Create durable job with idempotency key.
- Track upload, transcription and summary as separate states.
- Preserve source audio if any stage fails.
- Retry failed stages without duplicate billing/jobs where provider supports idempotency.
- Store transcript with timestamps and provenance.
- Label AI-generated summaries and answers; link claims to transcript segments/timestamps where feasible.

## 7. Security and privacy
- Local-first default; cloud features opt-in.
- TLS for network traffic.
- Client-side encryption before backup upload.
- Key material protected by Android Keystore; define recovery before release.
- Explicit microphone/recording indicator and stop control.
- No automatic sharing or hidden upload.
- User can export and delete their own data.
- Define server retention, logs, deletion propagation and processor disclosure before enabling cloud AI.
- Do not store API secrets in Android or web client.

## 8. Observability and error handling
Use explicit error categories: permission denied, unsupported source, audio focus conflict, capture interrupted, finalization failed, corrupt/unsupported file, upload failed, transcription failed, provider quota exceeded, backup failed. User-facing messages must explain the next safe action. Avoid logging audio or transcript content.

## 9. Compatibility and policy
Maintain a matrix of device model, Android version, source app/source type, audio route, capture mechanism, test date, result, evidence and policy review. Supported status is scoped to the tested configuration. No Accessibility misuse, sideloaded helper dependency, hidden capture, root-only workaround or privileged-permission assumption.

## 10. Technical decisions still open
- Exact playback library/engine.
- Capture adapter and supported source combinations.
- Transcription provider, pricing, language support and retention.
- Object storage provider and backup cost.
- Encryption format, key recovery and multi-device model.
- Backend deployment and authentication provider.
- Web companion scope.
- Automated test/CI setup.
