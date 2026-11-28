package dev.callvault.app

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.callvault.app.data.local.database.CallVaultDatabase
import dev.callvault.app.data.local.entity.RecordingEntity
import dev.callvault.app.data.repository.RecordingRepositoryImpl
import dev.callvault.app.domain.model.CaptureStatus
import dev.callvault.app.domain.model.Recording
import dev.callvault.app.domain.model.RecordingSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CallVaultDatabaseMigrationTest {

    private val testDbName = "callvault_migration_test.db"
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        context.deleteDatabase(testDbName)
    }

    @After
    fun tearDown() {
        context.deleteDatabase(testDbName)
    }

    @Test
    fun migrateV1ToV2_preservesImportedRowsAndConvertsLegacyCallRecording() = runBlocking {
        // 1. Create an exact v1 SQLite database matching exported 1.json.
        val v1Helper = object : SQLiteOpenHelper(context, testDbName, null, 1) {
            override fun onCreate(db: SQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `recordings` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `filePath` TEXT NOT NULL, `mimeType` TEXT NOT NULL, `durationMillis` INTEGER NOT NULL, `fileSizeBytes` INTEGER NOT NULL, `sourceType` TEXT NOT NULL, `importedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))"
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_recordings_importedAt` ON `recordings` (`importedAt`)"
                )
            }

            override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit
        }

        v1Helper.writableDatabase.use { sqlite ->
            sqlite.execSQL(
                """
                INSERT INTO recordings (id, title, filePath, mimeType, durationMillis, fileSizeBytes, sourceType, importedAt)
                VALUES ('rec-imp-1', 'sample-3s', 'recordings/rec-imp-1.mp3', 'audio/mpeg', 3000, 52224, 'IMPORTED', 1764240000000)
                """.trimIndent()
            )
            sqlite.execSQL(
                """
                INSERT INTO recordings (id, title, filePath, mimeType, durationMillis, fileSizeBytes, sourceType, importedAt)
                VALUES ('rec-legacy-1', 'old-call', 'recordings/rec-legacy-1.mp3', 'audio/mpeg', 8000, 90000, 'CALL_RECORDING', 1764240001000)
                """.trimIndent()
            )
        }
        v1Helper.close()

        // 2. Open through Room v2 builder with MIGRATION_1_2 and zero destructive fallback.
        val roomDb = Room.databaseBuilder(
            context,
            CallVaultDatabase::class.java,
            testDbName
        )
            .addMigrations(CallVaultDatabase.MIGRATION_1_2)
            .build()

        try {
            val dao = roomDb.recordingDao()
            val repository = RecordingRepositoryImpl(dao)

            val imported = repository.getRecording("rec-imp-1")!!
            assertEquals(RecordingSource.IMPORTED, imported.source)
            assertEquals("Imported audio", imported.sourceLabel)
            assertEquals(CaptureStatus.SAVED, imported.status)
            assertEquals(1764240000000L, imported.createdAt)
            assertNull(imported.checksum)

            val legacy = repository.getRecording("rec-legacy-1")!!
            assertEquals(RecordingSource.LEGACY_UNVERIFIED, legacy.source)
            assertEquals("Unverified legacy source", legacy.sourceLabel)
            assertEquals(CaptureStatus.FAILED, legacy.status)
            assertEquals(1764240001000L, legacy.createdAt)

            val rawLegacy = dao.getById("rec-legacy-1")!!
            assertEquals("LEGACY_UNVERIFIED", rawLegacy.sourceType)
            assertEquals("FAILED", rawLegacy.status)
            assertEquals("Unverified legacy source", rawLegacy.sourceLabel)

            // 3. Verify inserting a new v2 record works cleanly after migration.
            val newV2 = Recording(
                id = "rec-imp-2",
                title = "new-v2",
                filePath = "recordings/rec-imp-2.wav",
                mimeType = "audio/wav",
                durationMillis = 5000L,
                fileSizeBytes = 88000L,
                source = RecordingSource.IMPORTED,
                sourceLabel = "Imported audio",
                status = CaptureStatus.SAVED,
                createdAt = 1764240002000L,
                checksum = null
            )
            repository.addRecording(newV2)
            assertEquals(3, repository.observeRecordings().first().size)
        } finally {
            roomDb.close()
        }
    }

    @Test
    fun unknownPersistedValues_surviveTitleUpdateAndRejectOverwrite_endToEnd() = runBlocking {
        val roomDb = Room.databaseBuilder(
            context,
            CallVaultDatabase::class.java,
            testDbName
        )
            .addMigrations(CallVaultDatabase.MIGRATION_1_2)
            .build()

        try {
            val dao = roomDb.recordingDao()
            val repository = RecordingRepositoryImpl(dao)

            dao.insert(
                RecordingEntity(
                    id = "rec-future-e2e",
                    title = "Future Audio",
                    filePath = "recordings/future.m4a",
                    mimeType = "audio/mp4",
                    durationMillis = 4000L,
                    fileSizeBytes = 40960L,
                    sourceType = "FUTURE_SOURCE_V3",
                    sourceLabel = "Future Source",
                    status = "FUTURE_STATUS_V3",
                    importedAt = 1764240003000L,
                    checksum = "sha256:v3"
                )
            )

            // Observation and decode do not crash.
            val observed = repository.observeRecordings().first()
            assertEquals(1, observed.size)
            val decoded = repository.getRecording("rec-future-e2e")!!
            assertEquals(RecordingSource.LEGACY_UNVERIFIED, decoded.source)
            assertEquals(CaptureStatus.FAILED, decoded.status)
            assertEquals("FUTURE_SOURCE_V3", decoded.rawSourceType)
            assertEquals("FUTURE_STATUS_V3", decoded.rawStatus)

            // Renaming only title preserves exact unrecognized SQLite strings.
            repository.updateRecording(decoded.copy(title = "Renamed Future Audio"))
            val storedAfterRename = dao.getById("rec-future-e2e")!!
            assertEquals("Renamed Future Audio", storedAfterRename.title)
            assertEquals("FUTURE_SOURCE_V3", storedAfterRename.sourceType)
            assertEquals("FUTURE_STATUS_V3", storedAfterRename.status)

            // Overwrite attempt is rejected without modifying SQLite row.
            val overwriteAttempt = decoded.copy(
                title = "Should Fail",
                source = RecordingSource.VERIFIED_CALL_SOURCE,
                status = CaptureStatus.SAVED,
                rawSourceType = null,
                rawStatus = null
            )
            try {
                repository.updateRecording(overwriteAttempt)
                fail("Expected overwrite of unknown persisted values to throw.")
            } catch (_: IllegalStateException) {
            }

            val storedAfterRejected = dao.getById("rec-future-e2e")!!
            assertEquals("Renamed Future Audio", storedAfterRejected.title)
            assertEquals("FUTURE_SOURCE_V3", storedAfterRejected.sourceType)
            assertEquals("FUTURE_STATUS_V3", storedAfterRejected.status)
        } finally {
            roomDb.close()
        }
    }
}
