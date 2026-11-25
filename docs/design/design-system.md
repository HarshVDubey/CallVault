# CV Design System 1.0

**Status:** Foundation implemented and visually checked in the API 37 emulator; tokens remain subject to accessibility and physical-device validation.  
**Platform:** Android / Jetpack Compose / Material 3  
**Direction:** Premium dark, violet accent, bottom navigation.

## 1. Approved direction

- Premium visual style.
- Dark theme as initial default.
- Violet accent.
- Five-item bottom navigation.
- Calm, trustworthy, privacy-conscious visual language.

The user approved the direction. Individual hex values, font choice and dimensions below are initial design tokens and require visual validation on device.

## 2. Color tokens (proposed)

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

## 3. Typography (proposed)

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
