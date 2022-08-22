package com.ilyin.settings.feature.themepicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.ilyin.tools_android.VMPart
import com.ilyin.tools_android.VMPartImpl
import com.ilyin.ui_core_compose.themes.AppTheme

class AppThemePickerVMImpl constructor(
  savedStateHandle: SavedStateHandle,
  private val themeController: ThemeController,
  private val onShow: () -> Unit = {},
  private val onThemePicked: () -> Unit = {},
) : AppThemePickerVM, VMPart by VMPartImpl(
  savedStateHandle = savedStateHandle,
  name = "AppThemePickerVMImpl"
) {

  override val themes: List<AppTheme> = themeController.themes

  private val _showThemePicker = getLiveData("showThemePicker", false)
  override val showThemePicker: LiveData<Boolean> = _showThemePicker

  private val _selectedTheme = getLiveData("selectedTheme", themeController.theme)
  override val selectedTheme: LiveData<AppTheme> = _selectedTheme

  override fun show() {
    _showThemePicker.value = true
    onShow()
  }

  override fun onThemePicked(theme: AppTheme) {
    themeController.theme = theme
    _selectedTheme.value = theme
    onThemePicked()
  }

  override fun dismissRequest() {
    _showThemePicker.value = false
  }
}