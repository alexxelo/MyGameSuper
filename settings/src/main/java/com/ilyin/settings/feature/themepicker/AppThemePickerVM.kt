package com.ilyin.settings.feature.themepicker

import androidx.lifecycle.LiveData
import com.ilyin.ui_core_compose.themes.AppTheme

interface AppThemePickerVM {

  val themes: List<AppTheme>
  val selectedTheme: LiveData<AppTheme>
  val showThemePicker: LiveData<Boolean>

  fun show()
  fun onThemePicked(theme: AppTheme)
  fun dismissRequest()
}