# CallVault — Technical Requirements Document

**Version:** 1.1  
**Status:** Engineering baseline; observed values separated from target choices  
**Date:** 26 November 2026

## 1. Platform baseline

### Android — observed generated project

| Setting | Observed value |
|---|---|
| Gradle wrapper | 9.6.0 |
| Android Gradle Plugin | 9.5.3 |
| Kotlin | 2.2.10 |
| Compose BOM | 2026.02.01 |
| compileSdk | 37 |
| targetSdk | 37 |
| minSdk | 26 |
| Namespace/applicationId | dev.callvault.app |
| Java source/target compatibility | 11 |
| Gradle modules | `:app` only |
| UI | Jetpack Compose, Material 3 |
| Repositories | Google, Maven Central, Gradle Plugin Portal |

The user reported successful Gradle builds and showed the app running on a Medium Phone API 37.0 emulator. This is a local result, not independent CI verification.

### Android product baseline
- Language: Kotlin.
- UI: Jetpack Compose and Material 3.
- Build: Gradle Kotlin DSL and Version Catalog.
- Primary physical test device: OnePlus 10T 5G, Android 15 (not yet tested).
- Emulator: Medium Phone API 37.0.
- Local database: Room (recording metadata foundation implemented).
- Background work: WorkManager planned.
- Playback: AndroidX Media3 or platform media APIs; selection pending.
- Secure key storage: Android Keystore.

### Room/KSP build note
Room runtime, KTX and compiler dependencies and the KSP plugin were added through the version catalog. With AGP 9.5.3 built-in Kotlin, the user encountered a KSP Kotlin source-set registration error. The user added `android.disallowKotlinSourceSets=false` to `gradle.properties`, after which Gradle builds succeeded. This is a working compatibility workaround that must be revisited against official compatibility guidance before build-tool upgrades; it is not a permanent architecture decision.

### Backend target
- Language: Java.
- Framework: Spring Boot modular monolith; exact stable version to be pinned at backend implementation.
- Build: choose and standardize Gradle or Maven before backend scaffold.
- API: REST + OpenAPI.
- Database: PostgreSQL.
- Schema migrations: choose Flyway or Liquibase before first migration.
- Authentication: OIDC/OAuth-compatible or secure backend-managed sessions; unresolved.
- Object storage: private S3-compatible bucket or equivalent.
- Observability: structured logs, metrics and sensitive-data redaction.

### Web target
- React + TypeScript.
- Lovable-generated initial client.
- Accessible component system and shared semantic design tokens.
- Calls backend APIs; no database credentials in browser.

## 2. Android project organization

Current project is a single Gradle module `:app`. Package boundaries:
- `core/designsystem`
- `data/local/dao`
- `data/local/database`
- `data/local/entity`
- `data/mapper`
- `data/repository`
- `domain/model`
- `domain/repository`
- `domain/usecase`
- `feature/home`
- `feature/recordings`
- `feature/player`
- `feature/insights`
- `feature/backup`
- `feature/settings`

A generated `ui.theme` package remains. Consolidate/remove generated theme files only after inspecting their source and updating imports; do not delete blindly. Do not create multiple Gradle modules in the first milestone.

### Implemented recording data path
- `Recording` domain model and `RecordingSource` enum (`IMPORTED`, future `CALL_RECORDING`).
- `RecordingEntity` Room entity and `RecordingDao`.
- `CallVaultDatabase` Room database, currently version 1.
- Entity/domain mappers.
- `RecordingRepository` interface and `RecordingRepositoryImpl`.
- No transcript, summary, tag or processing-job entities are confirmed implemented.

### Presentation architecture note
The first import integration currently creates the repository/database access inside `HomeScreen` as temporary manual wiring. Move this into a ViewModel/presentation layer before adding further business behavior; the target remains MVVM/Clean Architecture boundaries.

## 3. Android permissions and recording constraints

Request only permissions required by implemented features. Do not request broad permissions pre-emptively.

Potential permissions to evaluate:
- `RECORD_AUDIO` only for a legitimate microphone recording feature.
- `READ_PHONE_STATE` only if call-state metadata is genuinely required and permitted by current Android/Play rules.
- Foreground-service permissions/types only if a legitimate user-visible workflow requires them and meets current policy.
- Notifications permission where required for visible recording/processing notifications.
- Network permission for cloud features.
- No `MANAGE_EXTERNAL_STORAGE` for ordinary audio import; use Storage Access Framework.
- No Accessibility Service for audio capture or restriction circumvention.
- No assumption that privileged `CAPTURE_AUDIO_OUTPUT` is available.
- Do not assume ordinary apps can use `VOICE_CALL`, `VOICE_UPLINK` or `VOICE_DOWNLINK`.

Third-party VoIP apps may restrict playback capture. Do not promise WhatsApp audio capture. A source is supported only after a permitted mechanism is demonstrated and policy reviewed.

## 4. Audio requirements and current import behavior

