# CallVault — Interaction Patterns

**Version:** 1.0  
**Status:** Interaction specification; implementation pending.

## 1. Navigation

- Five primary destinations: Home, Library, Insights, Backup, Settings.
- Bottom navigation persists on primary destinations.
- Secondary screens use contextual back navigation.
- Navigation state should not reset active playback unexpectedly.
- Use clear selected-state indication and accessible labels.

## 2. Recording state

Recording controls are available only for a verified permitted capture path.

Required states:
- Unavailable/unsupported.
- Unverified.
- Ready after capability check.
- Disclosure/consent required.
- Recording active.
- Paused only if supported by capture adapter.
- Finalizing.
- Saved.
- Failed.

Rules:
- Show explicit visible recording state and stop action.
- Do not use a hold-to-talk gesture as a substitute for a call-recording flow.
- Do not show active state from call-state metadata alone.
- On failure, do not create a completed recording item.
- Explain source limitations.

## 3. Import

1. User selects Import.
2. Launch Android system picker.
3. User selects file.
4. Validate type, size and readability.
5. Copy/reference in app-private storage as designed.
6. Verify copy and metadata.
7. Create item with provenance `IMPORTED`.
8. Show success or actionable error.

Do not delete original source file as a side effect of import.

## 4. Playback

- Play/pause reflects actual player state.
- Seek reflects actual media timeline.
- Buffering and errors are visible.
- Mini-player appears only while a real item is active/paused according to agreed playback policy.
- Full Player opens from mini-player.
- Release media resources on lifecycle transitions.
- Playback speed/skip controls appear only after implementation.

## 5. Transcript

- Transcript is linked to source audio.
- Show timestamps only when returned/stored by provider.
- Speaker labels are estimates where diarization is used.
- Selecting a timestamp may seek audio only when timestamp mapping is available.
- Search results highlight matched text without altering source transcript.
- Distinguish original transcript from user edits and AI-generated summary.

## 6. AI processing disclosure

Before audio is sent to a cloud processor, show:
- Which recording will be processed.
- What data leaves the device.
- The processing provider/destination as required by product policy.
- Purpose of processing.
- Relevant retention/deletion information.
- Confirm and cancel actions.

Show queued/uploading/processing/completed/failed states based on actual job state. Allow cancellation when supported. Label generated content as AI-generated.

## 7. Summary presentation

Present:
- Overview.
- Key points.
- Decisions.
- Action items.
- Uncertainty or low-confidence caveats where relevant.

Do not invent speakers, commitments, dates or facts absent from source. Preserve links to source transcript/audio where possible.

## 8. Backup

- Off until user opts in.
- Explain encryption and key recovery before enabling.
- Show selected scope (all eligible or selected items).
- Encrypt locally before upload.
- Show queued/uploading/backed up/failed/restore states.
- Retry transient errors with bounded policy.
- Confirm destructive cloud deletion.
- Explain unrecoverability if keys are lost.

## 9. Delete/export

- Confirm destructive delete.
- Distinguish local deletion from cloud deletion.
- Show pending deletion until server/object deletion is confirmed.
- Export/share uses Android-supported user-mediated mechanisms.
- Do not expose permanent public object URLs.

## 10. Feedback, loading and errors

- Use concise status text and accessible icon/label.
- Progress must correspond to real work.
- Provide retry only for retryable errors.
- Preserve user input across recoverable errors.
- Handle offline state without implying local content is unavailable.
- Do not use decorative progress indicators to imply upload or transcription.

## 11. Accessibility and motion

- TalkBack labels for icon-only actions.
- Adequate touch targets and contrast.
- Respect text scaling.
- Status never conveyed by color alone.
- Motion is restrained and respects reduced-motion preferences.

## 12. Test cases

- Unsupported call source cannot start recording.
- Unverified source cannot be presented as supported.
- Import failure does not create a valid library item.
- Playback UI follows actual player callbacks.
- Transcript timestamp seeks only when mapping exists.
- AI upload cannot start without explicit confirmation.
- Backup cannot be shown as complete before server confirmation.
- Local and cloud delete statuses remain distinguishable.
