package com.ilyin.settings.feature.langpicker

import androidx.lifecycle.LiveData
import com.ilyin.localization.AppLanguage

interface LanguagePickerVM {

  val languages: List<AppLanguage>
  val showLangPicker: LiveData<Boolean>
  val selectedLang: LiveData<AppLanguage>

  fun show()

  /**
   * return true uf language is changed. Use it to request app restart.
   */
  fun onLangPicked(language: AppLanguage): Boolean
  fun dismissRequest()
}