# CallVault — Technical Requirements Document (TRD)

**Version:** 0.1  
**Status:** Initial engineering baseline

## 1. Platform baseline

### Android
- Language: Kotlin.
- UI: Jetpack Compose and Material 3.
- IDE: Android Studio stable release.
- Build: Gradle Kotlin DSL.
- Compile/target SDK: select current Google Play-required target at release; initial project may use the installed stable SDK. For the November 2026 planning baseline, target API 36 if required by the applicable Play policy.
- Minimum SDK: API 26 is the proposed baseline; validate library support and market coverage before release.
- Test device: OnePlus 10T 5G running Android 15.
- Local database: Room.
- Background jobs: WorkManager.
- Playback: AndroidX Media3 or platform media APIs, selected based on supported formats and lifecycle requirements.
- Secure key storage: Android Keystore.

### Backend
- Language: Java.
- Framework: Spring Boot 3.x baseline, with exact stable patch version pinned at implementation start.
- Build: Gradle or Maven; choose one and standardize.
- API: REST + OpenAPI.
- Database: PostgreSQL.
- Schema migrations: Flyway or Liquibase (choose one).
- Authentication: OIDC/OAuth-compatible approach or secure backend-managed sessions; finalize before implementation.
- Object storage: private S3-compatible bucket or equivalent.
- Observability: structured logs, metrics and error reporting with sensitive data redaction.

### Web
- React + TypeScript.
- Tailwind CSS and accessible component system.
- Generated initially through Lovable.
- Calls backend APIs; no direct database credentials in browser.

## 2. Android module requirements

Proposed initial Gradle modules:
- `:app` — application entry point and dependency wiring.
- `:core:common` — shared result/error types and utilities.
- `:core:designsystem` — theme and reusable UI components.
- `:core:navigation` — navigation definitions.
- `:data` — Room, repositories and data sources.
- `:domain` — domain models and use cases.
- `:feature:home`
- `:feature:recordings`
- `:feature:player`
- `:feature:insights`
- `:feature:backup`
- `:feature:settings`

A single Gradle module with package boundaries is acceptable for the first prototype. Split into modules when build complexity or team size justifies it.

## 3. Android permissions and recording constraints

Request only permissions required by implemented features. Do not request broad permissions “just in case”.

Potential permissions to evaluate:
- `RECORD_AUDIO` only for a legitimate microphone recording feature.
- `READ_PHONE_STATE` only if call-state metadata is genuinely required and permitted under current Android/Play rules.
- Foreground service permissions/types only if a legitimate user-visible recording workflow requires them and the use case meets current policy.
- Notifications permission where required for user-visible recording/processing notifications.
- Network permission for cloud features.
- No `MANAGE_EXTERNAL_STORAGE` for ordinary audio import; use Storage Access Framework.
- No Accessibility Service for audio capture or policy circumvention.
- No privileged `CAPTURE_AUDIO_OUTPUT` or restricted call-audio source assumptions.

`VOICE_CALL`, `VOICE_UPLINK` and `VOICE_DOWNLINK` are restricted audio sources for ordinary third-party applications. Do not design the app around access to them.

Third-party VoIP apps may restrict playback capture. Do not promise WhatsApp audio capture. Only support a source after a permitted mechanism is demonstrated and policy reviewed.

## 4. Audio requirements

- Supported import formats should be selected based on Android decoder support; initial candidates: M4A/AAC, MP3, WAV, OGG/Opus.
- Validate file type by content where practical, not extension alone.
- Store audio in app-private storage.
- Keep metadata separate from file bytes.
- Handle partial writes and interrupted recordings/imports.
- Verify duration, size and readability before marking an item complete.
- Do not delete the source until an import copy is verified.
- Playback must release resources on lifecycle changes.
- Provide clear errors for unsupported/corrupt formats.

## 5. Local database requirements

Initial tables:
- `recordings`
- `transcripts`
- `summaries`
- `tags`
- `recording_tags`
- `processing_jobs`
- `app_settings` (only where Room preferences are not sufficient)

Use UUIDs for stable identifiers. Store timestamps in UTC epoch milliseconds or ISO-8601 consistently. Store file references as app-private relative paths/identifiers, not arbitrary external absolute paths. Use Room migrations and test upgrades.

## 6. Security requirements

- Use Android Keystore for device-bound key protection.
- Use authenticated encryption such as AES-GCM for local audio encryption where implemented.
- Use a unique nonce/IV per encryption operation and never reuse a nonce with the same key.
- Keep keys separate from encrypted audio files.
- TLS for all network traffic.
- Use short-lived access tokens and secure refresh-token handling.
- Do not embed API keys in the APK or web bundle.
- Encrypt cloud audio client-side before upload.
- Ensure object storage is private.
- Redact logs and crash reports.
- Provide deletion for audio, transcripts, summaries and cloud objects.
- Define key backup/recovery before promising multi-device restore.

Cryptographic implementation must use vetted platform/library APIs; do not invent a custom cipher or key-derivation scheme.

## 7. Backend requirements

- REST endpoints versioned under `/api/v1`.
- Request validation and consistent error response format.
- Authenticated user ownership checks on every resource.
- Rate limiting for expensive AI and upload operations.
- Upload size/type limits.
- Signed, short-lived object upload/download authorization where appropriate.
- Idempotency for job creation and backup operations.
- Background processing for transcription and summaries.
- Database migrations checked into source control.
- API documentation via OpenAPI.
- Audit events for sensitive actions, without logging content.

## 8. AI requirements

- Provider selected after cost, language, retention, security and regional processing review.
- AI processing is user-initiated and opt-in.
- Show the specific recording and destination before upload.
- Store provider/job identifiers, status and error codes; avoid storing secrets.
- Label output as AI-generated.
- Summary format: overview, key points, decisions, action items, uncertainty/limitations.
- Do not fabricate speaker identity. Speaker labels, if supported, must be presented as diarization estimates.
- Allow user correction and deletion.
- Define provider retention/deletion behavior.

## 9. Backup and synchronization

- Client-side encrypt audio before upload.
- Upload ciphertext only.
- Use content hash and object size for integrity checks.
- Support resumable/multipart uploads if needed.
- Retry transient failures with bounded exponential backoff.
- Expose backup states: not configured, queued, uploading, backed up, failed, restore queued, restored.
- Handle duplicate objects and conflict resolution.
- Document consequences of lost encryption keys.

## 10. Testing strategy

### Unit tests
- Domain use cases.
- Mappers.
- Validation.
- State reducers/ViewModels.
- Encryption wrapper behavior (using test vectors and vetted library APIs).

### Android instrumentation tests
- Room DAO and migration tests.
- Import via Storage Access Framework.
- Playback lifecycle.
- File deletion and recovery behavior.
- Permission denial flows.
- Device-specific capability assessment.

### Backend tests
- Controller/API tests.
- Authorization and ownership isolation.
- Database migration tests.
- Upload authorization.
- AI provider failure and retry behavior.
- Deletion propagation.

### Device test matrix
- OnePlus 10T / Android 15 (primary).
- At least one current Pixel device.
- At least one other OEM device.
- Each Android version and source combination documented separately.

## 11. Definition of technical readiness

- Reproducible build.
- Dependencies pinned and version-compatible.
- Tests pass in CI.
- No secrets in source control.
- Permission rationale documented.
- Recording capabilities verified, not assumed.
- Threat model and privacy review completed before cloud release.
