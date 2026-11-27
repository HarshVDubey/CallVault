
package dev.callvault.app.data.mapper

import dev.callvault.app.data.local.entity.RecordingEntity
import dev.callvault.app.domain.model.Recording
import dev.callvault.app.domain.model.RecordingSource

fun RecordingEntity.toDomain(): Recording {
    return Recording(
        id = id,
        title = title,
        filePath = filePath,
        mimeType = mimeType,
        durationMillis = durationMillis,
        fileSizeBytes = fileSizeBytes,
        source = RecordingSource.valueOf(sourceType),
        importedAt = importedAt
    )
}

fun Recording.toEntity(): RecordingEntity {
    return RecordingEntity(
        id = id,
        title = title,
        filePath = filePath,
        mimeType = mimeType,
        durationMillis = durationMillis,
        fileSizeBytes = fileSizeBytes,
        sourceType = source.name,
        importedAt = importedAt
    )
}
