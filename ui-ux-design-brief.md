# CallVault — UI/UX Design Brief

**Version:** 0.1  
**Product:** Android-first privacy-first audio library

## 1. Design objective

Create a premium, calm and trustworthy interface for managing sensitive audio. The experience should feel like a polished productivity tool rather than a call-spying utility.

Reference qualities:
- Linear: restrained hierarchy and spacing.
- Raycast: focused, efficient interactions.
- Modern audio apps: clear playback controls and waveform use.
- Android Material 3: native accessibility and interaction conventions.

Do not copy another product's exact interface.

## 2. Brand personality

- Trustworthy.
- Quietly premium.
- Clear and transparent.
- Organized.
- Privacy-conscious.
- Technically capable without looking intimidating.

Avoid:
- Spyware visual language.
- Hidden microphone motifs.
- Aggressive recording animations.
- Misleading “record every call” claims.
- Excessive gradients, glass effects or decorative charts.

## 3. Visual system

### Theme
Dark theme is the initial default. Provide a light theme later if requested.

### Suggested tokens
- Background: deep navy/near-black.
- Surface: elevated charcoal/navy.
- Primary accent: violet.
- Secondary accent: cool blue.
- Success: green.
- Warning: amber.
- Error: red.
- Main text: near-white.
- Secondary text: muted cool gray.

Use semantic color tokens, not hard-coded colors scattered across components.

### Typography
- Use platform-friendly sans-serif typography.
- Strong, compact page titles.
- Clear body text.
- Tabular numerals for durations, timestamps and storage.
- Avoid very small secondary text.

### Shape and spacing
- Card radius: approximately 16–20 dp.
- Buttons: comfortable touch targets.
- Consistent 4/8 dp spacing scale.
- Subtle borders and elevation.
- Avoid placing every element inside a card.

## 4. Navigation

Primary Android navigation:
1. Home
2. Recordings
3. AI Insights
4. Backup
5. Settings

Use a bottom navigation bar for the primary destinations. On larger screens, consider a navigation rail or adaptive navigation layout.

## 5. Screen specifications

### Home
Purpose: summarize the user's local library and important status.
Components:
- Greeting/header.
- Local library count.
- Total duration.
- Storage usage.
- Recent recordings.
- Compatibility status shortcut.
- Backup status.
- Empty-state onboarding.

Avoid showing a “recording now” indicator unless an actual permitted recording session is active.

### Recordings
Purpose: find and manage audio.
Components:
- Search field.
- Sort/filter controls.
- Recording rows/cards.
- Source badge: Cellular (verified), VoIP (verified), Imported.
- Date, duration and processing status.
- Empty and no-results states.

### Recording detail/player
Purpose: listen and inspect one audio item.
Components:
- Title and metadata.
- Waveform or progress bar.
- Play/pause and seek.
- Transcript and Summary tabs/sections.
- Export, rename and delete actions.
- AI processing status and disclosure.

### AI Insights
Purpose: review generated information.
Components:
- Recent transcript/summary list.
- Key points.
- Decisions.
- Action items.
- Search within transcript.
- Clear AI-generated label.
- Source recording link.

### Backup
Purpose: manage encrypted backup.
Components:
- Backup enabled/disabled state.
- Last successful backup.
- Storage usage.
- Queue/progress.
- Failed item and retry.
- Restore.
- Encryption/key recovery explanation.

### Settings
Purpose: manage privacy and app behavior.
Sections:
- Appearance.
- Recording compatibility and preferences.
- Notifications.
- AI processing preferences.
- Backup and encryption.
- Storage and retention.
- Permissions.
- Privacy policy.
- Export data.
- Delete data/account.

## 6. Interaction principles

- One clear primary action per screen.
- Destructive actions require confirmation.
- Recording controls must be explicit and visible.
- Permission requests appear in context, with rationale.
- Unsupported capabilities are explained without blame.
- Long-running tasks show progress and can be retried.
- Do not use fake data in production.
- Distinguish imported, recorded and synchronized audio visually and textually.
- Use accessible labels for icon-only controls.

## 7. Motion

- Short, restrained transitions.
- No looping recording animation when idle.
- Playback progress reflects real media state.
- Upload/transcription progress reflects actual job state.
- Respect system reduced-motion settings.

## 8. Accessibility

- WCAG-informed contrast.
- Scalable text.
- TalkBack descriptions.
- Minimum comfortable touch targets.
- Do not communicate status by color alone.
- Support keyboard navigation in the web companion.
- Provide captions/transcript as an alternative way to access audio content.

## 9. Responsive web companion

- Desktop: left sidebar + content area.
- Tablet: compact navigation rail or sidebar.
- Mobile web: bottom navigation or compact menu.
- No web-only recording controls.
- Display only synchronized data.
- Clearly indicate local-only vs cloud-backed content.

## 10. Design deliverables

- Color and typography tokens.
- Component inventory.
- Android screen designs.
- Web responsive screen designs.
- Empty/loading/error/unsupported states.
- Recording disclosure and AI upload consent screens.
- Accessibility review.
