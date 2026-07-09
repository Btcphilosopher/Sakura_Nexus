package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SakuraPink,
    onPrimary = DeepIndigo,
    primaryContainer = SlateBlue,
    onPrimaryContainer = SakuraPinkLight,
    secondary = LuxuryGold,
    onSecondary = DeepIndigo,
    tertiary = CrimsonRed,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = SlateBlue,
    onSurfaceVariant = DarkOnSurface
)

private val LightColorScheme = lightColorScheme(
    primary = DeepIndigo,
    onPrimary = PureWhite,
    primaryContainer = SakuraPinkLight,
    onPrimaryContainer = CrimsonRed,
    secondary = SakuraPink,
    onSecondary = DeepIndigo,
    tertiary = CrimsonRed,
    background = OffWhite,
    onBackground = Color(0xFF1A1A1A), // Comfortable near-black for natural tones
    surface = PureWhite,
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = IceBlue, // Warm beige/sand color
    onSurfaceVariant = SlateBlue
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Disable dynamic colors to enforce the beautiful Japanese Sakura theme!
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
