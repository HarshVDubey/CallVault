# CallVault Visual Design System 2.0

**Status:** New visual direction approved by user on 26 November 2026; detailed tokens are proposed and implementation is pending.  
**Platform:** Android / Jetpack Compose / Material 3  
**Direction:** Modern colorful audio interface; electric blue, indigo, violet and magenta gradients; selective glassmorphism; immersive audio player.

## 1. Approved visual direction — Modern Colorful

The user selected a supplied reference featuring a vivid blue-violet audio interface with gradient surfaces, translucent layered cards, expressive audio artwork and an immersive player. This replaces the previous premium dark/violet-only direction as the current design baseline.

- Rich electric blue, indigo, violet and magenta color atmosphere.
- Gradient backgrounds and selective translucent/glass-like surfaces.
- Bright, expressive audio artwork and waveform visualizations.
- Large, immersive playback controls and clear audio metadata.
- Five-item bottom navigation: Home, Library, Insights, Backup, Settings.
- Premium, polished, energetic and approachable personality.

The reference is inspiration, not an instruction to copy SoundWave branding, protected assets, exact layouts or music-specific content. CallVault remains an audio recording/library and conversation assistant.

## 2. Proposed color tokens — subject to design validation

| Token | Hex | Semantic use |
|---|---|---|
| Deep navy | #101A3A | Base/deep background |
| Electric blue | #315BCE | Primary gradient anchor |
| Indigo | #4D4FE0 | Gradient and selected surfaces |
| Violet | #8655E8 | Primary accent |
| Magenta | #D94BC2 | Gradient highlight |
| Cyan | #49D7E8 | Audio/waveform highlight |
| Cloud white | #F7F8FF | Primary text on dark surfaces |
| Muted lavender | #C2C8E8 | Secondary text |
| Glass surface | #FFFFFF at 10–14% opacity | Select translucent panels |
| Glass border | #FFFFFF at 18–24% opacity | Subtle card outlines |
| Success | #39D9A5 | Success state |
| Warning | #FFC46B | Warning state |
| Error | #FF718B | Error state |

These values are starting proposals, not individually user-approved production tokens. Verify gradient combinations, contrast and translucency on actual devices. Status must never be communicated by color alone.

## 3. Legacy implementation note

The existing Android project currently contains the earlier dark/violet Material 3 palette and theme foundation. That implementation remains the current code state until redesigned and validated; this document describes the newly approved target direction. Do not claim the new palette or glass effects are implemented.

## 4. Typography (proposed)

| Token | Hex | Semantic use |
|---|---|---|
| background | #0B0A10 | App background |
| surface | #13111B | Main cards/sections |
| surfaceElevated | #1C1926 | Dialogs and sheets |
| primary | #8B5CF6 | Primary actions/selection |
| primaryLight | #A78BFA | Highlights |
| onBackground | #F8F7FC | Main text |
| onSurfaceVariant | #A5A0B3 | Secondary text |
| outline | #302B3A | Borders/dividers |
| success | #34D399 | Success |
| warning | #FBBF24 | Warning |
| error | #F87171 | Error |

Violet is an interaction accent, not a blanket background color. Never communicate state by color alone.

## 4. Typography (proposed)

Use a legible platform sans-serif. Inter is optional and should only be added if its licensing, packaging and visual value are confirmed.

- Display/page title: 26–30sp, semibold.
- Section heading: 18–20sp, semibold.
- Body: 14–16sp.
- Supporting text: 12–13sp, subject to readability.
- Duration, time and storage: tabular numerals where practical.

Respect Android font scaling and avoid fixed-height text containers that clip enlarged text.

## 4. Spacing and shapes (proposed)

Use a 4dp base scale:
- 4dp: tight icon/text gap.
- 8dp: compact internal spacing.
- 16dp: standard component/section spacing.
- 20dp: screen horizontal padding.
- Card radius: 18dp.
- Button radius: 14dp.

Validate values against actual screen density and Material 3 component conventions. Touch targets must be comfortable.

