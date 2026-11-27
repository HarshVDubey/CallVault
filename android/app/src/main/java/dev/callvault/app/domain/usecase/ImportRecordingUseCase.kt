package dev.callvault.app.domain.usecase

import dev.callvault.app.domain.model.Recording
import dev.callvault.app.domain.repository.RecordingRepository
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

/**
 * Coordinates importing an audio selection and persisting its metadata,
 * rolling back the copied file if repository persistence fails.
 */
class ImportRecordingUseCase(
    private val repository: RecordingRepository,
    private val importAudio: suspend (sourceUri: String) -> Recording,
    private val deleteImportedFile: suspend (relativePath: String) -> Unit
) {

    suspend operator fun invoke(sourceUri: String): Recording {
        val recording = importAudio(sourceUri)
        return try {
            repository.addRecording(recording)
            recording
        } catch (persistError: Throwable) {
            withContext(NonCancellable) {
                try {
                    deleteImportedFile(recording.filePath)
                } catch (cleanupError: Throwable) {
                    if (cleanupError !== persistError) {
                        persistError.addSuppressed(cleanupError)
                    }
                }
            }
            throw persistError
        }
    }
}
