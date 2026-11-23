package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val HomeEaseLightColorScheme = lightColorScheme(
    primary = DeepForest,
    onPrimary = PureWhite,
    primaryContainer = SageGreenContainer,
    onPrimaryContainer = DeepForest,
    secondary = SageGreen,
    onSecondary = PureWhite,
    secondaryContainer = SoftSandLight,
    onSecondaryContainer = DeepForest,
    tertiary = SoftSand,
    onTertiary = DeepForest,
    tertiaryContainer = SoftSandLight,
    onTertiaryContainer = Charcoal,
    background = WarmIvory,
    onBackground = Charcoal,
    surface = PureWhite,
    onSurface = Charcoal,
    surfaceVariant = WarmIvorySurface,
    onSurfaceVariant = MutedOlive,
    outline = SubtleBorder,
    outlineVariant = SoftSand,
    error = WarmCoral,
    onError = PureWhite,
    errorContainer = WarmCoralContainer,
    onErrorContainer = WarmCoral
)

private val HomeEaseDarkColorScheme = darkColorScheme(
    primary = DarkSagePrimary,
    onPrimary = DeepForest,
    primaryContainer = DeepForestContainer,
    onPrimaryContainer = DarkOnIvory,
    secondary = SageGreen,
    onSecondary = PureWhite,
    secondaryContainer = DarkForestSurfaceVariant,
    onSecondaryContainer = DarkOnIvory,
    tertiary = SoftSand,
    onTertiary = DeepForest,
    background = DarkForestBackground,
    onBackground = DarkOnIvory,
    surface = DarkForestSurface,
    onSurface = DarkOnIvory,
    surfaceVariant = DarkForestSurfaceVariant,
    onSurfaceVariant = SageGreenContainer,
    outline = DeepForestContainer,
    error = WarmCoral,
    onError = PureWhite
)

val HomeEaseShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun HomeEaseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) HomeEaseDarkColorScheme else HomeEaseLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = HomeEaseTypography,
        shapes = HomeEaseShapes,
        content = content
    )
}
