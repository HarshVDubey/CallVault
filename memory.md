# CallVault — Project Memory and Decision Log

**Purpose:** Durable handoff file for ChatGPT, coding assistants and contributors.  
**Last updated:** 26 November 2026  
**Memory version:** 1.1

## 1. Project identity

- Name: CallVault (working name; trademark/domain clearance not completed).
- Product: privacy-first audio library, playback, search, transcription, AI summaries and optional encrypted backup.
- Distribution goal: Google Play Store.
- Primary platform: Android.
- Development strategy: hybrid.
- Priority: working Android app, not web prototype.
- Primary physical compatibility target: OnePlus 10T 5G / Android 15.

## 2. User-approved decisions

| Area | Decision | Status |
|---|---|---|
| Product | CallVault | Working name |
| Platform | Android-first | Confirmed |
| Architecture | Hybrid Android + backend + companion web | Confirmed |
| Native language/UI | Kotlin + Jetpack Compose | Confirmed |
| Android architecture | MVVM with Clean Architecture boundaries | Confirmed |
| Local database | Room | Approved target; not integrated yet |
| Backend | Spring Boot modular monolith | Confirmed direction |
| Backend language | Java | Confirmed |
| Cloud relational DB | PostgreSQL | Confirmed direction |
| Web builder | Lovable | Confirmed |
| Web client | React/TypeScript | Target |
| Backup | Optional client-side encrypted audio backup | Requirement |
| Distribution | Google Play Store | Goal |
| Call types | Cellular SIM and internet/VoIP both desired, subject to feasibility | User preference |
| Recording modes | Manual and automatic both desired, subject to feasibility/policy | User preference |
| Product features | Playback, search, transcription, summaries, backup | User-selected scope |
| Design style | Premium | Confirmed |
| Theme | Dark | Confirmed direction |
| Accent | Violet | Confirmed |
| Primary navigation | Bottom navigation | Confirmed |
| Main destinations | Home, Library, Insights, Backup, Settings | Confirmed visual/navigation naming |

## 3. Critical product constraints

- Never promise universal SIM call recording.
- Never promise universal WhatsApp/third-party VoIP recording.
- Call-state detection does not imply call-audio access.
- Android restricts internal call-audio capture for ordinary third-party apps.
- Do not use Accessibility Services to bypass recording restrictions.
- Do not use root-only, privileged permission or hidden capture workarounds.
- Do not fake recording state or present imported/demo audio as CallVault-recorded.
- Recording must be transparent, visible and consent-aware.
- Cloud AI processing requires explicit user action and disclosure.
- Audio backup must be encrypted client-side before upload.
- Verify current Android and Google Play requirements before release; target SDK rules can change.

## 4. Product distinction

CallVault remains useful if cellular/third-party call recording is unavailable:
- Import and manage audio.
- Play and search local recordings.
- Transcribe user-selected files.
- Summarize conversations.
- Back up encrypted audio.
- Offer recording only on verified permitted configurations.

A CallVault-controlled VoIP feature would be a separate product decision with participant consent and legal/policy review.

## 5. Repository and Android project facts

- Repository: `HarshVDubey/CallVault`.
- Default working branch at documentation baseline: `main`.
- Android project directory is `android/` inside the repository root shown in Windows Explorer; root also contains `.git` and the Markdown documents.
- User screenshots show the repository nested under Downloads/CallVault_Project_Documents/CallVault_Project_Documents. Use the actual local Git root shown by Android Studio; do not assume the earlier StudioProjects path.
- Android package: `dev.callvault.app`.
- Gradle modules: single `:app`.
- Observed generated packages: core.designsystem.components/theme; data.local/mapper/repository; domain.model/repository/usecase; feature.home/recordings/player/insights/backup/settings; generated ui.theme remains; MainActivity.kt.
- The screenshot proves package folders exist, not that each has implementation.

### Supplied build configuration

| Setting | Value |
|---|---|
| Gradle wrapper | 9.6.0 |
| AGP | 9.5.3 |
| Kotlin | 2.2.10 |
| Compose BOM | 2026.02.01 |
| compileSdk | 37 |
| targetSdk | 37 |
| minSdk | 26 |
| Java source/target | 11 |
| namespace/applicationId | dev.callvault.app |
| Module | :app |

The user showed successful Gradle sync/build and successful installation/launch of the generated starter app on Medium Phone API 37.0 emulator. No clean CI build is verified. The OnePlus 10T physical device has not yet been used for compatibility tests.

No Room, Navigation Compose, Media3, WorkManager, backend or AI dependencies are confirmed as added. Do not claim they are integrated.

## 6. Design research supplied

User selected premium dark visual direction, violet accent and bottom navigation.

Mobbin references/screenshots supplied:
- Google Drive onboarding.
- Spotify onboarding, library and playback.
- Otter.ai onboarding.
- Apple Music player/system-capture/player patterns.
- Clubhouse voice-message hold/release/cancel interaction (not evidence of phone-call recording feasibility).
- Transcript examples including voice-message transcription and transcript reading/highlighting.
- AI summary examples including a YouTube “Ask about this video” flow.
- Cloud backup examples including iCloud backup settings/history and encrypted backup education.
- Privacy settings examples from multiple apps.

Research is used for interaction patterns, not copying exact layouts/assets. Initial color values in design docs are proposed tokens, not individually approved brand specifications.

## 7. Current implementation status

