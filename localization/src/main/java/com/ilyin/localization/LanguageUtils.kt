package com.ilyin.localization

import androidx.annotation.DrawableRes
import java.util.*

object LanguageUtils {

  fun computeLanguageDisplay(lang: AppLanguage): String {
    return computeLanguageDisplay(
      languageCode = lang.language,
      countryCode = lang.countryCode,
    )
  }

  fun computeLanguageDisplay(languageCode: String, countryCode: String?): String {
    val langLocale = if (countryCode.isNullOrBlank()) {
      Locale.forLanguageTag(languageCode)
    } else {
      Locale(languageCode, countryCode)
    }
    val langDisplay = langLocale.getDisplayLanguage(langLocale).replaceFirstChar { it.titlecase(langLocale) }
    val country = langLocale.getDisplayCountry(langLocale).replaceFirstChar { it.titlecase(langLocale) }
    return if (country.isBlank()) {
      langDisplay
    } else {
      "$langDisplay ($country)"
    }
  }

  @DrawableRes
  fun getDrawableBy(lang: AppLanguage): Int {
    return getDrawableBy(
      languageCode = lang.language,
      countryCode = lang.countryCode,
    )
  }

  @DrawableRes
  fun getDrawableBy(languageCode: String, countryCode: String? = null): Int {
    val languageAccessor: (AppLanguage) -> String = AppLanguage::language
    return with(AppLanguage) {
      when (languageCode) {
        languageAccessor(RUSSIAN) -> R.drawable.ic_flag_russia
        languageAccessor(GERMAN) -> R.drawable.ic_flag_germany
        languageAccessor(ENGLISH) -> R.drawable.ic_flag_united_kingdom
        languageAccessor(SPAIN) -> {
          when (countryCode) {
            SPAIN_MEXICO.countryCode -> R.drawable.ic_flag_mexico
            else -> R.drawable.ic_flag_spain
          }
        }
        languageAccessor(HINDI) -> R.drawable.ic_flag_india
        languageAccessor(KOREAN) -> R.drawable.ic_flag_south_korea
        languageAccessor(JAPAN) -> R.drawable.ic_flag_japan
        languageAccessor(FRENCH) -> R.drawable.ic_flag_france
        languageAccessor(CHINESE) -> R.drawable.ic_flag_china
        languageAccessor(PORTUGAL_BRAZIL), languageAccessor(PORTUGAL_PORTUGAL) -> {
          when (countryCode) {
            PORTUGAL_PORTUGAL.countryCode -> R.drawable.ic_flag_portugal
            else -> R.drawable.ic_flag_brazil
          }
        }
        languageAccessor(ITALIAN) -> R.drawable.ic_flag_italy
        languageAccessor(ARABIC) -> R.drawable.ic_flag_arabic
        languageAccessor(INDONESIAN) -> R.drawable.ic_flag_indonesian
        languageAccessor(POLISH) -> R.drawable.ic_flag_poland
        languageAccessor(UKRAINIAN) -> R.drawable.ic_flag_ukraine
        languageAccessor(TURKISH) -> R.drawable.ic_flag_turkey
        languageAccessor(MALAYSIAN) -> R.drawable.ic_flag_malaysia
        languageAccessor(BENGAL) -> R.drawable.ic_flag_bangladesh
        languageAccessor(VIETNAMESE) -> R.drawable.ic_flag_vietnam
        languageAccessor(PERSIAN) -> R.drawable.ic_flag_iran
        languageAccessor(CZECH) -> R.drawable.ic_flag_czech_republic
        languageAccessor(DUTCH) -> R.drawable.ic_flag_netherlands
        languageAccessor(ROMANIAN) -> R.drawable.ic_flag_romania
        languageAccessor(THAI) -> R.drawable.ic_flag_thailand
        languageAccessor(DANISH) -> R.drawable.ic_flag_denmark
        languageAccessor(FINNISH) -> R.drawable.ic_flag_finland
        languageAccessor(SWEDISH) -> R.drawable.ic_flag_sweden
        languageAccessor(NORWEGIAN) -> R.drawable.ic_flag_norway
        languageAccessor(HEBREW) -> R.drawable.ic_flag_israel
        else -> throw RuntimeException("$languageCode Unknown language")
      }
    }
  }
}