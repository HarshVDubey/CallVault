# CallVault

**Product:** Privacy-first Android audio library and conversation assistant  
**Distribution goal:** Google Play Store  
**Product direction:** Android-first hybrid  
**Primary compatibility device:** OnePlus 10T 5G, Android 15  
**Documentation baseline:** 26 November 2026  
**Documentation version:** 1.2  
**Project status:** Android UI, Room persistence foundation and first audio-import flow implemented; playback and recording feasibility remain outstanding.

## Product statement

CallVault helps users manage audio conversations they are legally and technically able to record or import. It provides a local audio library, playback, search, optional transcription and AI summaries, and optional encrypted cloud backup. A companion web dashboard may be built with Lovable; native device integration remains in Kotlin/Android.

## Source-of-truth order

When documents appear inconsistent, use this order and record the resolution in `memory.md`:

1. Explicit user-approved decisions recorded in `memory.md`.
2. Product scope and acceptance criteria in `prd.md`.
3. System boundaries in `architecture.md`.
4. Verified implementation/toolchain facts in `trd.md`.
5. User journeys in `appflow.md`.
6. Visual and interaction rules in `ui-ux-design-brief.md` and `docs/design/`.
7. Delivery sequencing in `phases.md`.
8. Backend proposal in `backend-schema.md`.

A source-of-truth document does not turn an unverified assumption into a fact. Each statement must be identifiable as **Confirmed**, **Proposed**, **Unverified**, or **Blocked** where relevant.

## Document index

| Document | Purpose |
|---|---|
| [prd.md](prd.md) | Product vision, scope, requirements and acceptance criteria |
| [phases.md](phases.md) | Delivery phases, milestones and exit gates |
| [architecture.md](architecture.md) | Android, backend, web and data boundaries |
| [trd.md](trd.md) | Technical baseline, dependencies, constraints and tests |
| [memory.md](memory.md) | Durable decisions, verified status and unresolved questions |
| [appflow.md](appflow.md) | Navigation, user journeys and UI states |
| [ui-ux-design-brief.md](ui-ux-design-brief.md) | Brand direction, Mobbin research and design principles |
| [backend-schema.md](backend-schema.md) | Proposed PostgreSQL schema and API contract |
| [docs/design/design-system.md](docs/design/design-system.md) | Initial design tokens and foundations |
| [docs/design/component-library.md](docs/design/component-library.md) | Reusable component inventory and behavior |
| [docs/design/screen-specifications.md](docs/design/screen-specifications.md) | Screen requirements and acceptance criteria |

## Critical product constraint

CallVault must not claim universal cellular SIM-call or third-party VoIP-call recording. Android restricts access to internal call audio, and third-party applications control their own audio capture behavior. A detected call does not grant access to its audio.

The app must distinguish:
- **Verified recording:** a permitted capture path has been demonstrated on the exact device, OS, source app and configuration.
- **Unsupported:** the platform or source does not expose a permitted capture path.
- **Unverified:** feasibility has not been demonstrated.
- **Imported audio:** a file selected by the user; not recorded by CallVault.
- **Call metadata:** call-state or user-entered information; not proof of audio capture.
- **Demo data:** development-only fixture data; never represented as a real user recording in production.

No Accessibility Service misuse, hidden capture, root-only workaround, privileged permission assumption or restriction circumvention is allowed.

## Confirmed product and engineering direction

- Android-first native application: Kotlin, Jetpack Compose, Material 3.
- Android architecture: MVVM with Clean Architecture boundaries.
- Local persistence: Room.
- Local audio: app-private storage; encryption design remains to be validated.
- Backend: Spring Boot modular monolith, Java.
- Cloud metadata: PostgreSQL.
- Audio backup: private object storage; client-side encryption before upload is a requirement.
- Web companion: Lovable-generated React/TypeScript client using documented backend APIs.
- Local library and playback should work offline.
- Primary early physical-device target: OnePlus 10T 5G / Android 15.
- Current Android Studio project is a single Gradle module (`:app`) with package-based boundaries.

## Current implementation status

Verified from user-provided project files, successful build reports and emulator screenshots:
- Kotlin/Compose project builds and launches on Medium Phone API 37.0 emulator.
- CV Design System foundation and reusable components are implemented.
- Home screen and five-destination bottom navigation are implemented; Library, Insights, Backup and Settings remain placeholders.
- Room database foundation is integrated: recording entity, DAO, database, mapper, domain model, repository interface and implementation.
- KSP/Room build succeeds in the user's environment with `android.disallowKotlinSourceSets=false` in `gradle.properties`; this compatibility workaround should be revisited before toolchain upgrades.
- Android Storage Access Framework picker is connected to Home's Import Audio action.
- `AudioImportManager` copies selected MP3, M4A or WAV files into app-private `filesDir/recordings`, extracts duration metadata and returns an `IMPORTED` recording model.
- Imported recording metadata is saved through the Room repository and observed on Home.
- User successfully imported `sample-3s`; screenshot showed 1 audio item, 00:03 total duration and a 51 KB imported item.
- Import runs on `Dispatchers.IO`; cleanup is attempted if repository insertion fails.
- Recording compatibility UI remains informational and correctly does not claim call-audio capture.

Not yet verified:
- Playback, search, sort, rename, delete UI and Library destination.
- Playback, search, sort, rename, delete UI and Library destination.
- OnePlus 10T physical-device behavior.
- Automated unit/instrumentation tests and clean CI build.
- Permitted SIM/VoIP call-audio capture.
- Backend, AI processing, encrypted backup and web companion.

## Current delivery phase (26 November 2026)

- **Phase 1 — Android foundation and first UI: IN PROGRESS.** Design system, Home, navigation shell, Room foundation and first import flow are implemented; test baseline and physical-device verification remain.
- **Phase 2 — Audio import, library and playback: IN PROGRESS.** SAF import, Room metadata insertion and persistence after force-stop/relaunch are user-verified on API 37 emulator. Library management and playback are not implemented.

See `memory.md` and `phases.md` for evidence and outstanding acceptance checks.

## How to work with this repository

1. Read `memory.md`, `prd.md`, `architecture.md` and `trd.md` before architectural or feature work.
2. Read the relevant flow/design document before implementing a screen.
3. Do not infer capabilities from UI mockups or demo data.
4. Do not silently change an approved decision or dependency baseline.
5. Make small changes, build/test, and report exact results.
6. Update documentation in the same change when scope, architecture, dependencies, behavior or status changes.
7. Mark assumptions and open decisions explicitly.
8. Never mark a feature complete without its acceptance checks passing.

## Documentation governance — v1.2 (26 November 2026)

- Preserve user-approved scope unless the user approves a change.
- Record decisions with date, status, rationale and affected documents.
- Keep proposed backend schema separate from implemented database migrations.
- Keep visual inspiration separate from copied assets or protected layouts.
- Record actual build output and device tests; do not claim tests that were not run.
- A successful import and restart check demonstrate the flow on the API 37 emulator, not playback, physical-device compatibility or call recording.
