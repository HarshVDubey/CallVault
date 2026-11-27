
package dev.callvault.app.domain.model

enum class RecordingSource {
    IMPORTED,
    CALL_RECORDING
}

data class Recording(
    val id: String,
    val title: String,
    val filePath: String,
    val mimeType: String,
    val durationMillis: Long,
    val fileSizeBytes: Long,
    val source: RecordingSource,
    val importedAt: Long
)
