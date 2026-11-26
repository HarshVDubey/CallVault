# CallVault — Screen Specifications

**Version:** 1.2  
**Status:** Modern Colorful blue-violet visual direction approved 26 November 2026; current Home/import implementation still uses the earlier UI and requires redesign. Other screens remain specification-only.

## 1. Shared screen rules

- Modern Colorful visual direction: electric blue/indigo/violet/magenta gradients, selective glassmorphism and expressive audio visuals.
- Material 3 and Jetpack Compose.
- Preserve contrast, legibility and accessibility; use calmer surfaces for data-heavy screens.
- Primary navigation: Home, Library, Insights, Backup, Settings.
- Show provenance and status explicitly.
- Include empty/loading/error/offline/unsupported states where applicable.
- Demo fixtures are development-only and visibly identified.
- Never imply call recording, playback, AI processing or backup is working unless its real subsystem reports that state.

## 2. Home

### Purpose
Summarize the user's local library and provide entry points to common tasks.

### Layout
1. Header: CallVault identity and supporting tagline.
2. Overview: local item count and total duration.
3. Quick actions: Import audio; Recording compatibility.
4. Recent recordings.
5. Privacy-first information card.
6. Five-item bottom navigation.

### Behavior
- Import opens Android Storage Access Framework picker.
- Compatibility opens the current source/device status explanation.
- Recent items are observed from the Room-backed repository.
- Bottom navigation changes primary destination.
- No mini-player is shown until actual media playback exists.

### States
- First-use empty.
- Importing.
- Populated.
- Import success.
- Import failure.
- Compatibility information.

### Acceptance
- Layout adapts to compact device sizes and font scaling.
- Navigation works.
- Import action opens system picker.
- Counts and duration reflect Room-backed local recordings.
- Import runs off the main thread.
- Imported files are copied into app-private storage.
- Import provenance is marked `IMPORTED`.
- No fake recording indicator or fake playback state.

### Home implementation status — 26 November 2026

Implemented in `android/app/src/main/java/dev/callvault/app/feature/home/HomeScreen.kt`:
- CallVault header and supporting tagline.
- Library overview with live item count and summed duration from Room Flow.
- Import Audio action launches `ActivityResultContracts.OpenDocument` with `audio/*`.
- Import runs through `AudioImportManager` and `RecordingRepositoryImpl` on `Dispatchers.IO`.
- Import progress and result/error dialog.
- Recent Recordings shows up to five imported items with title, duration, size and Imported audio provenance.
- Recording Compatibility remains an explanatory dialog; no capture capability is claimed.
- Privacy-first information card.
- Five-destination bottom navigation shell in `MainActivity.kt`.

Evidence: user supplied an API 37 emulator screenshot showing successful import of `sample-3s`; Home displayed 1 audio item, total duration 00:03, and the item as 00:03 / 51 KB. User also reported successful Gradle builds during Room/repository integration. Force-stop/relaunch persistence and physical-device behavior remain unverified.

Implementation note: repository/database creation currently occurs in the composable as a temporary manual-wiring approach. Refactor into a ViewModel/presentation layer before adding more Home behavior.

## 3. Library

### Purpose
Find and manage local audio items.

### Content
- Search field.
- Sort/filter controls.
- Recording/import list.
- Title, date, duration, source and processing status.
- Backup status when relevant.

### Behavior
- Search only fields actually indexed.
- Filters reflect available metadata.
- Tap opens Recording Details.
- Rename/delete/export actions are user-mediated and confirmed where destructive.

### States
Empty, loading, populated, no results, offline (for cloud data), missing file, unsupported format, error.

### Current status
The Home screen displays recent Room-backed imports, but the dedicated Library destination is still a placeholder. Search, sorting, filtering, details, rename, delete and export are not implemented.

## 4. Recording Details

### Purpose
Inspect one audio item and access its related content.

### Content
- Title, date/time, duration, source label.
- Player/progress.
- Transcript status/content.
- Summary status/content.
- Rename, export/share, delete, transcribe, summarize, backup actions as each becomes available.

### Rules
- Transcript and summary must link to the source item.
- AI output must be labelled.
- Cloud processing requires explicit disclosure/confirmation.
- Unavailable actions must not appear as active.

