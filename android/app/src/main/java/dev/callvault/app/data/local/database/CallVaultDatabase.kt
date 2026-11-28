package dev.callvault.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.callvault.app.data.local.dao.RecordingDao
import dev.callvault.app.data.local.entity.RecordingEntity

@Database(
    entities = [RecordingEntity::class],
    version = 2,
    exportSchema = true
)
abstract class CallVaultDatabase : RoomDatabase() {

    abstract fun recordingDao(): RecordingDao

    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE recordings ADD COLUMN sourceLabel TEXT NOT NULL DEFAULT 'Imported audio'"
                )
                db.execSQL(
                    "ALTER TABLE recordings ADD COLUMN status TEXT NOT NULL DEFAULT 'SAVED'"
                )
                db.execSQL(
                    "ALTER TABLE recordings ADD COLUMN checksum TEXT DEFAULT NULL"
                )
                db.execSQL(
                    """
                    UPDATE recordings
                    SET sourceType = 'LEGACY_UNVERIFIED',
                        status = 'FAILED',
                        sourceLabel = 'Unverified legacy source'
                    WHERE sourceType = 'CALL_RECORDING'
                       OR sourceType NOT IN ('IMPORTED', 'MICROPHONE', 'VERIFIED_CALL_SOURCE', 'DEMO', 'LEGACY_UNVERIFIED')
                    """.trimIndent()
                )
            }
        }

        @Volatile
        private var INSTANCE: CallVaultDatabase? = null

        fun getDatabase(context: Context): CallVaultDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CallVaultDatabase::class.java,
                    "callvault_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { database ->
                        INSTANCE = database
                    }
            }
        }
    }
}
