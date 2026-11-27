
package dev.callvault.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.callvault.app.data.local.entity.RecordingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordingDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(recording: RecordingEntity)

    @Update
    suspend fun update(recording: RecordingEntity)

    @Delete
    suspend fun delete(recording: RecordingEntity)

    @Query(
        "SELECT * FROM recordings ORDER BY importedAt DESC"
    )
    fun observeAll(): Flow<List<RecordingEntity>>

    @Query(
        "SELECT * FROM recordings WHERE id = :recordingId LIMIT 1"
    )
    suspend fun getById(recordingId: String): RecordingEntity?

    @Query("SELECT COUNT(*) FROM recordings")
    fun observeCount(): Flow<Int>

    @Query("DELETE FROM recordings WHERE id = :recordingId")
    suspend fun deleteById(recordingId: String)

}
