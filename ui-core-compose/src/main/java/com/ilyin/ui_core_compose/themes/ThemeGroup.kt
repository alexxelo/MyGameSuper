package com.ilyin.ui_core_compose.themes

import java.io.Serializable

data class ThemeGroup constructor(
  val colorId: String,
  val themes: List<AppTheme>,
) : Serializable