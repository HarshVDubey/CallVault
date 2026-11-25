# CallVault — Backend Schema and API Data Model

**Version:** 0.1  
**Database:** PostgreSQL  
**Backend:** Spring Boot modular monolith  
**Status:** Proposed schema; validate against selected authentication, object storage and encryption design before migration.

## 1. Schema principles

- Store metadata and job state in PostgreSQL.
- Store encrypted audio objects in private object storage, never as large database blobs.
- Every user-owned record has an owner identifier.
- Enforce ownership in backend authorization checks, not only by filtering in the UI.
- Use UUID primary keys.
- Use UTC timestamps (`TIMESTAMPTZ`).
- Store only minimum necessary personal data.
- Do not store plaintext audio or full transcripts in audit logs.
- Audio uploaded for backup must be client-side encrypted.
- The schema must distinguish a recording from an imported file.

## 2. Entity relationships

```mermaid
erDiagram
  USERS ||--o{ DEVICES : has
  USERS ||--o{ RECORDINGS : owns
  RECORDINGS ||--o{ TRANSCRIPTS : has
  RECORDINGS ||--o{ SUMMARIES : has
  RECORDINGS ||--o{ BACKUP_OBJECTS : backed_up_as
  RECORDINGS ||--o{ PROCESSING_JOBS : processed_by
  USERS ||--o{ PROCESSING_JOBS : requests
  USERS ||--o{ AUDIT_EVENTS : generates
  USERS ||--o{ BACKUP_OBJECTS : owns

  USERS {
    uuid id PK
    text email
    text display_name
    timestamptz created_at
    timestamptz updated_at
    timestamptz deleted_at
  }
  DEVICES {
    uuid id PK
    uuid user_id FK
    text platform
    text app_version
    text device_label
    timestamptz last_seen_at
  }
  RECORDINGS {
    uuid id PK
    uuid user_id FK
    text title
    text source_type
    text status
    bigint duration_ms
    bigint size_bytes
    text mime_type
    text local_reference
    timestamptz captured_at
    timestamptz created_at
    timestamptz deleted_at
  }
  TRANSCRIPTS {
    uuid id PK
    uuid recording_id FK
    text language
    text transcript_text
    jsonb segments
    text status
    timestamptz created_at
  }
  SUMMARIES {
    uuid id PK
    uuid recording_id FK
    text overview
    jsonb key_points
    jsonb decisions
    jsonb action_items
    text model_label
    timestamptz created_at
  }
  PROCESSING_JOBS {
    uuid id PK
    uuid user_id FK
    uuid recording_id FK
    text job_type
    text status
    text provider
    text provider_job_id
    text error_code
    timestamptz created_at
    timestamptz completed_at
  }
  BACKUP_OBJECTS {
    uuid id PK
    uuid user_id FK
    uuid recording_id FK
    text object_key
    text encryption_version
    text wrapped_key_reference
    text checksum_sha256
    bigint ciphertext_size
    text status
    timestamptz created_at
  }
  AUDIT_EVENTS {
    uuid id PK
    uuid user_id FK
    text event_type
    jsonb safe_metadata
    timestamptz created_at
  }
```

## 3. PostgreSQL DDL proposal

```sql
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email TEXT UNIQUE,
    display_name TEXT,
    auth_subject TEXT UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at TIMESTAMPTZ
);

CREATE TABLE devices (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    platform TEXT NOT NULL DEFAULT 'ANDROID',
    app_version TEXT,
    device_label TEXT,
    os_version TEXT,
    last_seen_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE recordings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    device_id UUID REFERENCES devices(id) ON DELETE SET NULL,
    title TEXT NOT NULL,
    source_type TEXT NOT NULL CHECK (
        source_type IN ('CELLULAR', 'VOIP', 'IMPORTED')
    ),
    source_app TEXT,
    source_verification TEXT NOT NULL DEFAULT 'UNVERIFIED'
        CHECK (source_verification IN ('VERIFIED', 'UNVERIFIED', 'UNSUPPORTED', 'NOT_APPLICABLE')),
    status TEXT NOT NULL DEFAULT 'AVAILABLE'
        CHECK (status IN ('PENDING', 'AVAILABLE', 'FAILED', 'DELETED')),
    duration_ms BIGINT CHECK (duration_ms IS NULL OR duration_ms >= 0),
    size_bytes BIGINT CHECK (size_bytes IS NULL OR size_bytes >= 0),
    mime_type TEXT,
    checksum_sha256 TEXT,
    captured_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at TIMESTAMPTZ,
    metadata JSONB NOT NULL DEFAULT '{}'::jsonb
);

CREATE INDEX idx_recordings_user_created
    ON recordings(user_id, created_at DESC)
    WHERE deleted_at IS NULL;

CREATE TABLE transcripts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    recording_id UUID NOT NULL REFERENCES recordings(id) ON DELETE CASCADE,
    language TEXT,
    transcript_text TEXT,
    segments JSONB NOT NULL DEFAULT '[]'::jsonb,
    provider TEXT,
    status TEXT NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'DELETED')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_transcripts_recording
    ON transcripts(recording_id, created_at DESC);

CREATE TABLE summaries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    recording_id UUID NOT NULL REFERENCES recordings(id) ON DELETE CASCADE,
    transcript_id UUID REFERENCES transcripts(id) ON DELETE SET NULL,
    overview TEXT,
    key_points JSONB NOT NULL DEFAULT '[]'::jsonb,
    decisions JSONB NOT NULL DEFAULT '[]'::jsonb,
    action_items JSONB NOT NULL DEFAULT '[]'::jsonb,
    model_label TEXT,
    status TEXT NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'DELETED')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE processing_jobs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recording_id UUID NOT NULL REFERENCES recordings(id) ON DELETE CASCADE,
    job_type TEXT NOT NULL CHECK (job_type IN ('TRANSCRIPTION', 'SUMMARY')),
    status TEXT NOT NULL DEFAULT 'QUEUED'
        CHECK (status IN ('QUEUED', 'UPLOADING', 'PROCESSING', 'COMPLETED', 'FAILED', 'CANCELLED')),
    provider TEXT,
    provider_job_id TEXT,
    error_code TEXT,
    safe_error_message TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    started_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ
);

CREATE INDEX idx_jobs_user_status
    ON processing_jobs(user_id, status, created_at DESC);

CREATE TABLE backup_objects (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recording_id UUID NOT NULL REFERENCES recordings(id) ON DELETE CASCADE,
    object_key TEXT NOT NULL UNIQUE,
    encryption_version TEXT NOT NULL,
    wrapped_key_reference TEXT,
    checksum_sha256 TEXT NOT NULL,
    ciphertext_size BIGINT NOT NULL CHECK (ciphertext_size >= 0),
    status TEXT NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'UPLOADING', 'BACKED_UP', 'FAILED', 'DELETED')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE audit_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    event_type TEXT NOT NULL,
    safe_metadata JSONB NOT NULL DEFAULT '{}'::jsonb,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_audit_user_created
    ON audit_events(user_id, created_at DESC);
```

