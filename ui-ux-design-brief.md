# CallVault — UI/UX Design Brief

**Version:** 2.0  
**Status:** Modern Colorful visual direction approved; detailed tokens and screen designs remain proposed pending review  
**Date:** 26 November 2026

## 1. Design objective

Create a premium, calm and trustworthy Android interface for managing sensitive audio. The experience should feel like a polished productivity and audio-management tool, not a call-spying utility.

The interface must make provenance and capability status obvious. A user must be able to distinguish verified recordings, imported files, unsupported/unverified sources and development-only fixtures.

## 2. Confirmed user design decisions

| Decision | Selection | Status |
|---|---|---|
| Overall style | Modern colorful, premium audio interface | Approved 26 Nov 2026 |
| Visual treatment | Blue-violet gradients and selective glassmorphism | Approved direction; details proposed |
| Color family | Electric blue, indigo, violet, magenta, cyan | Proposed palette within approved direction |
| Primary navigation | Bottom navigation | Confirmed |
| Main destinations | Home, Library, Insights, Backup, Settings | Confirmed |
| Native UI | Jetpack Compose + Material 3 | Confirmed |
| Design references | User-supplied colorful music/audio UI screenshots | Approved inspiration, not assets to copy |

The latest colorful blue-violet reference supersedes the earlier dark/violet-only visual direction. Individual hex values, gradient stops, typography, radii and opacity values remain proposed until reviewed.

## 3. Brand personality

- Trustworthy.
- Quietly premium.
- Clear and transparent.
- Organized.
- Privacy-conscious.
- Technically capable without looking intimidating.

Avoid spyware visual language, hidden microphone motifs, aggressive recording animations and misleading “record every call” claims. Gradients and glass effects are now part of the approved direction but must be selective, readable and purposeful. Avoid decorative charts without meaning.

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

### Visual direction

Use the user's selected modern colorful reference as the visual anchor: vivid electric-blue and indigo foundations, violet/magenta highlights, cyan audio accents, expressive artwork, gradient surfaces and restrained glassmorphism.

### Proposed color family

- Deep navy for grounding and readable contrast.
- Electric blue and indigo for large gradient surfaces.
- Violet and magenta for visual energy and selected elements.
- Cyan for audio-related highlights and waveform accents.
- Cloud white and muted lavender for text.
- Green, amber and coral for semantic status only.

Exact hex values and gradient stops are documented in `docs/design/design-system.md` as proposals pending validation.

### Typography proposal

- Modern, highly legible sans-serif.
- Manrope or a similar geometric family may be evaluated for display headings.
- Inter or platform sans-serif for body text.
- Tabular numerals for durations, timestamps and storage.
- Preserve Android font scaling; avoid fixed-height text that clips.

### Surface, spacing and shape proposal

- Gradient hero panels and feature cards.
- Translucent surfaces with subtle light borders used selectively.
- Avoid putting every element inside a glass card.
- Keep settings and backup surfaces calmer than Home and Player.
- Maintain comfortable touch targets, clear hierarchy and readable contrast.
- Final spacing, radii and blur values remain subject to screen-level design review.


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

## 13. Approved visual reference translation

The selected reference is a music/audio product interface. Translate its visual language into CallVault without importing its music-specific product semantics:
- Album/artist artwork patterns become recording artwork or abstract audio identity visuals.
- Music track metadata becomes recording title, source/provenance, date, duration and file information.
- Music discovery/recommendations become recent recordings, categories and AI insights.
- Player controls remain familiar, with recording-specific transcript, bookmark and source details.
- Any waveform presented as measured data must be generated from the actual audio; decorative wave motifs must not be represented as real waveform analysis.

Home and Player may be more expressive. Library, Backup and Settings should prioritize scanability and clarity while retaining the same color identity.

## 14. Implementation boundary

The existing Compose app currently implements the previous dark/violet theme. The selected direction is approved for future redesign, not yet implemented in code. The existing app functionality and technical architecture remain unchanged until each screen is redesigned, implemented and tested.
