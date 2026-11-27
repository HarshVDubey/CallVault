# CallVault — Research-informed project baseline

**Baseline date:** 27 November 2026  
**Product:** CallVault  
**Direction:** Android-first hybrid — Capture, Manage and Understand are equally central

## Core documents

1. [`requirement-spec.md`](requirement-spec.md) — product direction, evidence rules, functional/non-functional requirements, acceptance criteria and release gates.
2. [`spec.md`](spec.md) — approved technology baseline, architecture, domain model, local audio lifecycle, AI processing, cloud/security boundaries and open technical decisions.
3. [`phases.md`](phases.md) — staged delivery roadmap and phase exit criteria.
4. [`task.md`](task.md) — implementation backlog with priorities, dependencies, acceptance criteria and traceability to research evidence.

## Research basis

The documents incorporate six user-provided competitor PDFs and `CallVault_Competitor_Intelligence_Matrix.xlsx` (45 evidence-register entries, 20 feature observations and 16 research-proposed CallVault backlog items).

## Baseline technical decisions retained

- Android: Kotlin, Jetpack Compose, MVVM/Clean Architecture, Room.
- Backend: Java, Spring Boot 3.x modular monolith, PostgreSQL.
- Web companion: React/TypeScript generated through Lovable.
- Local-first audio library; optional client-side encrypted cloud backup.
- Primary early compatibility test device: OnePlus 10T 5G / Android 15.

## Important constraints

CallVault must not promise universal cellular or third-party VoIP call recording. Call-state detection does not imply call-audio access. No Accessibility API misuse, hidden capture, root-only access, privileged audio-source assumption or sideloaded helper APK workaround is permitted. Call-source support requires a repeatable device/OS/source test and current policy review.

## Status discipline

These are planning documents. They do not assert that the Android project, recording, AI pipeline, backend, cloud backup or web dashboard has been implemented or tested. Audit the current repository before changing code or marking tasks complete.