## 4. Schema notes and required refinements

1. The DDL is a starting proposal, not a production migration.
2. Add row-level security only if it fits the selected database access model; backend ownership checks remain mandatory.
3. Consider a separate `recording_tags` and `tags` model when tags enter MVP scope.
4. Consider a `backup_manifests` table for versioned backup sets and restore state.
5. Avoid storing raw encryption keys in `wrapped_key_reference`; it must be a reference or opaque wrapped-key envelope designed by the security model.
6. Add retention and deletion workflows for external AI providers.
7. Add tenant/user quotas and rate-limit records if required.
8. Ensure deleting a recording removes or schedules deletion of object storage data and derived transcripts/summaries.
9. Define whether transcript text is encrypted at rest and whether server-side search is allowed. For stronger privacy, consider client-side encrypted transcript storage, accepting reduced server search capability.
10. Do not store phone numbers, contact names or participant identities unless a clear feature requirement and privacy justification exist.

## 5. Suggested API contract

Base path: `/api/v1`

### Identity
- `GET /me` — current user profile.
- `DELETE /me` — request account deletion.

### Recordings
- `GET /recordings` — paginated recording metadata.
- `POST /recordings` — register synchronized recording metadata.
- `GET /recordings/{recordingId}` — recording metadata.
- `PATCH /recordings/{recordingId}` — update title/tags/metadata.
- `DELETE /recordings/{recordingId}` — delete recording and related data.

### Processing
- `POST /recordings/{recordingId}/transcriptions` — request transcription after explicit consent.
- `GET /processing-jobs/{jobId}` — job status.
- `POST /recordings/{recordingId}/summaries` — request summary.
- `GET /recordings/{recordingId}/transcripts` — list transcripts.
- `GET /recordings/{recordingId}/summaries` — list summaries.

### Backup
- `POST /recordings/{recordingId}/backup-upload` — request authorized upload.
- `POST /recordings/{recordingId}/backup-complete` — confirm upload and checksum.
- `GET /backups` — list backup objects/status.
- `POST /backups/{backupId}/restore-access` — request short-lived download authorization.
- `DELETE /backups/{backupId}` — delete cloud backup.

### API rules
- Every endpoint checks authenticated user ownership.
- Use pagination.
- Use idempotency keys for job and upload creation.
- Return consistent error objects.
- Never return permanent public object-storage URLs.
- Use short-lived upload/download authorization.
- Validate file size, content type and quota.
- Do not accept arbitrary client-supplied object keys.

## 6. Example error response

```json
{
  "code": "RECORDING_NOT_FOUND",
  "message": "The requested recording was not found.",
  "requestId": "opaque-correlation-id"
}
```

Do not include transcript/audio content, credentials or internal stack traces in client-facing errors.

## 7. Data lifecycle

- **Local only:** audio and metadata remain on Android.
- **AI requested:** audio is transmitted only after user confirmation; transcript/summary lifecycle is linked to source recording.
- **Backup requested:** client encrypts audio before upload.
- **Delete:** local and cloud copies plus derived data are deleted or queued for deletion; report incomplete deletion clearly.
- **Account deletion:** revoke credentials, delete or anonymize personal data according to policy and legal obligations, and remove cloud objects.
