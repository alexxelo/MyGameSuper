package com.first.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ilyin.settings.feature.hidesystemui.SystemUiHideMemory
import com.ilyin.settings.feature.nightmode.NightModeMemory
import com.ilyin.settings.feature.themepicker.ThemeController
import com.ilyin.ui_core_compose.themes.AppTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class AppViewModelImpl @Inject constructor(
  savedStateHandle: SavedStateHandle,
  nightModeMemory: NightModeMemory,
  uiHideMemory: SystemUiHideMemory,
  themeController: ThemeController,
) : ViewModel(), AppViewModel {

  private val _nightMode = savedStateHandle.getLiveData("nightMode", nightModeMemory.isNightMode)
  override val nightMode: LiveData<Boolean> = _nightMode

  private val _hideSystemUi = savedStateHandle.getLiveData<Boolean>("hideSystemUi")
  override val hideSystemUi: LiveData<Boolean> = _hideSystemUi

  private val _selectedTheme = savedStateHandle.getLiveData<AppTheme>("selectedTheme")
  override val selectedTheme: LiveData<AppTheme> = _selectedTheme

  private var nightModeDisposable: Disposable = nightModeMemory.isNightModeObservable
    .subscribe({
      _nightMode.value = it
    }, Timber::e)

  private var uiHideDisposable: Disposable = uiHideMemory.isHiddenObservable
    .subscribe({
      _hideSystemUi.value = it
    }, Timber::e)

  private var themeDisposable: Disposable = themeController.onCurrentThemeChanged()
    .subscribe({
      _selectedTheme.value = it
    }, Timber::e)

  override fun onCleared() {
    super.onCleared()
    nightModeDisposable.dispose()
    uiHideDisposable.dispose()
    themeDisposable.dispose()
  }
}
