package com.ilyin.localization

import java.util.*

class LocaleControllerImpl constructor(private val memory: LocalePref) : LocaleController {

  override val currentLanguage: AppLanguage
    get() {
      val defaultLocale = Locale.getDefault()
      val selectedLanguageCode = memory.languageCode ?: defaultLocale.language
      val selectedCountryCode = memory.countryCode ?: defaultLocale.country
      return AppLanguage.getBy(selectedLanguageCode, selectedCountryCode)
    }

  /**
   * @return true if locale changed.
   */
  override fun changeLanguage(langCode: String, countryCode: String?): Boolean {
    return if (memory.languageCode != langCode || memory.countryCode != countryCode) {
      memory.languageCode = langCode
      memory.countryCode = countryCode
      true
    } else {
      false
    }
  }
}