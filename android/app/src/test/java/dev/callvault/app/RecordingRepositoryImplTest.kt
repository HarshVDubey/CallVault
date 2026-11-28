package dev.callvault.app

import dev.callvault.app.data.local.dao.RecordingDao
import dev.callvault.app.data.local.entity.RecordingEntity
import dev.callvault.app.data.repository.RecordingRepositoryImpl
import dev.callvault.app.domain.model.CaptureStatus
import dev.callvault.app.domain.model.Recording
import dev.callvault.app.domain.model.RecordingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class RecordingRepositoryImplTest {

    @Test
    fun unknownPersistedValues_surviveTitleUpdateAndRejectOverwrite() = runBlocking {
        val dao = InMemoryRecordingDao()
        val rawEntity = RecordingEntity(
            id = "rec-future",
            title = "Original future title",
            filePath = "recordings/future.mp3",
            mimeType = "audio/mpeg",
            durationMillis = 3000L,
            fileSizeBytes = 12345L,
            sourceType = "FUTURE_SOURCE_X",
            sourceLabel = "Future capture",
            status = "FUTURE_STATUS_Y",
            importedAt = 1764240000000L,
            checksum = "sha256:xyz"
        )
        dao.insert(rawEntity)
        val repository = RecordingRepositoryImpl(dao)

        // Library observation must not crash on unknown values.
        val observed = repository.observeRecordings().first()
        assertEquals(1, observed.size)
        val decoded = repository.getRecording("rec-future")!!
        assertEquals(RecordingSource.LEGACY_UNVERIFIED, decoded.source)
        assertEquals(CaptureStatus.FAILED, decoded.status)
        assertEquals("FUTURE_SOURCE_X", decoded.rawSourceType)
        assertEquals("FUTURE_STATUS_Y", decoded.rawStatus)

        // Updating only title preserves exact raw database values.
        repository.updateRecording(decoded.copy(title = "Updated future title"))
        val storedAfterRename = dao.getById("rec-future")!!
        assertEquals("Updated future title", storedAfterRename.title)
        assertEquals("FUTURE_SOURCE_X", storedAfterRename.sourceType)
        assertEquals("FUTURE_STATUS_Y", storedAfterRename.status)

        // Attempting to strip raw metadata or promote source/status is rejected.
        val tampered = decoded.copy(
            title = "Overwrite attempt",
            source = RecordingSource.VERIFIED_CALL_SOURCE,
            status = CaptureStatus.SAVED,
            rawSourceType = null,
            rawStatus = null
        )
        try {
            repository.updateRecording(tampered)
            fail("Expected updateRecording to reject overwriting unknown persisted values.")
        } catch (_: IllegalStateException) {
        }

        val storedAfterRejectedOverwrite = dao.getById("rec-future")!!
        assertEquals("Updated future title", storedAfterRejectedOverwrite.title)
        assertEquals("FUTURE_SOURCE_X", storedAfterRejectedOverwrite.sourceType)
        assertEquals("FUTURE_STATUS_Y", storedAfterRejectedOverwrite.status)
    }

    @Test
    fun addRecording_rejectsLegacyUnverifiedAndIdleStates() = runBlocking {
        val dao = InMemoryRecordingDao()
        val repository = RecordingRepositoryImpl(dao)

        val legacyAttempt = Recording(
            id = "rec-bad-1",
            title = "Bad",
            filePath = "recordings/bad.mp3",
            mimeType = "audio/mpeg",
            durationMillis = 1000L,
            fileSizeBytes = 1024L,
            source = RecordingSource.LEGACY_UNVERIFIED,
            sourceLabel = "Unverified legacy source",
            status = CaptureStatus.FAILED,
            createdAt = 1000L
        )
        try {
            repository.addRecording(legacyAttempt)
            fail("Expected LEGACY_UNVERIFIED insert to be rejected.")
        } catch (_: IllegalArgumentException) {
        }

        val idleAttempt = legacyAttempt.copy(
            id = "rec-bad-2",
            source = RecordingSource.IMPORTED,
            sourceLabel = "Imported audio",
            status = CaptureStatus.IDLE
        )
        try {
            repository.addRecording(idleAttempt)
            fail("Expected IDLE insert to be rejected.")
        } catch (_: IllegalArgumentException) {
        }
    }

    private class InMemoryRecordingDao : RecordingDao {
        private val rows = MutableStateFlow<List<RecordingEntity>>(emptyList())

        override suspend fun insert(recording: RecordingEntity) {
            rows.value = rows.value + recording
        }

        override suspend fun update(recording: RecordingEntity) {
            rows.value = rows.value.map { if (it.id == recording.id) recording else it }
        }

        override suspend fun delete(recording: RecordingEntity) {
            rows.value = rows.value.filterNot { it.id == recording.id }
        }

        override fun observeAll(): Flow<List<RecordingEntity>> =
            rows.map { list -> list.sortedByDescending { it.importedAt } }

        override suspend fun getById(recordingId: String): RecordingEntity? =
            rows.value.firstOrNull { it.id == recordingId }

        override fun observeCount(): Flow<Int> = rows.map { it.size }

        override suspend fun deleteById(recordingId: String) {
            rows.value = rows.value.filterNot { it.id == recordingId }
        }
    }
}
