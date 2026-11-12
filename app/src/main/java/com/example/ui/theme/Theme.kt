package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = TealPrimaryDark,
    onPrimary = Color(0xFF00363D),
    primaryContainer = Color(0xFF004E58),
    onPrimaryContainer = TealContainer,
    secondary = CoralSecondaryDark,
    onSecondary = Color(0xFF5F1600),
    secondaryContainer = Color(0xFF812B0E),
    onSecondaryContainer = CoralContainer,
    tertiary = SageTertiaryDark,
    onTertiary = Color(0xFF003734),
    tertiaryContainer = Color(0xFF1E4E4B),
    onTertiaryContainer = SageContainer,
    background = BackgroundDark,
    onBackground = Color(0xFFE0E3E1),
    surface = SurfaceDark,
    onSurface = Color(0xFFE0E3E1),
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = Color(0xFFBFC9C7),
  )

private val LightColorScheme =
  lightColorScheme(
    primary = TealPrimary,
    onPrimary = Color.White,
    primaryContainer = TealContainer,
    onPrimaryContainer = OnTealContainer,
    secondary = CoralSecondary,
    onSecondary = Color.White,
    secondaryContainer = CoralContainer,
    onSecondaryContainer = OnCoralContainer,
    tertiary = SageTertiary,
    onTertiary = Color.White,
    tertiaryContainer = SageContainer,
    onTertiaryContainer = OnSageContainer,
    background = BackgroundLight,
    onBackground = OnSurfaceLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = Color(0xFF3F4948),
  )

@Composable
fun ReturnTheFavorTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep warm branding cohesive across Android versions
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
