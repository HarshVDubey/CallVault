
package dev.callvault.app.data.repository

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.OpenableColumns
import dev.callvault.app.domain.model.Recording
import dev.callvault.app.domain.model.RecordingSource
import java.io.File
import java.io.IOException
import java.util.UUID

class AudioImportManager(
    private val context: Context
) {
    private val supportedExtensions = setOf("mp3", "m4a", "wav")

    fun importAudio(uri: Uri): Recording {
        val resolver = context.contentResolver

        val originalName = queryDisplayName(uri)
            ?: throw IOException("Unable to read the selected file name.")

        val extension = originalName
            .substringAfterLast('.', "")
            .lowercase()

        if (extension !in supportedExtensions) {
            throw IOException("Unsupported audio format. Select MP3, M4A or WAV.")
        }

        val mimeType = resolver.getType(uri)
            ?: when (extension) {
                "mp3" -> "audio/mpeg"
                "m4a" -> "audio/mp4"
                "wav" -> "audio/wav"
                else -> "application/octet-stream"
            }

        val recordingsDirectory = File(context.filesDir, "recordings")

        if (!recordingsDirectory.exists() && !recordingsDirectory.mkdirs()) {
            throw IOException("Could not create CallVault storage.")
        }

        val id = UUID.randomUUID().toString()
        val storedFileName = "$id.$extension"
        val destination = File(recordingsDirectory, storedFileName)

        try {
            val input = resolver.openInputStream(uri)
                ?: throw IOException("Unable to open the selected audio file.")

            input.use { source ->
                destination.outputStream().use { output ->
                    source.copyTo(output)
                }
            }

            if (!destination.exists() || destination.length() == 0L) {
                throw IOException("The selected audio file is empty or unreadable.")
            }

            val duration = readDuration(destination)

            return Recording(
                id = id,
                title = originalName.substringBeforeLast('.'),
                filePath = "recordings/$storedFileName",
                mimeType = mimeType,
                durationMillis = duration,
                fileSizeBytes = destination.length(),
                source = RecordingSource.IMPORTED,
                importedAt = System.currentTimeMillis()
            )
        } catch (exception: Exception) {
            destination.delete()
            throw exception
        }
    }

    private fun queryDisplayName(uri: Uri): String? {
        context.contentResolver.query(
            uri,
            arrayOf(OpenableColumns.DISPLAY_NAME),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0) {
                    return cursor.getString(index)
                }
            }
        }

        return null
    }

    private fun readDuration(file: File): Long {
        val retriever = MediaMetadataRetriever()

        return try {
            retriever.setDataSource(file.absolutePath)

            retriever.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_DURATION
            )?.toLongOrNull() ?: 0L
        } finally {
            retriever.release()
        }
    }

    fun deleteImportedFile(relativePath: String) {
        val file = File(context.filesDir, relativePath)

        // Only delete files inside CallVault's recordings directory.
        val recordingsDirectory = File(context.filesDir, "recordings")
            .canonicalFile
        val targetFile = file.canonicalFile

        if (targetFile.parentFile == recordingsDirectory) {
            targetFile.delete()
        }
    }

}
