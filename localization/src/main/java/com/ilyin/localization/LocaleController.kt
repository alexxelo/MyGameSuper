package com.ilyin.localization

interface LocaleController {

  var defaultLanguage: AppLanguage
  val currentLanguage: AppLanguage

  fun changeLanguage(langCode: String, countryCode: String?): Boolean

  fun changeLanguage(lang: AppLanguage): Boolean {
    return changeLanguage(
      langCode = lang.language,
      countryCode = lang.countryCode,
    )
  }
}