## 5. Full Player

### Purpose
Provide focused audio playback.

### Content
- Recording title and metadata.
- Progress/seek and elapsed/total time.
- Play/pause.
- Playback speed/skip controls only when implemented.
- Transcript shortcut when transcript exists.

### Rules
- Progress comes from actual media playback.
- Waveform is shown only when actual waveform data is available.
- Player resources are released appropriately.
- Playback state is preserved across in-app navigation where supported.

## 6. Insights

### Purpose
Review transcripts and generated summaries.

### Content
- Available transcript/summary list.
- Source recording link.
- Overview, key points, decisions and action items when generated.
- AI-generated label.
- Transcript search only when implemented.

### States
No insights, loading, processing, completed, failed, offline and deleted source.

## 7. Backup

### Purpose
Manage optional encrypted backup.

### Content
- Backup enabled/disabled state.
- Last successful backup.
- Queue/progress.
- Failed item and retry.
- Restore history/actions.
- Encryption and key-recovery explanation.

### Rules
- Backup is opt-in.
- Display actual job state.
- Do not claim encryption/backup is active until implemented and verified.
- Explain key-loss limitations.

## 8. Settings

### Sections
- Appearance.
- Recording compatibility/preferences.
- Notifications.
- AI processing preferences.
- Backup/encryption.
- Storage/retention.
- Permissions.
- Privacy policy.
- Export data.
- Delete data/account.

Only settings backed by real functionality should be interactive.

## 9. Onboarding

1. Welcome to CallVault.
2. Explain audio library/import and supported recording scope.
3. Explain transcription and AI summaries as optional capabilities.
4. Explain local storage, cloud processing and backup privacy.
5. Enter Home.
6. Request permissions contextually when the user invokes the relevant feature.

Do not request all permissions on first launch. Onboarding must not promise universal cellular or third-party VoIP capture.

## 10. Recording Compatibility

- Explain call-state detection versus call-audio access.
- Separate cellular SIM and third-party VoIP.
- Show Supported (verified), Unsupported or Unverified.
- Include exact device/OS/source evidence for Supported.
- Do not offer active recording action for unsupported/unverified source.

## 11. Acceptance criteria

- Screen matches semantic design tokens.
- Every primary interaction has a defined destination/action.
- All applicable states are designed.
- Accessible semantics and scalable text.
- Real data drives status/progress.
- No demo data in production.
- Build and UI tests pass before marking implemented.

## 11. Visual redesign targets — approved direction, not implemented

### Home — Expressive audio dashboard
- Blue/indigo gradient hero region with CallVault identity and greeting.
- Prominent Import Audio action.
- Compact library statistics and recent recording cards.
- Recent items show title, duration, date and provenance.
- AI insights preview only when actual generated results exist.
- Use expressive abstract audio artwork sparingly; no fake recording state.

### Library — Organized recording collection
- Search and category/filter controls at the top.
- Calm blue-violet surfaces with selective artwork tiles.
- Group recordings by date where useful.
- Clear Imported/Verified/Unverified/Unsupported provenance labels.
- Sort, selection and destructive actions must have explicit states.

### Full Player — Immersive listening
- Deep blue-violet gradient background and large artwork/visualization region.
- Recording title, source, date and duration.
- Actual seek/progress and prominent play/pause controls.
- Waveform only when generated from real audio data.
- Transcript, bookmarks and summary entry points when those features exist.
- No artist/track metadata copied from music-app references.

### Insights — Conversation intelligence
- Structured summary, key points, decisions and action items.
- Transcript sections linked to source recording.
- AI-generated label and processing status.
- Use gradient accents for hierarchy, not as a substitute for information.

### Backup — Secure archive status
- Clear backup enabled/disabled state, last successful backup, queue and failure recovery.
- Encryption/key-recovery explanation.
- Restrained gradient panels; status must be textually explicit.

### Settings — Functional control centre
- Grouped settings with clear descriptions and toggles.
- Minimal decorative treatment.
- Maintain shared palette and component styling without sacrificing density or readability.

These specifications define the target visual language. They do not indicate that these screens or features are implemented.
