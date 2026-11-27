
package dev.callvault.app.data.repository

import dev.callvault.app.data.local.dao.RecordingDao
import dev.callvault.app.data.mapper.toDomain
import dev.callvault.app.data.mapper.toEntity
import dev.callvault.app.domain.model.Recording
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
        recordingDao.insert(recording.toEntity())
    }

    override suspend fun updateRecording(recording: Recording) {
        recordingDao.update(recording.toEntity())
    }

    override suspend fun deleteRecording(id: String) {
        recordingDao.deleteById(id)
    }
}
