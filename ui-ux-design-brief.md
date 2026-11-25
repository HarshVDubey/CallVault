# CallVault — UI/UX Design Brief

**Version:** 1.0  
**Status:** Approved visual direction; detailed tokens are initial proposals  
**Date:** 25 November 2026

## 1. Design objective

Create a premium, calm and trustworthy Android interface for managing sensitive audio. The experience should feel like a polished productivity and audio-management tool, not a call-spying utility.

The interface must make provenance and capability status obvious. A user must be able to distinguish verified recordings, imported files, unsupported/unverified sources and development-only fixtures.

## 2. Confirmed user design decisions

| Decision | Selection |
|---|---|
| Overall style | Premium |
| Theme direction | Dark |
| Accent | Violet |
| Primary navigation | Bottom navigation |
| Main destinations | Home, Library, Insights, Backup, Settings |
| Native UI | Jetpack Compose + Material 3 |
| Design research | Mobbin references supplied by user |

The dark/violet token values are proposed starting values; the user approved the direction, not every individual hex value.

## 3. Brand personality

- Trustworthy.
- Quietly premium.
- Clear and transparent.
- Organized.
- Privacy-conscious.
- Technically capable without looking intimidating.

Avoid spyware visual language, hidden microphone motifs, aggressive recording animations, misleading “record every call” claims, excessive gradients/glass effects and decorative charts without meaning.

## 4. Mobbin research supplied

The user supplied Mobbin screenshots and collections covering:
- Google Drive onboarding.
- Spotify onboarding, library and playback.
- Otter.ai onboarding.
- Apple Music player and system-capture/player patterns.
- Clubhouse voice-message hold/release/cancel interactions.
- Transcript examples including voice-message transcription, reading and highlighting.
- AI-summary examples including a video “Ask about this” interaction.
- Cloud-backup examples including iCloud backup settings/history and encrypted-backup education.
- Privacy-setting examples from multiple apps.

### Research interpretation

**Google Drive:** onboarding clarity, account/file organization, helpful empty states and familiar navigation patterns.

**Spotify and Apple Music:** persistent mini-player, prominent playback controls, progress/duration, immersive player and library organization.

**Otter.ai and transcript examples:** explain product value before asking for access; readable transcript; speaker/timestamp context where available; transcript linked to source audio.

**Clubhouse:** clear voice-interaction state feedback (hold, release, cancel, sent). This is an interaction reference only; it does not establish phone-call recording feasibility. Do not automatically copy the hold-to-talk gesture into call recording.

**AI-summary examples:** contextual questions and structured generated results. AI output must be clearly distinguished from original transcript.

**Backup examples:** opt-in backup, status/history, retention controls and encryption/recovery explanation. iCloud-specific behavior is not an Android implementation requirement.

**Privacy examples:** grouped controls, concise explanations and user-visible privacy settings.

These are pattern references, not permission to copy exact screen layouts, branding, illustrations or assets. Screenshots supplied by the user are design research material, not proof that a feature exists in CallVault.

## 5. Visual system

### Initial proposed color tokens

| Token | Hex | Usage |
|---|---|---|
| Background | #0B0A10 | Main screen |
| Surface | #13111B | Cards and sections |
| Elevated surface | #1C1926 | Dialogs/sheets |
| Primary | #8B5CF6 | Primary actions and selected states |
| Primary light | #A78BFA | Highlights |
| Text primary | #F8F7FC | Main text |
| Text secondary | #A5A0B3 | Supporting text |
| Border | #302B3A | Dividers/outlines |
| Success | #34D399 | Successful state |
| Warning | #FBBF24 | Warning |
| Error | #F87171 | Error |

Use semantic tokens rather than scattered hard-coded colors. Violet should communicate interaction and selection, not cover every surface.

### Typography proposal
- Platform-friendly sans-serif; Inter may be evaluated but is not a mandatory dependency.
- Screen titles: approximately 26–30sp, semibold.
- Section headings: approximately 18–20sp, semibold.
- Body: 14–16sp.
- Supporting text: 12–13sp, while preserving accessibility.
- Tabular numerals for durations, timestamps and storage.

### Spacing and shape proposal
- 4dp base spacing scale.
- Common spacing: 8dp, 16dp, 20dp.
- Screen horizontal padding: approximately 20dp.
- Card radius: approximately 18dp.
- Button radius: approximately 14dp.
- Touch targets must remain comfortable and accessible.
- Use subtle borders/elevation; avoid putting every element inside a card.

## 6. Navigation

Five primary destinations:
1. Home.
2. Library.
3. Insights.
4. Backup.
5. Settings.

Use Material 3 bottom navigation for compact screens. Consider adaptive navigation rail on larger layouts after responsive requirements are implemented.

Secondary screens (Recording Details, Full Player, Transcript, Compatibility) use contextual navigation and should not appear as primary destinations.

## 7. Screen direction

### Home
- CallVault identity, greeting and profile action.
- Local item count, total audio duration and storage usage.
- Import action and compatibility entry point.
- Recent recordings.
- AI insights preview only when real results exist.
- Mini-player only when real media is playing.
- Helpful first-use empty state.

### Library
- Search, sort and filters.
- Recording rows/cards with title, date, duration and source.
- Distinguish Imported, verified recorded source, unsupported/unverified and demo fixture.
- Processing and backup state where applicable.

### Recording details/player
- Title and metadata.
- Progress/waveform only when backed by actual media state.
- Play/pause, seek and duration.
- Transcript and Summary sections.
- Rename, export/share and delete actions.
- AI processing status and disclosure.

### Insights
- Transcript/summary list.
- Key points, decisions and action items when generated.
- AI-generated label.
- Link to source recording.
- Transcript search only when implemented.

### Backup
- Enabled/disabled state.
- Last successful backup.
- Storage and queue/progress.
- Failed item and retry.
- Restore.
- Encryption and key-recovery explanation.

### Settings
- Appearance.
- Recording compatibility/preferences.
- Notifications.
- AI processing.
- Backup/encryption.
- Storage/retention.
- Permissions.
- Privacy policy.
- Export/delete data.

## 8. Interaction principles

- One clear primary action per screen.
- Destructive actions require confirmation.
- Recording controls are explicit and visible.
- Permission requests appear in context with rationale.
- Unsupported capabilities are explained without blame.
- Long-running tasks show real progress and retry options.
- Do not use fake data in production.
- Imported, recorded and synchronized audio remain distinguishable.
- Icon-only controls have accessible labels.

## 9. Motion

- Short, restrained transitions.
- No looping recording animation when idle.
- Playback progress reflects actual media state.
- Upload/transcription progress reflects actual job state.
- Respect system reduced-motion settings.

## 10. Accessibility

- WCAG-informed contrast.
- Scalable text.
- TalkBack descriptions.
- Comfortable touch targets.
- Never communicate status by color alone.
- Keyboard navigation in web companion.
- Transcript as an alternative way to access audio content.

## 11. Responsive web companion

- Desktop: sidebar + content area.
- Tablet: compact navigation rail/sidebar.
- Mobile web: compact menu or bottom navigation.
- No web-only call recording controls.
- Display only synchronized data.
- Clearly distinguish local-only from cloud-backed content.

## 12. Design deliverables

- Semantic color/typography/shape/spacing tokens.
- Component inventory and state definitions.
- Android screen specifications.
- Web responsive specifications.
- Empty/loading/error/offline/unsupported/unverified states.
- Recording disclosure and AI upload consent screens.
- Accessibility review.
