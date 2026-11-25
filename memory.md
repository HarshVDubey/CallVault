# CallVault — Project Memory and Decision Log

**Purpose:** Durable handoff file for future ChatGPT/Copilot/Lovable sessions.  
**Last updated:** 25 November 2026

## 1. Project identity

- Name: CallVault (working name; trademark/domain clearance not yet done).
- Product: privacy-first audio recording management, transcription, AI summaries and optional encrypted backup.
- Distribution goal: Google Play Store.
- Primary platform: Android.
- Development strategy: hybrid.
- Priority: working Android application, not web prototype.
- Initial test device: OnePlus 10T 5G running Android 15.

## 2. Confirmed decisions

| Area | Decision |
|---|---|
| Architecture | Hybrid Android + companion web + backend |
| Priority | Android-first |
| Android language | Kotlin |
| Android UI | Jetpack Compose |
| Android architecture | MVVM with Clean Architecture boundaries |
| Local DB | Room |
| Backend | Spring Boot modular monolith initially |
| Backend language | Java |
| Cloud relational DB | PostgreSQL |
| Web builder | Lovable |
| Web client | React/TypeScript generated through Lovable |
| Audio backup | Private object storage, encrypted client-side |
| Product goal | Google Play Store |
| Core features | Library, playback, search, transcription, summaries, backup |

## 3. Product requirements from user

The user wants support for:
- Normal SIM calls (incoming and outgoing), where Android/device policy and a permitted capture path allow it.
- Internet calls, including interest in WhatsApp, subject to third-party app and Android restrictions.
- Manual and automatic recording where supported.
- Playback.
- Search.
- Transcription.
- AI summaries.
- Cloud backup.
- Google Play distribution.

## 4. Critical constraints

- Never promise universal SIM call recording.
- Never promise universal WhatsApp/third-party VoIP recording.
- Call-state detection does not imply call-audio access.
- Android restricts internal call audio capture for ordinary third-party applications.
- Do not use Accessibility Services to bypass recording restrictions.
- Do not use root-only, privileged permission or hidden recording workarounds.
- Do not fake recording state or present imported/demo audio as CallVault-recorded audio.
- Recording must be transparent, user-visible and consent-aware.
- Cloud AI processing requires explicit user action and disclosure.
- Audio backup must be encrypted client-side before upload.
- Check current Android and Google Play requirements before release; target SDK policy can change.

## 5. Important product distinction

CallVault can be valuable even when cellular/third-party call recording is unavailable:
- Import and manage audio.
- Play and search local recordings.
- Transcribe user-selected files.
- Summarize conversations.
- Back up encrypted audio.
- Offer recording only on verified, permitted configurations.

If a future VoIP calling feature is considered, it must be a separately scoped product feature with participant consent and legal/policy review.

## 6. Project delivery order

1. Phase 0: Android/Play policy and recording feasibility discovery.
2. Phase 1: Android foundation.
3. Phase 2: import, playback and device recording feasibility.
4. Phase 3: local security and library polish.
5. Phase 4: Lovable web companion.
6. Phase 5: Spring Boot backend.
7. Phase 6: transcription and AI summaries.
8. Phase 7: encrypted backup and sync.
9. Phase 8: security, compliance and Play Store release.

Do not build backend/AI complexity before the local Android foundation and recording feasibility gate.

## 7. Current progress at last update

- Product concept and hybrid architecture agreed.
- Android-first priority agreed.
- OnePlus 10T / Android 15 identified as initial test device.
- Initial Phase 1 Android Studio setup instructions provided.
- User then asked to prepare the project documentation.
- No confirmation yet that the Android Studio project has been created or built.
- No recording compatibility tests have been performed.

## 8. Next actions

1. Review this documentation package.
2. Create the Android Studio project and establish a reproducible build.
3. Complete Phase 1 navigation, package structure and database foundation.
4. Begin Phase 0/2 recording feasibility research and on-device tests before claiming call recording support.
5. Maintain a device/source compatibility matrix.
6. Only then plan AI/backend implementation details.

## 9. Working rules for AI coding assistants

- Read `memory.md`, `prd.md`, `phases.md`, `architecture.md` and `trd.md` before making architectural changes.
- Treat these documents as the current baseline.
- Do not silently change user-approved technology choices.
- Do not invent Android recording capabilities.
- Do not implement unsupported permissions or policy workarounds.
- Keep changes small and testable.
- Update documentation when a decision changes.
- Do not modify requirements documents just to make an incomplete implementation appear compliant.
- Clearly report implemented, partially implemented, blocked and not implemented requirements.
- Ask before making a major architecture or scope change.

## 10. Decision log

| Date | Decision | Reason |
|---|---|---|
| 2026-11-25 | CallVault working name | Project identity |
| 2026-11-25 | Android-first | User prioritizes working Android app |
| 2026-11-25 | Hybrid architecture | Lovable for web UI; Kotlin for native Android; Spring Boot backend |
| 2026-11-25 | OnePlus 10T / Android 15 | Initial device compatibility target |
| 2026-11-25 | Modular monolith backend | Avoid premature microservices |
| 2026-11-25 | Local-first | Core library should work offline |
