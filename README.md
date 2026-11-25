# CallVault — Project Documentation

**Project:** CallVault  
**Product direction:** Android-first, hybrid architecture  
**Primary device for early compatibility testing:** OnePlus 10T 5G, Android 15  
**Document baseline:** 25 November 2026  
**Status:** Planning / foundation

## Document index

| Document | Purpose |
|---|---|
| [prd.md](prd.md) | Product requirements, users, scope, success criteria |
| [phases.md](phases.md) | Delivery phases, milestones, acceptance gates |
| [architecture.md](architecture.md) | System architecture and component boundaries |
| [trd.md](trd.md) | Technical requirements and implementation standards |
| [appflow.md](appflow.md) | Screen map and key user journeys |
| [ui-ux-design-brief.md](ui-ux-design-brief.md) | Visual design and interaction brief |
| [backend-schema.md](backend-schema.md) | Backend domain model, PostgreSQL schema proposal |
| [memory.md](memory.md) | Durable project decisions, constraints and current status |

## Product statement

CallVault is a privacy-first Android application for managing audio recordings, with transcription, AI-generated summaries, search and optional encrypted cloud backup. A companion web dashboard may be built with Lovable, while the primary product and native device integration remain in Kotlin/Android.

## Critical product constraint

CallVault must not claim that it can universally record cellular SIM calls or third-party VoIP calls. Android restricts capture of internal call audio, and third-party apps such as WhatsApp control their own audio capture policies. Recording capability must be validated on each supported device/OS combination and implemented only through legitimate, policy-compliant mechanisms. No Accessibility Service, hidden capture, root-only workaround, or permission circumvention may be used.

The app must distinguish:
- **Supported recording:** audio capture has been verified for the specific device, OS, call type and app.
- **Unsupported recording:** the platform or third-party app does not expose a permitted audio path.
- **Imported audio:** a file selected by the user, not a recording made by CallVault.
- **Call metadata:** call-state or user-entered information; this is not proof that audio was recorded.

## Baseline decisions

- Native Android app: Kotlin, Jetpack Compose, MVVM/Clean Architecture.
- Local persistence: Room.
- Local audio: app-private storage, encrypted where feasible using Android Keystore-backed key management.
- Background work: WorkManager for deferred upload/transcription tasks, not for bypassing background execution restrictions.
- Backend: Spring Boot modular monolith initially.
- Cloud metadata: PostgreSQL.
- Audio backup: private object storage, encrypted before upload.
- Web companion: Lovable-generated React application, treated as a separate client of documented backend APIs.
- Android-first: validate native recording feasibility before building costly cloud/AI features.

## How to use these documents

These documents are the initial source of truth. Update `memory.md` whenever a major decision changes. Update the PRD and architecture when scope or system boundaries change. Record all unresolved decisions explicitly rather than silently assuming an answer.
