package com.ilyin.ui_core_compose.themes

import androidx.compose.ui.graphics.toArgb
import com.ilyin.ui_core_compose.colors.MdColor
import com.ilyin.ui_core_compose.colors.MdColors
import java.io.Serializable

data class AppTheme constructor(
  val id: String,
  val primary: Int,
  val primaryVariant: Int,
  val primaryLight: Int,
) : Serializable {

  companion object {

    fun mainThemeOfColor(color: MdColor): AppTheme {
      return AppTheme(
        id = color.id + "_1",
        primary = color.c500.toArgb(),
        primaryVariant = color.c700.toArgb(),
        primaryLight = color.c200.toArgb(),
      )
    }

    fun fromColorAdvanced(color: MdColor): List<AppTheme> {
      return listOf(
        AppTheme(
          id = color.id + "_0",
          primary = color.c400.toArgb(),
          primaryVariant = color.c600.toArgb(),
          primaryLight = color.c100.toArgb(),
        ),
        mainThemeOfColor(color),
        AppTheme(
          id = color.id + "_2",
          primary = color.c600.toArgb(),
          primaryVariant = color.c800.toArgb(),
          primaryLight = color.c300.toArgb(),
        ),
        AppTheme(
          id = color.id + "_3",
          primary = color.c700.toArgb(),
          primaryVariant = color.c900.toArgb(),
          primaryLight = color.c400.toArgb(),
        ),
      )
    }

    fun baseTheme(): AppTheme {
      return mainThemeOfColor(
        color = MdColors.deepPurple,
      )
    }

    fun baseThemes(): List<AppTheme> {
      val colors = MdColors.colors
      return colors.map(Companion::mainThemeOfColor)
    }

    fun advancedThemes(): List<AppTheme> {
      val colors = MdColors.colors
      return colors.flatMap(Companion::fromColorAdvanced)
    }

    fun groupedThemes(): List<ThemeGroup> {
      return MdColors.colors.map { ThemeGroup(colorId = it.id, themes = fromColorAdvanced(it)) }
    }
  }
}