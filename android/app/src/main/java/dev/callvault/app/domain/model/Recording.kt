package dev.callvault.app.domain.model

enum class RecordingSource {
    IMPORTED,
    MICROPHONE,
    VERIFIED_CALL_SOURCE,
    DEMO,
    LEGACY_UNVERIFIED
}

enum class CaptureStatus {
    IDLE,
    RECORDING,
    PAUSED,
    INTERRUPTED,
    FINALIZING,
    SAVED,
    FAILED;

    val isPersistable: Boolean
        get() = this != IDLE

    val isTerminal: Boolean
        get() = this == SAVED || this == FAILED

    fun canTransitionForCapture(next: CaptureStatus): Boolean = when (this) {
        IDLE -> next == RECORDING
        RECORDING -> next == PAUSED || next == INTERRUPTED || next == FINALIZING || next == FAILED
        PAUSED -> next == RECORDING || next == INTERRUPTED || next == FINALIZING || next == FAILED
        INTERRUPTED -> next == FINALIZING || next == FAILED
        FINALIZING -> next == SAVED || next == FAILED
        SAVED, FAILED -> false
    }

    fun canTransitionForImport(next: CaptureStatus): Boolean = when (this) {
        IDLE -> next == FINALIZING
        FINALIZING -> next == SAVED || next == FAILED
        RECORDING, PAUSED, INTERRUPTED, SAVED, FAILED -> false
    }

    fun transitionTo(next: CaptureStatus, isImport: Boolean): CaptureStatus {
        val allowed = if (isImport) {
            canTransitionForImport(next)
        } else {
            canTransitionForCapture(next)
        }
        if (!allowed) {
            val flow = if (isImport) "import" else "capture"
            throw IllegalStateException("Invalid $flow status transition from $this to $next.")
        }
        return next
    }
}

data class Recording(
    val id: String,
    val title: String,
    val filePath: String,
    val mimeType: String,
    val durationMillis: Long,
    val fileSizeBytes: Long,
    val source: RecordingSource,
    val sourceLabel: String,
    val status: CaptureStatus,
    val createdAt: Long,
    val checksum: String? = null,
    val rawSourceType: String? = null,
    val rawStatus: String? = null
) {
    val hasUnrecognizedPersistedValues: Boolean
        get() = rawSourceType != null || rawStatus != null

    fun transitionStatus(next: CaptureStatus): Recording {
        if (hasUnrecognizedPersistedValues) {
            throw IllegalStateException(
                "Cannot transition status for recording '$id' with unrecognized persisted metadata."
            )
        }
        val isImport = source == RecordingSource.IMPORTED
        return copy(status = status.transitionTo(next, isImport = isImport))
    }
}
