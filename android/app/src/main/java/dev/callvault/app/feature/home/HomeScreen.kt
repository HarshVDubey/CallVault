
package dev.callvault.app.feature.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.callvault.app.core.designsystem.components.CVCard
import dev.callvault.app.core.designsystem.components.CVPrimaryButton
import dev.callvault.app.core.designsystem.components.CVSecondaryButton
import dev.callvault.app.domain.model.CaptureStatus
import dev.callvault.app.domain.model.Recording
import dev.callvault.app.ui.theme.CVPrimary

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current.applicationContext
    val factory = remember(context) { HomeViewModelFactory(context) }
    val viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()
    val recordings = uiState.recordings

    val audioPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            viewModel.importAudio(uri.toString())
        }
    }

    var showCompatibilityInfo by remember {
        mutableStateOf(false)
    }

    val totalDuration = recordings.sumOf {
        it.durationMillis
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        // Header
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "CallVault",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Your private audio space",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Library overview
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Your Library",
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OverviewCard(
                    title = "Audio items",
                    value = recordings.size.toString(),
                    modifier = Modifier.weight(1f)
                )

                OverviewCard(
                    title = "Total duration",
                    value = formatDuration(totalDuration),
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = if (recordings.isEmpty()) {
                    "Your library statistics will appear when audio is added."
                } else {
                    "Your imported audio is stored in your private library."
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Quick actions
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleLarge
            )

            if (uiState.isImporting) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                CVPrimaryButton(
                    text = "Import Audio",
                    onClick = {
                        audioPicker.launch(arrayOf("audio/*"))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            CVSecondaryButton(
                text = "Recording Compatibility",
                onClick = {
                    showCompatibilityInfo = true
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Recent recordings
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Recent Recordings",
                style = MaterialTheme.typography.titleLarge
            )

            if (recordings.isEmpty()) {
                CVCard {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Your library is empty",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = "Import an audio file to see it here. " +
                                    "CallVault will keep your library organized in one place.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                recordings.take(5).forEach { recording ->
                    RecordingItem(recording)
                }
            }
        }

        // Privacy note
        CVCard {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Privacy first",
                    style = MaterialTheme.typography.titleMedium,
                    color = CVPrimary
                )

                Text(
                    text = "Your audio library is designed to work locally. " +
                            "Cloud processing and backup will remain optional.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }

    // Import result dialog
    if (uiState.importResultMessage != null) {
        AlertDialog(
            onDismissRequest = {
                viewModel.dismissImportResult()
            },
            title = {
                Text("Audio import")
            },
            text = {
                Text(uiState.importResultMessage!!)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.dismissImportResult()
                    }
                ) {
                    Text("Got it")
                }
            }
        )
    }

    // Recording compatibility dialog
    if (showCompatibilityInfo) {
        AlertDialog(
            onDismissRequest = {
                showCompatibilityInfo = false
            },
            title = {
                Text("Recording compatibility")
            },
            text = {
                Text(
                    "CallVault has not verified a permitted call-audio capture " +
                            "method for this device yet. Detecting a phone call does " +
                            "not mean an app can access its audio. We will show " +
                            "source-specific compatibility only after testing."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCompatibilityInfo = false
                    }
                ) {
                    Text("Understood")
                }
            }
        )
    }
}

@Composable
private fun RecordingItem(
    recording: Recording
) {
    CVCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = recording.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "${formatDuration(recording.durationMillis)}  •  " +
                        "${formatFileSize(recording.fileSizeBytes)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            val provenanceText = if (recording.status == CaptureStatus.SAVED) {
                recording.sourceLabel
            } else {
                val statusLabel = recording.status.name.lowercase()
                    .replaceFirstChar { it.uppercase() }
                "${recording.sourceLabel} • $statusLabel"
            }

            Text(
                text = provenanceText,
                style = MaterialTheme.typography.labelSmall,
                color = if (recording.status == CaptureStatus.FAILED) {
                    MaterialTheme.colorScheme.error
                } else {
                    CVPrimary
                }
            )
        }
    }
}

@Composable
private fun OverviewCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    CVCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun formatDuration(durationMillis: Long): String {
    val totalSeconds = durationMillis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        "%02d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%02d:%02d".format(minutes, seconds)
    }
}

private fun formatFileSize(bytes: Long): String {
    val kilobytes = bytes / 1024.0

    return if (kilobytes >= 1024) {
        "%.1f MB".format(kilobytes / 1024)
    } else {
        "%.0f KB".format(kilobytes)
    }
}
