# CallVault — Component Library

**Version:** 1.0  
**Status:** Component inventory and behavior proposal; not yet implemented.

## 1. Principles

- Components use CV Design System semantic tokens.
- Feature screens own feature-specific state; reusable components remain presentation-focused.
- Components do not access repositories, Room, files or network clients directly.
- Every interactive control has accessible semantics and a defined state.
- No component implies an unimplemented capability.

## 2. Foundation components

| Component | Responsibility | Required states |
|---|---|---|
| CVScaffold | Screen layout and system insets | Standard |
| CVTopBar | Title, navigation and actions | Back/action |
| CVSectionHeader | Section title and optional action | Standard |
| CVPrimaryButton | Primary action | Enabled, disabled, loading |
| CVSecondaryButton | Secondary action | Enabled, disabled |
| CVIconButton | Icon action | Enabled, disabled; content description |
| CVStatusBadge | Textual status | Verified, Imported, Unverified, Unsupported, Processing, Failed |
| CVEmptyState | Explain no content and next action | Action/no action |
| CVErrorState | Explain failure and recovery | Retryable/permanent |
| CVLoadingState | Actual loading | Indeterminate/determinate |
| CVConfirmDialog | Confirm destructive/important action | Confirm/cancel |

## 3. Audio components

| Component | Responsibility | Required states |
|---|---|---|
| RecordingItemCard | Recording/import metadata | Imported/verified/demo/failed |
| AudioProgressBar | Actual media position | Idle/playing/buffering/ended |
| MiniPlayer | Current media summary and controls | Hidden/playing/paused/buffering |
| FullPlayerControls | Playback actions | Play/pause/seek |
| WaveformView | Render waveform when actual data exists | Loading/available/unavailable |
| SourceTypeLabel | Show provenance | Cellular/VoIP/Imported |
| CompatibilityRow | Show source support | Supported/Unsupported/Unverified |

A waveform must not be fabricated as if it represented the file. A decorative placeholder must be clearly non-data-bearing or omitted.

## 4. AI components

| Component | Responsibility | Required states |
|---|---|---|
| ProcessingStatus | Show transcription/summary job | Not requested/queued/processing/completed/failed |
| TranscriptSegment | Display text and optional timestamp/speaker label | Normal/selected |
| AiGeneratedLabel | Identify generated output | Visible whenever applicable |
| SummarySection | Overview, points, decisions, actions | Available/empty/failed |
| ConsentDisclosure | Explain audio destination and processing | Pending/accepted/cancelled |

Speaker labels are estimates if produced by diarization; do not imply verified identity.

## 5. Backup and privacy components

| Component | Responsibility | Required states |
|---|---|---|
| BackupStatusCard | Backup overview | Off/queued/uploading/backed up/failed |
| BackupProgress | Actual transfer progress | Indeterminate/determinate |
| EncryptionInfoCard | Explain encryption/key recovery | Informational |
| PrivacySettingRow | Setting with explanation | On/off/disabled |
| StorageUsageCard | Actual local/cloud usage | Loading/available/error |

## 6. Home composition

Proposed order:
1. Header/greeting.
2. Recording/library overview.
3. Import and compatibility quick actions.
4. Recent recordings.
5. Insights preview if available.
6. Mini-player only during actual playback.
7. Primary bottom navigation.

## 7. Interaction/accessibility contract

- Icon-only buttons require content descriptions.
- Disabled actions explain why where useful.
- Destructive actions require confirmation.
- Loading state must correspond to actual work.
- Buttons must have comfortable touch targets.
- Status badges include text, not color alone.
- No demo fixture may be confused with user audio.

## 8. Implementation status

All components are specified, not yet implemented. Add components incrementally and record file paths/tests in the implementation status section of `memory.md`.
