package com.first.core.colors

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.ilyin.ui_core_compose.colors.MdColors
import com.ilyin.ui_core_compose.themes.AppTheme


private fun computeLightColorsMaterial3(appTheme: AppTheme): ColorScheme {
  val primary = Color(appTheme.primary)
  val onPrimary = if (primary.luminance() > 0.5f) Color.Black else Color.White
  return lightColorScheme(
    primary = primary,
    primaryContainer = Color(appTheme.primaryLight),
    onPrimary = onPrimary,
    secondary = MdColors.teal.c200,
    onSecondary = Color.Black,
    error = MdColors.red.c800
  )
}

private fun computeDarkColorsMaterial3(appTheme: AppTheme): ColorScheme {
  val primary = Color(appTheme.primary)
  val onPrimary = if (primary.luminance() > 0.5f) Color.Black else Color.White
  return darkColorScheme(
    primary = primary,
    primaryContainer = Color(appTheme.primaryVariant),
    onPrimary = onPrimary,
    secondary = MdColors.teal.c200,
    onSecondary = Color.Black,
    error = MdColors.red.c200,
  )
}

@Composable
fun ThemeMaterial3(
  appTheme: AppTheme = AppTheme.baseTheme(),
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = if (darkTheme) computeDarkColorsMaterial3(appTheme) else computeLightColorsMaterial3(appTheme),
    //typography = GeoTypography,
    content = content
  )
}