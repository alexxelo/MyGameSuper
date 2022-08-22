package com.ilyin.tools_android.crud

import android.content.SharedPreferences

abstract class FlagPref constructor(
  val defaultValueComputer: () -> Boolean = { false },
  prefs: SharedPreferences
) : PreferencesWrapper(prefs) {

  var value: Boolean
    get() = getBool(FLAG_PREF, defaultValueComputer())
    set(value) = save(FLAG_PREF, value)

  var valueNullable: Boolean?
    get() = if (prefs.contains(FLAG_PREF)) value else null
    set(value) = save(FLAG_PREF, value)

  companion object {
    private const val FLAG_PREF = "FLAG_PREF"
  }
}