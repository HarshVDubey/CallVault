
package dev.callvault.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.callvault.app.data.local.dao.RecordingDao
import dev.callvault.app.data.local.entity.RecordingEntity

@Database(
    entities = [RecordingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CallVaultDatabase : RoomDatabase() {

    abstract fun recordingDao(): RecordingDao

    companion object {
        @Volatile
        private var INSTANCE: CallVaultDatabase? = null

        fun getDatabase(context: Context): CallVaultDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CallVaultDatabase::class.java,
                    "callvault_database"
                ).build().also { database ->
                    INSTANCE = database
                }
            }
        }
    }
}
