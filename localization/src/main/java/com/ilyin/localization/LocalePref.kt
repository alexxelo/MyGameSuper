package com.ilyin.localization

import android.content.Context
import com.ilyin.tools_android.crud.PreferencesWrapper
import com.ilyin.tools_android.util.prefs

class LocalePref constructor(ctx: Context) : PreferencesWrapper(ctx.prefs("LocalePref")) {

  var defaultLanguageCode: String? by StringPrefNullable(this, "PREF_DEFAULT_LANGUAGE")
  var defaultCountryCode: String? by StringPrefNullable(this, "PREF_DEFAULT_COUNTRY")

  var languageCode: String? by StringPrefNullable(this, "PREF_LANGUAGE")
  var countryCode: String? by StringPrefNullable(this, "PREF_COUNTRY")

}