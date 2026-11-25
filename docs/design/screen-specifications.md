# CallVault — Screen Specifications

**Version:** 1.0  
**Status:** Home screen initial UI implemented and visually checked on API 37 emulator; other screens remain specification-only.

## 1. Shared screen rules

- Premium dark visual direction with violet accent.
- Material 3 and Jetpack Compose.
- Primary navigation: Home, Library, Insights, Backup, Settings.
- Show provenance and status explicitly.
- Include empty/loading/error/offline/unsupported states where applicable.
- Demo fixtures are development-only and visibly identified.
- Never imply call recording, playback, AI processing or backup is working unless its real subsystem reports that state.

## 2. Home

### Purpose
Summarize the user's local library and provide entry points to common tasks.

### Layout
1. Header: CallVault identity, greeting and profile action.
2. Overview: local item count, total duration and storage usage.
3. Quick actions: Import audio; Recording compatibility.
4. Recent recordings.
5. AI insights preview only when real results exist.
6. Mini-player only when actual media is playing.
7. Five-item bottom navigation.

### Behavior
- Tapping an item opens Recording Details.
- Import opens Android Storage Access Framework picker.
- Compatibility opens source/device status.
- Insights preview opens the relevant real result.
- Mini-player opens Full Player.
- Bottom navigation changes primary destination.

### States
- First-use empty.
- Loading.
- Populated.
- Offline.
- Error.
- No AI insights.
- No active playback.

### Acceptance
- Layout adapts to compact device sizes and font scaling.
- Navigation works.
- Import action opens system picker when implemented.
- Counts/storage reflect actual local data, or are clearly marked as development fixture.
- No fake recording indicator or fake playback state.

### Home implementation status — 26 November 2026

Implemented in `android/app/src/main/java/dev/callvault/app/feature/home/HomeScreen.kt`:
- CallVault header and supporting tagline.
- Library overview with intentionally unavailable values (`—`) rather than fabricated statistics.
- Import Audio and Recording Compatibility actions that currently open explanatory dialogs.
- Recent Recordings honest empty state.
- Privacy-first information card.
- Five-destination bottom navigation shell in `MainActivity.kt`.

The user supplied an emulator screenshot confirming the Home layout and bottom navigation render on Medium Phone API 37.0. Library, Insights, Backup and Settings are placeholder destinations. The import picker, local data, recording compatibility assessment and functional library are not connected yet. Temporary text glyphs are used for navigation icons.

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
