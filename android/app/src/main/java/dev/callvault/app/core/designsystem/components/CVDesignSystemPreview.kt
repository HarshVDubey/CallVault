
package dev.callvault.app.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.callvault.app.ui.theme.CallVaultTheme


@Composable
fun CVDesignSystemPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "CallVault",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Design System 1.0",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Buttons", style = MaterialTheme.typography.titleMedium)

                CVPrimaryButton(
                    text = "Primary Action",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                )

                CVSecondaryButton(
                    text = "Secondary Action",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Recording Status", style = MaterialTheme.typography.titleMedium)
                CVRecordingBadge()
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Cards", style = MaterialTheme.typography.titleMedium)

                CVCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Recent Recording",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "Sample call recording",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        CVRecordingBadge(label = "Saved")
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Empty State", style = MaterialTheme.typography.titleMedium)

                CVCard {
                    CVEmptyState(
                        title = "No recordings yet",
                        description = "Your recordings will appear here."
                    )
                }
            }
        }
    }
}
