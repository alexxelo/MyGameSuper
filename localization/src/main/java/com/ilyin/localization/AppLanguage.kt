package com.ilyin.localization

import java.io.Serializable
import java.util.*

data class AppLanguage constructor(
  val language: String,
  val countryCode: String? = null,
  val isDefault: Boolean = false,
  val developStatusPercent: Int = 100,
) : Serializable {

  companion object {
    val RUSSIAN = lang(language = "ru", developStatusPercent = 100)
    val POLISH = lang(language = "pl", developStatusPercent = 100)
    val UKRAINIAN = lang(language = "uk", developStatusPercent = 100)
    val GERMAN = lang(language = "de", developStatusPercent = 99)
    val SPAIN = lang(language = "es", countryCode = "ES", isDefault = true, developStatusPercent = 100)
    val SPAIN_MEXICO = lang(language = "es", countryCode = "MX", developStatusPercent = 100)
    val ENGLISH = lang(language = "en", developStatusPercent = 100)
    val HINDI = lang(language = "hi", developStatusPercent = 97)
    val KOREAN = lang(language = "ko", developStatusPercent = 100)
    val ITALIAN = lang(language = "it", developStatusPercent = 100)
    val JAPAN = lang(language = "ja", developStatusPercent = 100)
    val FRENCH = lang(language = "fr", developStatusPercent = 100)
    val CHINESE = lang(language = "zh", developStatusPercent = 99)
    val PORTUGAL_BRAZIL = lang(language = "pt", countryCode = "BR", isDefault = true, developStatusPercent = 99)
    val PORTUGAL_PORTUGAL = lang(language = "pt", countryCode = "PT", developStatusPercent = 99)
    val ARABIC = lang(language = "ar", developStatusPercent = 99)
    val INDONESIAN = lang(language = "in", developStatusPercent = 98)
    val TURKISH = lang(language = "tr", developStatusPercent = 99)
    val MALAYSIAN = lang(language = "ms", developStatusPercent = 78)
    val BENGAL = lang(language = "bn", developStatusPercent = 58)
    val VIETNAMESE = lang(language = "vi", developStatusPercent = 98)
    val PERSIAN = lang(language = "fa", developStatusPercent = 79)
    val CZECH = lang(language = "cs", developStatusPercent = 98)
    val DUTCH = lang(language = "nl", developStatusPercent = 94)
    val ROMANIAN = lang(language = "ro", developStatusPercent = 79)
    val THAI = lang(language = "th", developStatusPercent = 91)
    val DANISH = lang(language = "da", developStatusPercent = 85)
    val FINNISH = lang(language = "fi", developStatusPercent = 89)
    val SWEDISH = lang(language = "sv", developStatusPercent = 89)
    val NORWEGIAN = lang(language = "no", developStatusPercent = 78)
    val HEBREW = lang(language = "iw", developStatusPercent = 82)

    fun lang(language: String, countryCode: String? = null, isDefault: Boolean = false, developStatusPercent: Int = 0): AppLanguage {
      return AppLanguage(
        language = language,
        countryCode = countryCode,
        isDefault = isDefault,
        developStatusPercent = developStatusPercent,
      )
    }

    val DEFAULT = ENGLISH

    fun getBy(locale: Locale): AppLanguage {
      return getBy(locale.language, locale.country)
    }

    fun allLanguages(): List<AppLanguage> {
      return listOf(
        // Западная Европа
        ENGLISH,
        GERMAN,
        FRENCH,
        ITALIAN,
        SPAIN,
        SPAIN_MEXICO,
        PORTUGAL_BRAZIL,
        PORTUGAL_PORTUGAL,
        DUTCH,

        // Восточная Европа
        RUSSIAN,
        POLISH,
        UKRAINIAN,
        ROMANIAN,
        CZECH,

        // Северная Европа
        DANISH,
        FINNISH,
        SWEDISH,
        NORWEGIAN,

        // Восточная Азия
        CHINESE,
        JAPAN,
        KOREAN,

        // Южная Азия
        BENGAL,
        HINDI,

        // Юго-Восточная Азия
        INDONESIAN,
        MALAYSIAN,
        THAI,
        VIETNAMESE,

        ARABIC,
        PERSIAN,
        HEBREW,
        TURKISH,
      )
    }


    fun from(locale: Locale): AppLanguage {
      return AppLanguage(language = locale.language, countryCode = locale.country)
    }

    fun getBy(languageCode: String, countryCode: String?): AppLanguage {
      val languagesByLangCode = allLanguages().filter { it.language == languageCode }
      return when {
        languagesByLangCode.isEmpty() -> DEFAULT
        languagesByLangCode.size == 1 -> languagesByLangCode[0]
        countryCode.isNullOrBlank() -> languagesByLangCode[0]
        else -> languagesByLangCode.firstOrNull { it.countryCode == countryCode } ?: languagesByLangCode.find { it.isDefault }
        ?: languagesByLangCode[0]
      }
    }
  }
}