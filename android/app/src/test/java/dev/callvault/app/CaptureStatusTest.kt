package dev.callvault.app

import dev.callvault.app.domain.model.CaptureStatus
import dev.callvault.app.domain.model.Recording
import dev.callvault.app.domain.model.RecordingSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class CaptureStatusTest {

    @Test
    fun safImportTransitions_followExpectedLifecycle() {
        assertEquals(
            CaptureStatus.FINALIZING,
            CaptureStatus.IDLE.transitionTo(CaptureStatus.FINALIZING, isImport = true)
        )
        assertEquals(
            CaptureStatus.SAVED,
            CaptureStatus.FINALIZING.transitionTo(CaptureStatus.SAVED, isImport = true)
        )
        assertEquals(
            CaptureStatus.FAILED,
            CaptureStatus.FINALIZING.transitionTo(CaptureStatus.FAILED, isImport = true)
        )
        assertFalse(CaptureStatus.IDLE.canTransitionForImport(CaptureStatus.RECORDING))
        assertFalse(CaptureStatus.IDLE.canTransitionForImport(CaptureStatus.SAVED))
    }

    @Test
    fun captureTransitions_enforceTerminalStopAndTerminalEndStates() {
        assertEquals(
            CaptureStatus.RECORDING,
            CaptureStatus.IDLE.transitionTo(CaptureStatus.RECORDING, isImport = false)
        )
        assertEquals(
            CaptureStatus.PAUSED,
            CaptureStatus.RECORDING.transitionTo(CaptureStatus.PAUSED, isImport = false)
        )
        assertEquals(
            CaptureStatus.RECORDING,
            CaptureStatus.PAUSED.transitionTo(CaptureStatus.RECORDING, isImport = false)
        )
        assertEquals(
            CaptureStatus.INTERRUPTED,
            CaptureStatus.RECORDING.transitionTo(CaptureStatus.INTERRUPTED, isImport = false)
        )
        assertEquals(
            CaptureStatus.FINALIZING,
            CaptureStatus.INTERRUPTED.transitionTo(CaptureStatus.FINALIZING, isImport = false)
        )
        assertEquals(
            CaptureStatus.SAVED,
            CaptureStatus.FINALIZING.transitionTo(CaptureStatus.SAVED, isImport = false)
        )

        // Stop is terminal: FINALIZING cannot revert to RECORDING, PAUSED, or IDLE.
        assertFalse(CaptureStatus.FINALIZING.canTransitionForCapture(CaptureStatus.RECORDING))
        assertFalse(CaptureStatus.FINALIZING.canTransitionForCapture(CaptureStatus.PAUSED))
        assertFalse(CaptureStatus.FINALIZING.canTransitionForCapture(CaptureStatus.IDLE))
        assertFalse(CaptureStatus.INTERRUPTED.canTransitionForCapture(CaptureStatus.RECORDING))

        // SAVED and FAILED are terminal for all flows.
        assertTrue(CaptureStatus.SAVED.isTerminal)
        assertTrue(CaptureStatus.FAILED.isTerminal)
        assertFalse(CaptureStatus.IDLE.isPersistable)
        CaptureStatus.entries.forEach { target ->
            assertFalse(CaptureStatus.SAVED.canTransitionForCapture(target))
            assertFalse(CaptureStatus.SAVED.canTransitionForImport(target))
            assertFalse(CaptureStatus.FAILED.canTransitionForCapture(target))
            assertFalse(CaptureStatus.FAILED.canTransitionForImport(target))
        }
    }

    @Test
    fun recordingTransitionStatus_rejectsInvalidAndUnrecognizedTransitions() {
        val finalizingImport = Recording(
            id = "rec-1",
            title = "Import",
            filePath = "recordings/rec-1.mp3",
            mimeType = "audio/mpeg",
            durationMillis = 1000L,
            fileSizeBytes = 1024L,
            source = RecordingSource.IMPORTED,
            sourceLabel = "Imported audio",
            status = CaptureStatus.FINALIZING,
            createdAt = 1000L
        )
        val saved = finalizingImport.transitionStatus(CaptureStatus.SAVED)
        assertEquals(CaptureStatus.SAVED, saved.status)

        try {
            saved.transitionStatus(CaptureStatus.FINALIZING)
            fail("Expected transition out of SAVED to throw.")
        } catch (_: IllegalStateException) {
        }

        val unknownPersisted = finalizingImport.copy(
            source = RecordingSource.LEGACY_UNVERIFIED,
            status = CaptureStatus.FAILED,
            rawSourceType = "FUTURE_SOURCE",
            rawStatus = "FUTURE_STATUS"
        )
        try {
            unknownPersisted.transitionStatus(CaptureStatus.SAVED)
            fail("Expected transition on unrecognized persisted metadata to throw.")
        } catch (_: IllegalStateException) {
        }
    }
}
