
package dev.callvault.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.callvault.app.feature.home.HomeScreen
import dev.callvault.app.ui.theme.CallVaultTheme

private enum class AppDestination(
    val label: String,
    val symbol: String
) {
    HOME("Home", "⌂"),
    LIBRARY("Library", "▤"),
    INSIGHTS("Insights", "✦"),
    BACKUP("Backup", "☁"),
    SETTINGS("Settings", "⚙")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CallVaultTheme {
                CallVaultApp()
            }
        }
    }
}

@Composable
private fun CallVaultApp() {
    var selectedDestination by rememberSaveable {
        mutableStateOf(AppDestination.HOME.name)
    }

    val currentDestination = AppDestination.valueOf(selectedDestination)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                AppDestination.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = currentDestination == destination,
                        onClick = {
                            selectedDestination = destination.name
                        },
                        icon = {
                            Text(
                                text = destination.symbol,
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        label = {
                            Text(destination.label)
                        },
                        alwaysShowLabel = true
                    )
                }
            }
        }
    ) { innerPadding ->
        when (currentDestination) {
            AppDestination.HOME -> {
                HomeScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            }

            AppDestination.LIBRARY -> {
                PlaceholderDestination(
                    title = "Library",
                    description = "Your imported and verified audio items will appear here.",
                    modifier = Modifier.padding(innerPadding)
                )
            }

            AppDestination.INSIGHTS -> {
                PlaceholderDestination(
                    title = "Insights",
                    description = "Transcripts and AI-generated summaries will appear here when implemented.",
                    modifier = Modifier.padding(innerPadding)
                )
            }

            AppDestination.BACKUP -> {
                PlaceholderDestination(
                    title = "Backup",
                    description = "Optional encrypted backup will be configured here.",
                    modifier = Modifier.padding(innerPadding)
                )
            }

            AppDestination.SETTINGS -> {
                PlaceholderDestination(
                    title = "Settings",
                    description = "Privacy, appearance and app preferences will live here.",
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun PlaceholderDestination(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = description,
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Coming in a later milestone",
                modifier = Modifier.padding(top = 20.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