Current import implementation:
- Home launches Android Storage Access Framework using `ActivityResultContracts.OpenDocument` with `audio/*`.
- No broad storage permission was added to the manifest for this picker flow.
- `AudioImportManager` currently accepts filename extensions MP3, M4A and WAV.
- Selected content is copied to app-private `filesDir/recordings`.
- Duration is read using `MediaMetadataRetriever`; file size, MIME type, display name and import timestamp are recorded.
- Imported metadata is inserted through the Room repository.
- Import/copy/metadata and database work is dispatched to `Dispatchers.IO`.
- If repository insertion fails after copy, the UI attempts to delete the copied file.
- A user screenshot demonstrated one successful MP3 import (`sample-3s`, 3 seconds, 51 KB) on API 37 emulator.

Known gaps:
- Extension validation is not equivalent to validating file content; add content/format validation.
- Current accepted extension set is intentionally narrower than the candidate list below.
- Size limits, large-file behavior, interrupted copy and corrupt-file tests are not complete.
- User confirmed the imported item remained after force-stop/relaunch on the API 37 emulator; this is a user-reported local verification, not CI or physical-device evidence.
- Playback is not implemented.
- Local audio encryption at rest is not implemented.

Candidate future formats: M4A/AAC, MP3, WAV, OGG/Opus; confirm actual decoder support before advertising.

General requirements:
- Keep metadata separate from file bytes.
- Handle partial writes and interrupted imports/recordings.
- Verify copied file before marking complete.
- Do not delete source until an import copy is verified.
- Playback releases resources on lifecycle changes.
- Unsupported/corrupt formats show actionable errors.

## 5. Local database requirements

Implemented initial Room table:
- `recordings`

Future tables under consideration:
- `transcripts`
- `summaries`
- `tags`
- `recording_tags`
- `processing_jobs`
- `app_settings` only if DataStore/preferences are insufficient.

Use stable UUID identifiers. Store timestamps consistently (UTC epoch milliseconds or ISO-8601). Store app-private relative file identifiers, not arbitrary external absolute paths. Room database is currently version 1; migrations and migration tests are not yet established.

## 6. Security requirements

- Use Android Keystore for device-bound key protection.
- Use vetted authenticated encryption such as AES-GCM when implemented.
- Unique nonce/IV per encryption operation; never reuse nonce with same key.
- Keep keys separate from encrypted audio.
- TLS for network traffic.
- Short-lived access tokens and secure refresh-token handling.
- No API keys in APK or web bundle.
- Client-side encrypt cloud audio before upload.
- Private object storage.
- Redact logs/crash reports.
- Delete audio, transcripts, summaries and cloud objects through documented lifecycle.
- Define key recovery before promising multi-device restore.

Use vetted platform/library APIs. Do not invent a custom cipher or key-derivation scheme.

## 7. Backend requirements

- REST endpoints versioned under `/api/v1`.
- Request validation and consistent error format.
- Authenticated user ownership checks on every resource.
- Rate limits for AI and upload operations.
- Upload size/type limits.
- Short-lived authorized object upload/download.
- Idempotency for job creation and backup operations.
- Background processing for transcription/summaries.
- Database migrations in source control.
- OpenAPI.
- Audit events for sensitive actions without content logging.

## 8. AI requirements

- Select provider after cost, language, retention, security and processing-region review.
- AI processing is user-initiated and opt-in.
- Show selected recording and destination before upload.
- Store provider/job IDs, status and error codes; never secrets.
- Summary structure: overview, key points, decisions, action items, uncertainty/limitations.
- Do not fabricate speaker identity. Diarization labels are estimates when supported.
- Allow user correction/deletion.
- Define provider retention/deletion behavior.

## 9. Backup and synchronization

- Client-side encrypt audio before upload.
- Upload ciphertext only.
- Content hash and object size for integrity.
- Resumable/multipart uploads if needed.
- Bounded exponential retry for transient failures.
- States: not configured, queued, uploading, backed up, failed, restore queued, restored.
- Handle duplicate objects and conflicts.
- Explain lost-key consequences.

## 10. Testing strategy

### Unit
- Domain use cases.
- Mappers and validation.
- ViewModels/state reducers.
- Encryption wrapper behavior using vetted APIs and test vectors.

### Android instrumentation
- Room DAO and migration tests.
- Storage Access Framework import.
- Playback lifecycle.
- File deletion/recovery.
- Permission denial.
- Device-specific capability assessment.

### Device matrix
- OnePlus 10T / Android 15 (primary physical test target; not yet tested).
- API 37 emulator (Home and one audio import demonstrated).
- At least one current Pixel and another OEM before broad release.
- Document every Android version and source combination separately.

## 11. Technical readiness

- Reproducible clean build.
- Dependencies pinned and version compatibility verified.
- Tests pass in CI.
- No secrets in source control.
- Permission rationale documented.
- Recording capabilities verified, not assumed.
- Threat model/privacy review before cloud release.
