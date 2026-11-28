package dev.callvault.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recordings",
    indices = [
        Index(value = ["importedAt"])
    ]
)
data class RecordingEntity(
    @PrimaryKey
    val id: String,

    val title: String,

    // Relative path inside CallVault's private files directory.
    val filePath: String,

    val mimeType: String,

    val durationMillis: Long,

    val fileSizeBytes: Long,

    val sourceType: String,

    @ColumnInfo(defaultValue = "'Imported audio'")
    val sourceLabel: String = "Imported audio",

    @ColumnInfo(defaultValue = "'SAVED'")
    val status: String = "SAVED",

    val importedAt: Long,

    @ColumnInfo(defaultValue = "NULL")
    val checksum: String? = null
)
