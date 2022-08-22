package com.ilyin.localization

interface LocaleController {

  val currentLanguage: AppLanguage

  fun changeLanguage(langCode: String, countryCode: String?): Boolean

  fun changeLanguage(lang: AppLanguage): Boolean {
    return changeLanguage(
      langCode = lang.language,
      countryCode = lang.countryCode,
    )
  }
}