
package dev.callvault.app.domain.repository

import dev.callvault.app.domain.model.Recording
import kotlinx.coroutines.flow.Flow

interface RecordingRepository {

    fun observeRecordings(): Flow<List<Recording>>

    fun observeRecordingCount(): Flow<Int>

    suspend fun getRecording(id: String): Recording?

    suspend fun addRecording(recording: Recording)

    suspend fun updateRecording(recording: Recording)

    suspend fun deleteRecording(id: String)
}
