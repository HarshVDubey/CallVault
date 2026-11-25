# CallVault

**Product:** Privacy-first Android audio library and conversation assistant  
**Distribution goal:** Google Play Store  
**Product direction:** Android-first hybrid  
**Primary compatibility device:** OnePlus 10T 5G, Android 15  
**Documentation baseline:** 25 November 2026  
**Documentation version:** 1.1  
**Project status:** Android foundation created; product implementation not yet complete

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
| [docs/design/interaction-patterns.md](docs/design/interaction-patterns.md) | Cross-screen interaction and state patterns |

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
- Local persistence: Room (planned; not yet integrated).
- Local audio: app-private storage; encryption design to be validated.
- Deferred work: WorkManager for eligible backup/transcription jobs, not continuous covert capture.
- Backend: Spring Boot modular monolith, Java.
- Cloud metadata: PostgreSQL.
- Audio backup: private object storage; client-side encryption before upload is a requirement.
- Web companion: Lovable-generated React/TypeScript client using documented backend APIs.
- Local library and playback should work offline.
- Primary early physical-device target: OnePlus 10T 5G / Android 15.
- Current Android Studio project is a single Gradle module (`:app`) with package-based boundaries.

## Current implementation status

Verified from the user's Android Studio screenshots and supplied build files:
- Git repository opened in Android Studio.
- `android/` project directory exists inside the repository working tree.
- Kotlin/Compose Android project generated.
- Gradle sync/build completed and the starter app launched in an API 37 emulator.
- Initial package folders were created for core design system, data, domain and features.
- Current starter screen is still the generated Android greeting; CallVault screens and functional recording features are not implemented.
- OnePlus 10T physical-device testing is pending.

See `memory.md` for the detailed verified baseline and outstanding work.

## How to work with this repository

1. Read `memory.md`, `prd.md`, `architecture.md` and `trd.md` before architectural or feature work.
2. Read the relevant flow/design document before implementing a screen.
3. Do not infer capabilities from UI mockups or demo data.
4. Do not silently change an approved decision or dependency baseline.
5. Make small changes, build/test, and report exact results.
6. Update documentation in the same change when scope, architecture, dependencies, behavior or status changes.
7. Mark assumptions and open decisions explicitly.
8. Never mark a feature complete without its acceptance checks passing.

## Documentation change policy

- Preserve user-approved scope unless the user approves a change.
- Record decisions with date, status, rationale and affected documents.
- Keep proposed backend schema separate from implemented database migrations.
- Keep visual inspiration separate from copied assets or protected layouts.
- Record actual build output and device tests; do not claim tests that were not run.


---


## Documentation governance — v1.1 (25 November 2026)

### Verified project baseline
- Repository: `HarshVDubey/CallVault`, default branch `main`.
- Android project lives under `android/` inside this repository.
- Current Android build is a single Gradle module: `:app`.
- User-provided configuration: Gradle 9.6.0; AGP 9.5.3; Kotlin 2.2.10; Compose BOM 2026.02.01; compileSdk 37; targetSdk 37; minSdk 26; Java source/target compatibility 11; namespace/applicationId `dev.callvault.app`.
- User showed successful initial Gradle sync/build and starter-app launch on the Medium Phone API 37.0 emulator.
- OnePlus 10T 5G / Android 15 remains the primary physical-device target; it has not yet been tested.
- Package folders exist under the app source tree, but folder creation is not feature implementation.

### Design decisions
Premium dark visual direction, violet accent, and five-item bottom navigation are user-approved. The primary destinations are Home, Library, Insights, Backup and Settings. Individual color values and dimensions are proposed tokens pending emulator and physical-device validation.

### Hallucination-prevention protocol
For every implementation or status claim:
1. Cite the exact source document, code path, test or observed device result.
2. Mark it Confirmed, Proposed, Unverified or Blocked.
3. Do not infer implementation from a folder, UI mockup, documentation entry or generated sample.
4. Do not call a feature complete until its acceptance criteria and tests pass.
5. Keep the implementation status in `memory.md` current.
6. If a document is unavailable or a fact is not established, say so instead of filling the gap.

### v1.1 change summary
Reconciles the observed Android project baseline, package-based single-module architecture, Mobbin research supplied by the user, approved visual direction, Home-first implementation milestone, and explicit documentation/implementation status boundaries.