### Verified complete
- GitHub repository connected/access available to assistant.
- Android project created in repository's `android/` folder.
- Kotlin/Compose starter project generated.
- Gradle sync/build succeeded in user's environment.
- Starter app installed/launched on API 37 emulator.
- Initial package folders created.
- Mobbin design direction and reference screenshots supplied.
- Home screen first-milestone scope defined.
- CV Design System 1.0 theme foundation implemented in `dev.callvault.app.ui.theme`: `Color.kt`, `Theme.kt`, `Type.kt`, and new `Shapes.kt`.
- Material 3 CallVault color schemes implemented; dark mode is the default and dynamic color is disabled by default.
- Reusable components implemented under `dev.callvault.app.core.designsystem.components`: `CVButton.kt` (primary and secondary), `CVCard.kt`, `CVRecordingBadge.kt`, and `CVEmptyState.kt`.
- `CVDesignSystemPreview.kt` created and temporarily wired into `MainActivity.kt`.
- User supplied emulator screenshots confirming the preview builds/runs and showing the corrected dark background, buttons, badge, card and empty state.
- `HomeScreen.kt` implemented in `dev.callvault.app.feature.home`; includes branding/header, honest empty library overview, Import Audio and Recording Compatibility explanatory dialogs, Recent Recordings empty state and privacy note.
- `MainActivity.kt` now launches the Home screen and provides five selectable bottom navigation destinations: Home, Library, Insights, Backup and Settings. The four non-Home destinations are placeholders.
- User supplied an API 37 emulator screenshot confirming Home screen and five-tab navigation render.
- Import Audio is not connected to Storage Access Framework; compatibility dialog is informational only. No real audio items/statistics or recording functionality exist.
- Navigation uses temporary text glyphs rather than final Material icons.
- Current app is a Home UI shell, not a functional audio library.

### Not implemented / not verified
- Actual CallVault Home screen.
- Five-destination working navigation.
- Room database/entities/DAOs/migrations.
- Import flow.
- Playback engine.
- Recording capability adapter.
- SIM or VoIP audio capture.
- Transcription/AI summaries.
- Backend.
- PostgreSQL migrations.
- Encrypted cloud backup.
- Lovable web dashboard.
- OnePlus physical-device testing.
- Google Play compliance review.
- Automated tests for the new design-system components.
- Status-bar icon contrast/system-bar inset polish; dark status-bar icons were noticed in the emulator screenshot and remain to be corrected/verified.

### Current phase status — 26 November 2026
**Phase 1 — Android foundation and first UI: IN PROGRESS.** The design-system foundation and reusable component preview are implemented and visually verified by user screenshot on the API 37 emulator. This does not complete Phase 1: Home, navigation, Room, automated test baseline and OnePlus physical-device validation are still pending.

### Design-system implementation paths
- `android/app/src/main/java/dev/callvault/app/ui/theme/Color.kt`
- `android/app/src/main/java/dev/callvault/app/ui/theme/Theme.kt`
- `android/app/src/main/java/dev/callvault/app/ui/theme/Type.kt`
- `android/app/src/main/java/dev/callvault/app/ui/theme/Shapes.kt`
- `android/app/src/main/java/dev/callvault/app/core/designsystem/components/CVButton.kt`
- `android/app/src/main/java/dev/callvault/app/core/designsystem/components/CVCard.kt`
- `android/app/src/main/java/dev/callvault/app/core/designsystem/components/CVRecordingBadge.kt`
- `android/app/src/main/java/dev/callvault/app/core/designsystem/components/CVEmptyState.kt`
- `android/app/src/main/java/dev/callvault/app/core/designsystem/components/CVDesignSystemPreview.kt`
- `android/app/src/main/java/dev/callvault/app/MainActivity.kt` currently launches the preview.

## 8. Delivery order

0. Discovery and recording feasibility.
1. Android foundation and first UI.
2. Import, library, playback and recording feasibility.
3. Local security and library polish.
4. Backend foundation.
5. AI transcription and summaries.
6. Encrypted backup and synchronization.
7. Lovable companion web dashboard.
8. Hardening and Google Play release.

See `phases.md` for exit criteria.

## 9. Working rules for AI coding assistants

- Read `README.md`, `memory.md`, `prd.md`, `architecture.md` and `trd.md` before architectural changes.
- Read the relevant flow/design specification before implementing a screen.
- Treat these documents as baseline, but do not convert unverified assumptions into facts.
- Do not silently change user-approved technology choices.
- Do not invent Android recording capabilities.
- Do not implement unsupported permissions or policy workarounds.
- Keep changes small and testable.
- Update docs when a decision or implementation status changes.
- Report implemented, partially implemented, blocked and not implemented requirements separately.
- Ask before major architecture/scope changes.
- Distinguish source-derived facts, technical inference and proposed design.
- A screenshot/mockup does not prove the feature is implemented.

## 10. Decision log

| Date | Decision | Reason |
|---|---|---|
| 2026-11-25 | CallVault working name | Project identity |
| 2026-11-25 | Android-first | User priority |
| 2026-11-25 | Hybrid architecture | Native Android + Spring backend + Lovable web |
| 2026-11-25 | OnePlus 10T / Android 15 | Primary physical compatibility target |
| 2026-11-25 | Modular monolith backend | Avoid premature microservices |
| 2026-11-25 | Local-first | Core library works offline |
| 2026-11-25 | Premium dark + violet + bottom navigation | User-approved visual direction |
| 2026-11-25 | Home first | First UI implementation milestone |
| 2026-11-25 | Documentation-first governance | Prevent architectural drift and unsupported claims |
