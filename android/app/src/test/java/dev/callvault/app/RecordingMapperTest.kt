package dev.callvault.app

import dev.callvault.app.data.local.entity.RecordingEntity
import dev.callvault.app.data.mapper.toDomain
import dev.callvault.app.data.mapper.toEntity
import dev.callvault.app.domain.model.CaptureStatus
import dev.callvault.app.domain.model.Recording
import dev.callvault.app.domain.model.RecordingSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class RecordingMapperTest {

    @Test
    fun knownSourcesAndStatuses_roundTripAccurately() {
        val sources = listOf(
            RecordingSource.IMPORTED to "Imported audio",
            RecordingSource.MICROPHONE to "Microphone",
            RecordingSource.VERIFIED_CALL_SOURCE to "Verified call source",
            RecordingSource.DEMO to "Demo fixture"
        )
        val persistableStatuses = CaptureStatus.entries.filter { it.isPersistable }

        sources.forEach { (source, label) ->
            persistableStatuses.forEach { status ->
                val domain = Recording(
                    id = "rec-${source.name}-${status.name}",
                    title = "Sample",
                    filePath = "recordings/sample.m4a",
                    mimeType = "audio/mp4",
                    durationMillis = 4500L,
                    fileSizeBytes = 20480L,
                    source = source,
                    sourceLabel = label,
                    status = status,
                    createdAt = 1764240000123L,
                    checksum = "sha256:abc123"
                )

                val entity = domain.toEntity()
                assertEquals(source.name, entity.sourceType)
                assertEquals(label, entity.sourceLabel)
                assertEquals(status.name, entity.status)
                assertEquals(1764240000123L, entity.importedAt)
                assertEquals("sha256:abc123", entity.checksum)

                val decoded = entity.toDomain()
                assertEquals(domain, decoded)
                assertNull(decoded.rawSourceType)
                assertNull(decoded.rawStatus)
            }
        }
    }

    @Test
    fun legacyCallRecording_mapsToLegacyUnverifiedAndFailed() {
        val legacyEntity = RecordingEntity(
            id = "legacy-1",
            title = "Legacy item",
            filePath = "recordings/legacy.mp3",
            mimeType = "audio/mpeg",
            durationMillis = 2000L,
            fileSizeBytes = 1024L,
            sourceType = "CALL_RECORDING",
            sourceLabel = "Call recording",
            status = "SAVED",
            importedAt = 1764240000000L,
            checksum = null
        )

        val domain = legacyEntity.toDomain()
        assertEquals(RecordingSource.LEGACY_UNVERIFIED, domain.source)
        assertEquals(CaptureStatus.FAILED, domain.status)
        assertEquals("Unverified legacy source", domain.sourceLabel)
        assertNull(domain.rawSourceType)
        assertNull(domain.rawStatus)

        val reencoded = domain.toEntity()
        assertEquals("LEGACY_UNVERIFIED", reencoded.sourceType)
        assertEquals("FAILED", reencoded.status)
        assertEquals("Unverified legacy source", reencoded.sourceLabel)
    }

    @Test
    fun unknownSourceAndUnknownStatus_surviveDecodeAndEncodeWithoutDataLoss() {
        val futureEntity = RecordingEntity(
            id = "future-1",
            title = "Future item",
            filePath = "recordings/future.wav",
            mimeType = "audio/wav",
            durationMillis = 9000L,
            fileSizeBytes = 65536L,
            sourceType = "FUTURE_SYSTEM_RECORDER_V3",
            sourceLabel = "System capture v3",
            status = "ARCHIVED_CLOUD_V3",
            importedAt = 1764249999000L,
            checksum = "sha256:future"
        )

        val decoded = futureEntity.toDomain()
        assertEquals(RecordingSource.LEGACY_UNVERIFIED, decoded.source)
        assertEquals(CaptureStatus.FAILED, decoded.status)
        assertEquals("System capture v3", decoded.sourceLabel)
        assertEquals("FUTURE_SYSTEM_RECORDER_V3", decoded.rawSourceType)
        assertEquals("ARCHIVED_CLOUD_V3", decoded.rawStatus)
        assertTrue(decoded.hasUnrecognizedPersistedValues)

        val renamed = decoded.copy(title = "Renamed future item")
        val reencoded = renamed.toEntity()
        assertEquals("Renamed future item", reencoded.title)
        assertEquals("FUTURE_SYSTEM_RECORDER_V3", reencoded.sourceType)
        assertEquals("ARCHIVED_CLOUD_V3", reencoded.status)
        assertEquals("System capture v3", reencoded.sourceLabel)
    }

    @Test
    fun unknownSourceWithKnownSavedStatus_preservesOriginalStatusAcrossRoundTrip() {
        val futureSourceSavedEntity = RecordingEntity(
            id = "future-2",
            title = "Future saved item",
            filePath = "recordings/future2.mp3",
            mimeType = "audio/mpeg",
            durationMillis = 3000L,
            fileSizeBytes = 8192L,
            sourceType = "FUTURE_SOURCE_V2",
            sourceLabel = "Future source",
            status = "SAVED",
            importedAt = 1764240000555L,
            checksum = null
        )

        val decoded = futureSourceSavedEntity.toDomain()
        assertEquals(RecordingSource.LEGACY_UNVERIFIED, decoded.source)
        assertEquals(CaptureStatus.FAILED, decoded.status)
        assertEquals("FUTURE_SOURCE_V2", decoded.rawSourceType)
        assertEquals("SAVED", decoded.rawStatus)

        val reencoded = decoded.toEntity()
        assertEquals("FUTURE_SOURCE_V2", reencoded.sourceType)
        assertEquals("SAVED", reencoded.status)
    }

    @Test
    fun knownSourceWithUnknownStatus_preservesOriginalStatusAcrossRoundTrip() {
        val futureStatusEntity = RecordingEntity(
            id = "future-3",
            title = "Imported future state",
            filePath = "recordings/future3.mp3",
            mimeType = "audio/mpeg",
            durationMillis = 3000L,
            fileSizeBytes = 8192L,
            sourceType = "IMPORTED",
            sourceLabel = "Imported audio",
            status = "ENCRYPTED_ARCHIVED",
            importedAt = 1764240000777L,
            checksum = null
        )

        val decoded = futureStatusEntity.toDomain()
        assertEquals(RecordingSource.IMPORTED, decoded.source)
        assertEquals(CaptureStatus.FAILED, decoded.status)
        assertNull(decoded.rawSourceType)
        assertEquals("ENCRYPTED_ARCHIVED", decoded.rawStatus)

        val reencoded = decoded.toEntity()
        assertEquals("IMPORTED", reencoded.sourceType)
        assertEquals("ENCRYPTED_ARCHIVED", reencoded.status)
    }

    @Test
    fun toEntity_rejectsTamperedSourceOrStatusWhenRawMetadataIsPresent() {
        val futureEntity = RecordingEntity(
            id = "future-4",
            title = "Tamper test",
            filePath = "recordings/future4.mp3",
            mimeType = "audio/mpeg",
            durationMillis = 3000L,
            fileSizeBytes = 8192L,
            sourceType = "FUTURE_SOURCE_V4",
            sourceLabel = "Future source",
            status = "FUTURE_STATUS_V4",
            importedAt = 1764240000888L
        ).toDomain()

        try {
            futureEntity.copy(source = RecordingSource.VERIFIED_CALL_SOURCE).toEntity()
            fail("Expected promotion of unknown source to VERIFIED_CALL_SOURCE to fail.")
        } catch (_: IllegalStateException) {
        }

        try {
            futureEntity.copy(status = CaptureStatus.SAVED).toEntity()
            fail("Expected altering unknown status to SAVED to fail.")
        } catch (_: IllegalStateException) {
        }
    }
}
