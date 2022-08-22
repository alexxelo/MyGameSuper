package com.ilyin.tools_android.crud

import android.content.SharedPreferences

open class LongPref constructor(prefs: SharedPreferences, private val default: Long = 0) : PreferencesWrapper(prefs) {

  var value: Long
    get() = getLong(PREF_VALUE, default)
    set(value) = save(PREF_VALUE, value)

  companion object {
    private const val PREF_VALUE = "PREF_VALUE"
  }
}