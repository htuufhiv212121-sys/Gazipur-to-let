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

private val DarkColorScheme = darkColorScheme(
  primary = GreenDarkPrimary,
  onPrimary = GreenDarkOnPrimary,
  primaryContainer = GreenDarkContainer,
  onPrimaryContainer = Color(0xFFA5D6A7),
  secondary = GreenDarkPrimary,
  onSecondary = GreenDarkOnPrimary,
  background = BackgroundDark,
  surface = SurfaceDark,
  surfaceVariant = SurfaceVariantDark,
)

private val LightColorScheme = lightColorScheme(
  primary = GreenPrimary,
  onPrimary = GreenOnPrimary,
  primaryContainer = GreenPrimaryContainer,
  onPrimaryContainer = GreenOnPrimaryContainer,
  secondary = GreenSecondary,
  onSecondary = GreenOnSecondary,
  secondaryContainer = GreenSecondaryContainer,
  onSecondaryContainer = GreenOnSecondaryContainer,
  tertiary = GreenTertiary,
  onTertiary = GreenOnTertiary,
  background = BackgroundLight,
  surface = SurfaceLight,
  surfaceVariant = SurfaceVariantLight,
  outline = OutlineLight,
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // For brand consistency, we prefer our green brand palette
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
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
    typography = Typography,
    content = content
  )
}
