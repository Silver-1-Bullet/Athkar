package com.istighfar.counter.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    onPrimary = CreamBackground,
    primaryContainer = TealLight,
    onPrimaryContainer = TealDark,
    secondary = GoldAccent,
    onSecondary = TealDark,
    secondaryContainer = GoldLight,
    onSecondaryContainer = TealDark,
    background = CreamBackground,
    onBackground = TealDark,
    surface = CreamBackground,
    onSurface = TealDark,
    tertiary = SageGreen
)

private val DarkColorScheme = darkColorScheme(
    primary = TealLight,
    onPrimary = DarkBackground,
    primaryContainer = TealPrimary,
    onPrimaryContainer = GoldLight,
    secondary = GoldAccent,
    onSecondary = DarkBackground,
    secondaryContainer = DarkCard,
    onSecondaryContainer = GoldLight,
    background = DarkBackground,
    onBackground = GoldLight,
    surface = DarkSurface,
    onSurface = GoldLight,
    tertiary = SageGreen
)

@Composable
fun AthkarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AthkarTypography,
        content = content
    )
}
