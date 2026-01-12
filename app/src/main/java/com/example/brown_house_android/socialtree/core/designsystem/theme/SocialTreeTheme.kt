package com.example.brown_house_android.socialtree.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownColor
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownTypography

/**
 * Social Tree Theme
 * Warm earthy color scheme from HTML wireframes
 */
private val LightColorScheme = lightColorScheme(
    primary = BrownColor.Primary,
    onPrimary = BrownColor.TextMainDark,
    primaryContainer = BrownColor.PrimaryDark,
    onPrimaryContainer = BrownColor.TextMainDark,

    secondary = BrownColor.Accent,
    onSecondary = BrownColor.TextMainLight,
    secondaryContainer = BrownColor.AccentLight,
    onSecondaryContainer = BrownColor.TextMainLight,

    tertiary = BrownColor.AccentOrange,
    onTertiary = BrownColor.TextMainDark,
    tertiaryContainer = BrownColor.AccentSoft,
    onTertiaryContainer = BrownColor.TextMainLight,

    error = BrownColor.Error,
    onError = BrownColor.TextMainDark,

    background = BrownColor.BackgroundLight,
    onBackground = BrownColor.TextMainLight,

    surface = BrownColor.SurfaceLight,
    onSurface = BrownColor.TextMainLight,
    surfaceVariant = BrownColor.SurfaceVariant,
    onSurfaceVariant = BrownColor.TextSubLight,

    outline = BrownColor.BorderLight,
    outlineVariant = BrownColor.BorderLight,

    scrim = BrownColor.TextMainLight.copy(alpha = 0.4f)
)

private val DarkColorScheme = darkColorScheme(
    primary = BrownColor.Accent,
    onPrimary = BrownColor.TextMainLight,
    primaryContainer = BrownColor.PrimaryDark,
    onPrimaryContainer = BrownColor.TextMainDark,

    secondary = BrownColor.AccentLight,
    onSecondary = BrownColor.TextMainLight,
    secondaryContainer = BrownColor.Accent,
    onSecondaryContainer = BrownColor.TextMainDark,

    tertiary = BrownColor.AccentOrange,
    onTertiary = BrownColor.TextMainDark,
    tertiaryContainer = BrownColor.AccentSoft,
    onTertiaryContainer = BrownColor.TextMainLight,

    error = BrownColor.Error,
    onError = BrownColor.TextMainDark,

    background = BrownColor.BackgroundDark,
    onBackground = BrownColor.TextMainDark,

    surface = BrownColor.SurfaceDark,
    onSurface = BrownColor.TextMainDark,
    surfaceVariant = BrownColor.SurfaceDark,
    onSurfaceVariant = BrownColor.TextSubDark,

    outline = BrownColor.BorderDark,
    outlineVariant = BrownColor.BorderDark,

    scrim = BrownColor.TextMainDark.copy(alpha = 0.4f)
)

@Composable
fun SocialTreeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    // We disable it by default to maintain brand identity from wireframes
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = BrownTypography.createTypography(),
        content = content
    )
}
