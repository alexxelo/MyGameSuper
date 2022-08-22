package com.ilyin.settings.feature.themepicker

import com.ilyin.ui_core_compose.themes.AppTheme
import io.reactivex.rxjava3.core.Observable

interface ThemeController {

  val themes: List<AppTheme>

  fun onCurrentThemeChanged(): Observable<AppTheme>

  var theme: AppTheme
}