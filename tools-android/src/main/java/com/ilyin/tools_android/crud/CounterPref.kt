package com.ilyin.tools_android.crud

import android.content.SharedPreferences

abstract class CounterPref constructor(prefs: SharedPreferences) : PreferencesWrapper(prefs) {

  var value: Int
    get() = getInt(COUNTER_PREF, 0)
    set(value) = save(COUNTER_PREF, value)

  fun inc(): Int {
    value++
    return value
  }

  companion object {
    const val COUNTER_PREF = "COUNTER_PREF"
  }
}