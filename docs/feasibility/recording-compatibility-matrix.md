# CallVault — Recording Source Compatibility Matrix

**Baseline:** 27 November 2026  
**Purpose:** Track recording feasibility by exact device, Android version, source, route and capture mechanism.  
**Rule:** A source is not Supported until the mechanism is lawful, policy-reviewed and repeatably verified on the documented configuration. Call-state detection, an active-call notification or a successful import does not prove call-audio capture.

## Status definitions

- **Verified:** repeatable test evidence exists for the exact configuration and the proposed mechanism has passed current legal/platform/store-policy review.
- **Unsupported:** the documented configuration does not expose a permitted, usable capture path, or the proposed mechanism is prohibited.
- **Unverified:** not yet tested, evidence is incomplete, or policy review is pending.
- **Not implemented:** the app does not yet contain the relevant capture adapter. This is an implementation status, not a platform capability verdict.

## Device/source matrix

| Device / OS | Source | Proposed mechanism | Audio route / conditions | Status | Evidence / next action |
|---|---|---|---|---|---|
| Medium Phone emulator / API 37.0 | User-selected MP3 import | Android Storage Access Framework; copy to app-private storage; persist Room metadata | Local file picker; sample-3s, 3 seconds, 51 KB | Verified — import only | User demonstrated import and persistence after force-stop/relaunch. This does **not** verify recording or playback. |
| Medium Phone emulator / API 37.0 | Cellular SIM call | No ordinary third-party-app mechanism approved | Earpiece, speaker, wired and Bluetooth not tested | Unsupported for current ordinary Play-app model | Android audio-input sharing reserves call audio to the call, Accessibility or privileged pre-installed apps; Google Play prohibits Accessibility use for remote call recording. OEM-specific export paths are separate and unverified. |
| OnePlus 10T 5G / Android 15 | Cellular SIM call | No ordinary third-party-app mechanism approved | Earpiece, speaker, wired and Bluetooth not tested | Unsupported for current ordinary Play-app model; OEM path unverified | Same Android/Play restriction applies to ordinary app APIs. Physical device is not tested; do not infer OEM behavior. |
| Medium Phone emulator / API 37.0 | Third-party VoIP call (e.g. WhatsApp) | No mechanism approved | App/source and audio routes not tested | Unverified; capture adapter not implemented | Review Android playback-capture restrictions and source-app behavior; do not infer access from call state. |
| OnePlus 10T 5G / Android 15 | Third-party VoIP call (e.g. WhatsApp) | No mechanism approved | App/source and audio routes not tested | Unverified; device not tested | Requires exact source-app version, route matrix, repeatable evidence and policy review. |
| Medium Phone emulator / API 37.0 | Microphone / in-person conversation | User-started microphone foreground service (proposed only) | Microphone permission, foreground behavior and interruptions not tested | Unverified; capture adapter not implemented | Technically viable subject to RECORD_AUDIO, microphone FGS requirements, user-visible controls and Play review; separate from call audio. |
| OnePlus 10T 5G / Android 15 | Microphone / in-person conversation | User-started microphone foreground service (proposed only) | Microphone permission, foreground behavior and interruptions not tested | Unverified; capture adapter not implemented and device not tested | Physical test required after implementation; Android 15 background-start restrictions apply. |
| OEM/system-created recording file | User-selected import | SAF import of a file made available by the device/OEM | Exact OEM path and file formats not tested | Unverified beyond the tested MP3 import | Test only files the user can explicitly select and access; label resulting item Imported, not CallVault-recorded. |

## Required test record for every future row

Record all of the following before changing a status:
1. Device manufacturer/model and exact Android build/version.
2. Source type and exact source app/version where applicable.
3. Capture mechanism and relevant manifest permissions/foreground-service type.
4. Audio route: earpiece, speaker, wired headset and Bluetooth, as applicable.
5. Whether each participant/channel is audible; note silent, one-sided or distorted output.
6. Screen-off, app switching, incoming interruption, audio-focus conflict, stop and process-death behavior.
7. Output file existence, size, duration, reopenability and metadata validation.
8. Test date, repeat count, reproducible steps and evidence location.
9. Current Android platform and Google Play policy review reference/date.
10. Final status and known limitations.

## Guardrails

- Never use Accessibility Services to obtain call audio or bypass restrictions.
- Never rely on root, privileged-only permissions, hidden capture, a sideloaded helper APK or third-party app circumvention.
- Call-state observation is metadata only; it does not grant audio access.
- Do not mark SIM or VoIP support from a single successful call or from imported audio.
- Do not claim both sides are captured without listening to and validating both channels.
- If no permitted path is demonstrated, leave the source Unsupported or Unverified and keep the product useful through import, local management and playback.

## Current conclusion

No third-party VoIP call-audio capture path is verified for CallVault. Ordinary SIM-call internal audio capture is Unsupported for the current ordinary Play-distributed app model: the documented Accessibility route is prohibited for remote call recording by Google Play, and the privileged pre-installed route is not available to CallVault. Any OEM-generated file the user can select remains an import, not a CallVault recording. A user-started microphone recorder is technically plausible but not implemented or tested. The only verified media path recorded here is user-mediated MP3 import on the API 37 emulator. The OnePlus 10T / Android 15 target has not yet been physically tested. No call-audio recording capability is claimed.
