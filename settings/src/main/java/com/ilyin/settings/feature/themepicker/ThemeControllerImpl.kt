package com.ilyin.settings.feature.themepicker

import com.ilyin.ui_core_compose.themes.AppTheme
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class ThemeControllerImpl constructor(private val memory: SelectedThemeMemory) : ThemeController {

  override val themes: List<AppTheme> = AppTheme.advancedThemes()
  private val onCurrentThemeChanged: BehaviorSubject<AppTheme> = BehaviorSubject.createDefault(theme)

  override var theme: AppTheme
    get() = getThemeById(memory.themeId)
    set(value) {
      val curThemeId = memory.themeId
      val newThemeId = value.id
      if (curThemeId != newThemeId) {
        memory.themeId = newThemeId
        onCurrentThemeChanged.onNext(theme)
      }
    }

  override fun onCurrentThemeChanged(): Observable<AppTheme> {
    return onCurrentThemeChanged
  }

  private fun getThemeById(id: String): AppTheme {
    return themes.find { it.id == id } ?: AppTheme.baseTheme()
  }
}