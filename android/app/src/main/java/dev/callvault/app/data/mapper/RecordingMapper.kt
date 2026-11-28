package dev.callvault.app.data.mapper

import dev.callvault.app.data.local.entity.RecordingEntity
import dev.callvault.app.domain.model.CaptureStatus
import dev.callvault.app.domain.model.Recording
import dev.callvault.app.domain.model.RecordingSource

private const val LEGACY_CALL_RECORDING = "CALL_RECORDING"
private const val LEGACY_UNVERIFIED_LABEL = "Unverified legacy source"
private const val UNRECOGNIZED_SOURCE_LABEL = "Unrecognized audio source"

internal fun isRecognizedSourceType(rawSourceType: String): Boolean {
    return rawSourceType == LEGACY_CALL_RECORDING ||
        RecordingSource.entries.any { it.name == rawSourceType }
}

internal fun isRecognizedPersistableStatus(rawStatus: String): Boolean {
    return CaptureStatus.entries.any { it.name == rawStatus && it.isPersistable }
}

fun RecordingEntity.toDomain(): Recording {
    val isLegacyCallRecording = sourceType == LEGACY_CALL_RECORDING
    val matchedSource = if (isLegacyCallRecording) {
        RecordingSource.LEGACY_UNVERIFIED
    } else {
        RecordingSource.entries.firstOrNull { it.name == sourceType }
    }
    val hasUnknownSource = matchedSource == null
    val decodedSource = matchedSource ?: RecordingSource.LEGACY_UNVERIFIED
    val preservedRawSource = if (hasUnknownSource) sourceType else null

    val matchedStatus = CaptureStatus.entries.firstOrNull {
        it.name == status && it.isPersistable
    }
    val hasUnknownStatus = matchedStatus == null

    val decodedStatus: CaptureStatus
    val preservedRawStatus: String?
    val decodedLabel: String

    when {
        isLegacyCallRecording -> {
            decodedStatus = CaptureStatus.FAILED
            preservedRawStatus = if (hasUnknownStatus) status else null
            decodedLabel = LEGACY_UNVERIFIED_LABEL
        }

        hasUnknownSource -> {
            decodedStatus = CaptureStatus.FAILED
            preservedRawStatus = status
            decodedLabel = sourceLabel.ifBlank { UNRECOGNIZED_SOURCE_LABEL }
        }

        decodedSource == RecordingSource.LEGACY_UNVERIFIED -> {
            decodedStatus = CaptureStatus.FAILED
            preservedRawStatus = if (hasUnknownStatus) status else null
            decodedLabel = sourceLabel.ifBlank { LEGACY_UNVERIFIED_LABEL }
        }

        else -> {
            decodedStatus = matchedStatus ?: CaptureStatus.FAILED
            preservedRawStatus = if (hasUnknownStatus) status else null
            decodedLabel = sourceLabel.ifBlank { defaultLabelFor(decodedSource) }
        }
    }

    return Recording(
        id = id,
        title = title,
        filePath = filePath,
        mimeType = mimeType,
        durationMillis = durationMillis,
        fileSizeBytes = fileSizeBytes,
        source = decodedSource,
        sourceLabel = decodedLabel,
        status = decodedStatus,
        createdAt = importedAt,
        checksum = checksum,
        rawSourceType = preservedRawSource,
        rawStatus = preservedRawStatus
    )
}

fun Recording.toEntity(): RecordingEntity {
    if (rawSourceType != null && source != RecordingSource.LEGACY_UNVERIFIED) {
        throw IllegalStateException(
            "Cannot promote unrecognized persisted source '$rawSourceType' to $source."
        )
    }
    if (rawStatus != null && status != CaptureStatus.FAILED) {
        throw IllegalStateException(
            "Cannot alter unrecognized or fail-closed persisted status '$rawStatus' to $status."
        )
    }

    return RecordingEntity(
        id = id,
        title = title,
        filePath = filePath,
        mimeType = mimeType,
        durationMillis = durationMillis,
        fileSizeBytes = fileSizeBytes,
        sourceType = rawSourceType ?: source.name,
        sourceLabel = sourceLabel,
        status = rawStatus ?: status.name,
        importedAt = createdAt,
        checksum = checksum
    )
}

private fun defaultLabelFor(source: RecordingSource): String = when (source) {
    RecordingSource.IMPORTED -> "Imported audio"
    RecordingSource.MICROPHONE -> "Microphone"
    RecordingSource.VERIFIED_CALL_SOURCE -> "Verified call source"
    RecordingSource.DEMO -> "Demo fixture"
    RecordingSource.LEGACY_UNVERIFIED -> LEGACY_UNVERIFIED_LABEL
}
