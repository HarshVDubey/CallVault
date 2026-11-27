
package dev.callvault.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val CallVaultDarkColorScheme = darkColorScheme(
    primary = CVPrimary,
    onPrimary = CVOnBackground,
    primaryContainer = CVSurfaceElevated,
    onPrimaryContainer = CVPrimaryLight,

    secondary = CVPrimaryLight,
    onSecondary = CVBackground,
    secondaryContainer = CVSurfaceVariant,
    onSecondaryContainer = CVOnBackground,

    tertiary = CVInfo,
    onTertiary = CVBackground,

    background = CVBackground,
    onBackground = CVOnBackground,

    surface = CVSurface,
    onSurface = CVOnSurface,
    surfaceVariant = CVSurfaceVariant,
    onSurfaceVariant = CVTextSecondary,

    outline = CVOutline,
    outlineVariant = CVOutlineVariant,

    error = CVError,
    onError = CVOnError
)

private val CallVaultLightColorScheme = lightColorScheme(
    primary = CVPrimaryDark,
    onPrimary = CVOnBackground,
    primaryContainer = Color(0xFFEDE5FF),
    onPrimaryContainer = Color(0xFF32136E),

    secondary = CVPrimary,
    onSecondary = CVOnBackground,

    tertiary = Color(0xFF2563EB),
    onTertiary = Color.White,

    background = Color(0xFFF8F7FC),
    onBackground = Color(0xFF191622),

    surface = Color.White,
    onSurface = Color(0xFF191622),
    surfaceVariant = Color(0xFFECE9F2),
    onSurfaceVariant = Color(0xFF625C70),

    outline = Color(0xFFB8B1C5),
    outlineVariant = Color(0xFFDCD6E5),

    error = Color(0xFFB3261E),
    onError = Color.White
)

@Composable
fun CallVaultTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && isSystemInDarkTheme() -> CallVaultDarkColorScheme
        dynamicColor -> CallVaultLightColorScheme
        darkTheme -> CallVaultDarkColorScheme
        else -> CallVaultLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CallVaultTypography,
        shapes = CallVaultShapes,
        content = content
    )
}
