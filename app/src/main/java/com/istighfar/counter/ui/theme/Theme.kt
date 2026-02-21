package com.istighfar.counter.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    onPrimary = Color.White,
    primaryContainer = TealLight,
    onPrimaryContainer = TealDark,
    secondary = GoldAccent,
    onSecondary = Color.White,
    secondaryContainer = GoldLight,
    onSecondaryContainer = TealDark,
    background = CreamBackground,
    onBackground = TealDark,
    surface = CreamBackground,
    onSurface = TealDark,
    surfaceVariant = GoldLight,
    onSurfaceVariant = TealDark
)

private val DarkColorScheme = darkColorScheme(
    primary = TealLight,
    onPrimary = Color.White,
    primaryContainer = TealDark,
    onPrimaryContainer = GoldLight,
    secondary = GoldAccent,
    onSecondary = Color.Black,
    secondaryContainer = DarkCard,
    onSecondaryContainer = GoldLight,
    background = DarkBackground,
    onBackground = GoldLight,
    surface = DarkSurface,
    onSurface = GoldLight,
    surfaceVariant = DarkCard,
    onSurfaceVariant = GoldLight
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
