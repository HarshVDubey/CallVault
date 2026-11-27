# CallVault — Android and Google Play Capture Policy Review

**Review date:** 27 November 2026  
**Scope:** Android audio capture, call audio, playback capture, microphone foreground services, user disclosure and Google Play policy.  
**Purpose:** T-003 evidence for feasibility decisions; this is an engineering/policy screening, not legal advice or a guarantee of Play approval.

## Executive decision

1. **Do not build a SIM-call audio capture adapter for the ordinary Play-distributed CallVault app using public third-party-app APIs.** Android documents voice-call audio capture as available to the call itself, an Accessibility service, or a privileged pre-installed app with CAPTURE_AUDIO_OUTPUT. Google Play's Accessibility policy explicitly prohibits using Accessibility APIs for remote call audio recording. CallVault is not a privileged pre-installed system app. Treat this capture route as Unsupported for the current product/distribution model.
2. **Do not claim universal third-party VoIP capture.** Android playback capture is conditional: the capturing app needs RECORD_AUDIO and user-approved MediaProjection, and the source app must expose eligible audio usages and allow capture. This is not a general permission to capture voice communications. Assess each source app/configuration separately; current CallVault status remains Unverified.
3. **A user-started microphone recorder is a technically viable separate feature**, subject to microphone permission, foreground-service requirements, visible user controls/notification, current Play declarations and user consent. It captures microphone input, not internal SIM/VoIP call audio. No such capture feature is implemented in CallVault yet.
4. **Continue SAF import.** User-selected file import is distinct from call recording and does not require broad all-files access for the current workflow.
5. **No Accessibility, root, privileged permission, hidden capture, or helper-APK workaround.** These are explicitly outside the acceptable design.

## Findings

### A. Cellular/SIM call audio

Android's audio-input sharing documentation describes a voice-call scenario: the call receives audio; an Accessibility service can capture it; and a privileged pre-installed app with CAPTURE_AUDIO_OUTPUT can capture the voice call. The documentation describes VOICE_UPLINK / VOICE_DOWNLINK sources for that privileged path.

Google Play's Accessibility policy states that the Accessibility API is not designed for and cannot be requested for remote call audio recording, and prohibits using Accessibility to work around Android security/privacy controls. Therefore, the Accessibility route is not acceptable for CallVault's Play-distributed product. The privileged pre-installed route is not available to an ordinary Play-installed app.

**Decision:** SIM-call internal audio capture through ordinary third-party-app public APIs is Unsupported for the current CallVault distribution model. Do not add call-recording controls that imply it works. Revisit only if Android/OEM offers a documented, permitted, user-accessible export/integration path, and assess that path separately.

### B. Third-party VoIP audio

Android Audio Playback Capture is not a universal audio tap. The capturing app must have RECORD_AUDIO, obtain user approval through MediaProjection, and capture from the same user profile. The source app's player must use an eligible audio usage (USAGE_MEDIA, USAGE_GAME or USAGE_UNKNOWN) and its effective capture policy must permit capture. The most restrictive capture policy applies.

These conditions do not establish that WhatsApp or another VoIP app exposes call audio. Voice-communication audio may not meet the eligible usage conditions, and the source app can restrict capture. Source-app version, OEM, Android version and route must be tested individually.

**Decision:** third-party VoIP call audio remains Unverified per source/configuration. Do not use Accessibility or any other API to bypass source-app or platform restrictions.

### C. Microphone capture as a distinct capability

Android's microphone foreground-service type is intended for continued microphone capture, including voice recorders. For apps targeting Android 14/API 34 or later, declare the microphone service type and FOREGROUND_SERVICE_MICROPHONE permission, and obtain RECORD_AUDIO. RECORD_AUDIO is a while-in-use permission: a microphone foreground service generally must be created while the app is visible/eligible, not started arbitrarily from the background. Android 15 also restricts starting microphone foreground services from BOOT_COMPLETED.

Google Play requires foreground services to support a user-beneficial core feature, be user-initiated or user-perceptible, provide a clear notification/stop mechanism, and run only as long as necessary. For Android 14+ targets, the relevant FGS type must also be declared in Play Console.

**Decision:** a visible, user-started microphone recorder may be evaluated as a separate feature. It must not be described as SIM/VoIP call recording. Before implementation, define the UX, notification, stop behavior, permission rationale and device tests.

### D. Disclosure, consent and user data

Google Play's User Data policy requires prominent in-app disclosure before consent/permission requests when collection or use may not be reasonably expected. The disclosure must explain what data is accessed/collected, how it is used/shared, and obtain affirmative user action. A privacy policy alone is not a substitute.

The Play Data Safety section and in-app privacy policy must accurately reflect app and SDK data practices. Cloud AI processing and backup therefore need point-of-use disclosure, explicit user action, clear destination/retention information and deletion controls before those features ship.

### E. File import and broad storage permissions

Google Play's sensitive-permissions guidance says MANAGE_EXTERNAL_STORAGE should be requested only when platform alternatives such as the Storage Access Framework are insufficient for the app's core functionality. CallVault's existing SAF-selected import flow should remain the default; do not add all-files access for convenience.

## Engineering decisions for CallVault

| Capability | Decision | Implementation consequence |
|---|---|---|
| SIM-call internal audio capture by ordinary Play app | Unsupported for current distribution model | Do not implement an adapter or advertise support based on public APIs; no Accessibility workaround. |
| Third-party VoIP call audio | Unverified per source/device/OS | No generic capture feature; test only if a source explicitly permits a compliant path. |
| Microphone / in-person recorder | Potentially viable, not implemented | Separate feature and domain state; visible user start/stop; microphone FGS and permissions only after UX and policy review. |
| User-selected local audio import | Continue | SAF; label as Imported; no broad storage permission. |
| OEM/system-created recording file | Import-only path may be evaluated | User explicitly selects an accessible file; resulting item remains Imported. |
| Cloud AI / backup | Not in current implementation | Explicit opt-in, disclosure, secure transport; backup must encrypt client-side before upload. |

## Required follow-up

- Update the compatibility matrix with the decisions above.
- Keep SIM and VoIP statuses separate.
- Define whether microphone recording belongs in the MVP before adding it to scope.
- Re-check official Android and Play policy pages immediately before implementation and Play submission; policy pages can change.
- Obtain jurisdiction-specific legal review before enabling recording in any market.

## Official sources reviewed

- Android — Sharing audio input: https://developer.android.com/media/platform/sharing-audio-input
- Android — Capture video and audio playback: https://developer.android.com/media/platform/av-capture
- Android — Foreground service types: https://developer.android.com/develop/background-work/services/fgs/service-types
- Android — Restrictions on starting a foreground service from the background: https://developer.android.com/develop/background-work/services/fgs/restrictions-bg-start
- Android — Behavior changes for apps targeting Android 15: https://developer.android.com/about/versions/15/behavior-changes-15
- Google Play — Permissions and APIs that access sensitive information / Accessibility API: https://support.google.com/googleplay/android-developer/answer/16909972
- Google Play — User Data, prominent disclosure and consent: https://support.google.com/googleplay/android-developer/answer/10144311
- Google Play — Foreground service requirements: https://support.google.com/googleplay/android-developer/answer/16559646
- Google Play — Restricted permissions and minimum-scope alternatives: https://support.google.com/googleplay/android-developer/answer/16558241

## Limitations

This review does not certify legal compliance in every country, establish the behavior of any specific OEM firmware or VoIP app, or guarantee Google Play approval. It is a current engineering screening based on the official documentation linked above. Exact target SDK and policy declarations must be checked again before release.