## 5. Elevation and borders

- Prefer surface tonal hierarchy over heavy shadows.
- Use subtle outline/border for grouping.
- Do not wrap every text block in a card.
- Sheets/dialogs use elevated surface.
- Avoid excessive glassmorphism and gradients.

## 6. Iconography

Use a consistent Material Symbols/Material icon family. Icon-only controls require content descriptions. Use text labels for important source and processing states.

## 7. Navigation

Primary destinations: Home, Library, Insights, Backup, Settings. Use Material 3 NavigationBar on compact layouts. Consider adaptive navigation rail for larger screens after testing.

## 8. State semantics

- Success: text/icon plus color.
- Warning: text/icon plus color.
- Error: explanatory text, recovery action where possible.
- Unsupported/unverified: explicit label and explanation.
- Recording active: only when an actual permitted recording session exists.
- Playback active: only when media engine reports active playback.
- Backup/transcription progress: driven by real task state, never decorative animation.

## 9. Accessibility

- TalkBack semantics/content descriptions.
- Scalable text.
- Sufficient contrast.
- Minimum comfortable touch targets.
- Do not rely on color alone.
- Respect system reduced-motion settings.

## 10. Implementation status — 26 November 2026

Implemented in the Android project:
- `Color.kt`: CV semantic palette including dark surfaces, violet primary, text, outlines and status colors.
- `Theme.kt`: Material 3 dark and light schemes; dark theme is the default; dynamic color defaults to false.
- `Type.kt`: CallVault Material typography scale using the platform default font.
- `Shapes.kt`: Material 3 shape scale.
- `CVDesignSystemPreview.kt`: preview surface demonstrating primary/secondary buttons, recording badge, card and empty state.

The user ran the preview on the Medium Phone API 37.0 emulator and supplied screenshots. The dark background correction was applied and visually confirmed. This is an emulator visual check, not automated accessibility or physical-device validation.

Still pending: status-bar icon contrast/system-bar insets, font scaling, TalkBack checks, light theme review, narrow-screen review and OnePlus 10T validation.

## 11. Validation checklist

- Verify tokens on OnePlus 10T and emulator.
- Check light-on-dark contrast.
- Check font scaling and narrow widths.
- Check selected/unselected navigation states.
- Check empty/error/unsupported states.
- Check that demo fixtures are not present in production.

## 12. Gradient, glass and artwork rules

- Use gradients as composed backgrounds, hero panels and selected feature surfaces; avoid applying a loud gradient to every card.
- Glass-like surfaces should use restrained translucency, a subtle light border and sufficient contrast. Never place essential text over a busy image without a readable overlay.
- Prefer real recording artwork when supplied by the user or available through a legitimate source. Otherwise use generated abstract audio artwork or a waveform visualization; do not imply decorative art is a real waveform.
- Waveforms must represent actual audio samples when presented as data. A decorative sound motif must be visually distinguishable from measured waveform data.
- Keep functional screens such as Settings and Backup calmer than Home and Player while retaining the shared blue-violet identity.
- Avoid excessive blur, low-contrast glass text, visual clutter and motion that harms battery or accessibility.

## 13. Target screen personality

- Home: expressive hero area, quick import action, compact library metrics and recent recordings.
- Library: organized, searchable and calmer; artwork/visual markers support scanning.
- Player: immersive hero artwork, real playback timeline/waveform, prominent controls and recording metadata.
- Insights: structured AI summaries, transcript sections and source links.
- Backup: clear status, progress, encryption and recovery information; restrained gradients.
- Settings: clean grouped preferences with minimal decoration.

## 14. Validation checklist

- Test gradient and text contrast in dark and light environments.
- Test glass surfaces with accessibility text scaling and TalkBack.
- Verify performance on OnePlus 10T / Android 15 and supported emulator sizes.
- Respect reduced-motion settings.
- Confirm artwork and waveform visuals never misrepresent recording provenance or actual audio data.
- Review status-bar/navigation-bar contrast and edge-to-edge insets.
