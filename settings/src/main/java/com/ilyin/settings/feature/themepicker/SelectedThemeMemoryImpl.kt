package com.ilyin.settings.feature.themepicker

import android.content.Context
import com.ilyin.tools_android.crud.PreferencesWrapper
import com.ilyin.tools_android.util.prefs

class SelectedThemeMemoryImpl constructor(ctx: Context) : SelectedThemeMemory, PreferencesWrapper(ctx.prefs("ThemeMemoryImpl")) {

  override var themeId: String by StringPref(this, "themeId")
}