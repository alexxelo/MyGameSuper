package com.ilyin.settings.feature.langpicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.ilyin.localization.AppLanguage
import com.ilyin.localization.LocaleController
import com.ilyin.tools_android.VMPart
import com.ilyin.tools_android.VMPartImpl

class LanguagePickerVMImpl constructor(
  savedStateHandle: SavedStateHandle,
  private val localeController: LocaleController,
  private val onShow: () -> Unit = {},
) : LanguagePickerVM, VMPart by VMPartImpl(
  savedStateHandle = savedStateHandle,
  name = "LanguagePickerVMImpl"
) {

  override val languages: List<AppLanguage> = AppLanguage.allLanguages()

  private val _showLangPicker = getLiveData("showLangPicker", false)
  override val showLangPicker: LiveData<Boolean> = _showLangPicker

  private val _selectedLang = getLiveData("selectedLang", localeController.currentLanguage)
  override val selectedLang: LiveData<AppLanguage> = _selectedLang

  override fun show() {
    _showLangPicker.value = true
    onShow()
  }

  override fun onLangPicked(language: AppLanguage): Boolean {
    return localeController.changeLanguage(language)
  }

  override fun dismissRequest() {
    _showLangPicker.value = false
  }
}