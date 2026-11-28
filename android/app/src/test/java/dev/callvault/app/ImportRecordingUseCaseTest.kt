package dev.callvault.app

import dev.callvault.app.domain.model.CaptureStatus
import dev.callvault.app.domain.model.Recording
import dev.callvault.app.domain.model.RecordingSource
import dev.callvault.app.domain.repository.RecordingRepository
import dev.callvault.app.domain.usecase.ImportRecordingUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.io.IOException

class ImportRecordingUseCaseTest {

    @Test
    fun successfulImport_persistsRecordingAndDoesNotDeleteFile() = runBlocking {
        val expectedRecording = sampleRecording()
        val repository = FakeRecordingRepository()
        val deletedPaths = mutableListOf<String>()
        var importedUri: String? = null

        val useCase = ImportRecordingUseCase(
            repository = repository,
            importAudio = { uri ->
                importedUri = uri
                expectedRecording
            },
            deleteImportedFile = { path ->
                deletedPaths.add(path)
            }
        )

        val result = useCase("content://media/external/audio/123")

        assertEquals("content://media/external/audio/123", importedUri)
        assertEquals(expectedRecording, result)
        assertEquals(RecordingSource.IMPORTED, result.source)
        assertEquals("Imported audio", result.sourceLabel)
        assertEquals(CaptureStatus.SAVED, result.status)
        assertEquals(1, repository.addRecordingCalls)
        assertEquals(listOf(expectedRecording), repository.addedRecordings)
        assertTrue(deletedPaths.isEmpty())
    }

    @Test
    fun importFailure_doesNotInsertIntoRepository() = runBlocking {
        val importException = IOException("Unsupported audio format.")
        val repository = FakeRecordingRepository()
        val deletedPaths = mutableListOf<String>()

        val useCase = ImportRecordingUseCase(
            repository = repository,
            importAudio = {
                throw importException
            },
            deleteImportedFile = { path ->
                deletedPaths.add(path)
            }
        )

        val thrown = try {
            useCase("content://media/external/audio/bad")
            fail("Expected import exception to be thrown.")
        } catch (exception: IOException) {
            exception
        }

        assertSame(importException, thrown)
        assertEquals(0, repository.addRecordingCalls)
        assertTrue(repository.addedRecordings.isEmpty())
        assertTrue(deletedPaths.isEmpty())
    }

    @Test
    fun repositoryFailure_deletesCopiedFileAndRethrowsOriginalException() = runBlocking {
        val importedRecording = sampleRecording()
        val persistException = IllegalStateException("Room insert failed.")
        val repository = FakeRecordingRepository(addError = persistException)
        val deletedPaths = mutableListOf<String>()

        val useCase = ImportRecordingUseCase(
            repository = repository,
            importAudio = { importedRecording },
            deleteImportedFile = { path ->
                deletedPaths.add(path)
            }
        )

        val thrown = try {
            useCase("content://media/external/audio/456")
            fail("Expected repository exception to be thrown.")
        } catch (exception: IllegalStateException) {
            exception
        }

        assertSame(persistException, thrown)
        assertEquals(1, repository.addRecordingCalls)
        assertTrue(repository.addedRecordings.isEmpty())
        assertEquals(listOf(importedRecording.filePath), deletedPaths)
    }

    @Test
    fun repositoryAndCleanupFailure_preservesOriginalExceptionWithSuppressedCleanupError() = runBlocking {
        val importedRecording = sampleRecording()
        val persistException = IllegalStateException("Room insert failed.")
        val cleanupException = IOException("Disk delete failed.")
        val repository = FakeRecordingRepository(addError = persistException)
        val deletedPaths = mutableListOf<String>()

        val useCase = ImportRecordingUseCase(
            repository = repository,
            importAudio = { importedRecording },
            deleteImportedFile = { path ->
                deletedPaths.add(path)
                throw cleanupException
            }
        )

        val thrown = try {
            useCase("content://media/external/audio/789")
            fail("Expected original repository exception to be thrown.")
        } catch (exception: IllegalStateException) {
            exception
        }

        assertSame(persistException, thrown)
        assertEquals(listOf(importedRecording.filePath), deletedPaths)
        assertEquals(1, persistException.suppressed.size)
        assertSame(cleanupException, persistException.suppressed[0])
    }

    private fun sampleRecording(): Recording = Recording(
        id = "rec-001",
        title = "sample-3s",
        filePath = "recordings/rec-001.mp3",
        mimeType = "audio/mpeg",
        durationMillis = 3000L,
        fileSizeBytes = 52224L,
        source = RecordingSource.IMPORTED,
        sourceLabel = "Imported audio",
        status = CaptureStatus.SAVED,
        createdAt = 1764240000000L,
        checksum = null
    )

    private class FakeRecordingRepository(
        private val addError: Throwable? = null
    ) : RecordingRepository {
        private val recordingsFlow = MutableStateFlow<List<Recording>>(emptyList())
        val addedRecordings = mutableListOf<Recording>()
        var addRecordingCalls: Int = 0
            private set

        override fun observeRecordings(): Flow<List<Recording>> = recordingsFlow

        override fun observeRecordingCount(): Flow<Int> = recordingsFlow.map { it.size }

        override suspend fun getRecording(id: String): Recording? =
            recordingsFlow.value.firstOrNull { it.id == id }

        override suspend fun addRecording(recording: Recording) {
            addRecordingCalls++
            if (addError != null) {
                throw addError
            }
            addedRecordings.add(recording)
            recordingsFlow.value = recordingsFlow.value + recording
        }

        override suspend fun updateRecording(recording: Recording) {
            recordingsFlow.value = recordingsFlow.value.map {
                if (it.id == recording.id) recording else it
            }
        }

        override suspend fun deleteRecording(id: String) {
            recordingsFlow.value = recordingsFlow.value.filterNot { it.id == id }
        }
    }
}
