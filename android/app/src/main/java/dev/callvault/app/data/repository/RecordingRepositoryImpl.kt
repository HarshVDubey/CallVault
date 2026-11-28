package dev.callvault.app.data.repository

import dev.callvault.app.data.local.dao.RecordingDao
import dev.callvault.app.data.mapper.isRecognizedPersistableStatus
import dev.callvault.app.data.mapper.isRecognizedSourceType
import dev.callvault.app.data.mapper.toDomain
import dev.callvault.app.data.mapper.toEntity
import dev.callvault.app.domain.model.Recording
import dev.callvault.app.domain.model.RecordingSource
import dev.callvault.app.domain.repository.RecordingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecordingRepositoryImpl(
    private val recordingDao: RecordingDao
) : RecordingRepository {

    override fun observeRecordings(): Flow<List<Recording>> {
        return recordingDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeRecordingCount(): Flow<Int> {
        return recordingDao.observeCount()
    }

    override suspend fun getRecording(id: String): Recording? {
        return recordingDao.getById(id)?.toDomain()
    }

    override suspend fun addRecording(recording: Recording) {
        require(recording.source != RecordingSource.LEGACY_UNVERIFIED) {
            "New recordings cannot use LEGACY_UNVERIFIED source."
        }
        require(recording.status.isPersistable) {
            "New recordings cannot be persisted with status ${recording.status}."
        }
        require(!recording.hasUnrecognizedPersistedValues) {
            "New recordings cannot carry unrecognized raw persisted metadata."
        }
        recordingDao.insert(recording.toEntity())
    }

    override suspend fun updateRecording(recording: Recording) {
        require(recording.status.isPersistable) {
            "Recordings cannot be persisted with status ${recording.status}."
        }

        val existing = recordingDao.getById(recording.id)
        if (existing != null) {
            val existingHasUnknownSource = !isRecognizedSourceType(existing.sourceType)
            val existingHasUnknownStatus = !isRecognizedPersistableStatus(existing.status)

            if (existingHasUnknownSource && recording.rawSourceType != existing.sourceType) {
                throw IllegalStateException(
                    "Refusing to overwrite unrecognized persisted sourceType '${existing.sourceType}' for recording '${recording.id}'."
                )
            }
            if ((existingHasUnknownSource || existingHasUnknownStatus) && recording.rawStatus != existing.status) {
                throw IllegalStateException(
                    "Refusing to overwrite unrecognized or protected persisted status '${existing.status}' for recording '${recording.id}'."
                )
            }
        }

        recordingDao.update(recording.toEntity())
    }

    override suspend fun deleteRecording(id: String) {
        recordingDao.deleteById(id)
    }
}
