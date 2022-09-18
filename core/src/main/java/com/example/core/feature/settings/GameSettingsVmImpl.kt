package com.example.core.feature.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.core.feature.settings.sound.SoundEnabledOptionVM
import com.example.core.feature.settings.sound.SoundEnabledOptionVMImpl
import com.example.core.feature.sounds.GameSounds
import com.ilyin.localization.LocaleController
import com.ilyin.settings.feature.hidesystemui.HideSystemUiSettingVM
import com.ilyin.settings.feature.hidesystemui.HideSystemUiSettingVMImpl
import com.ilyin.settings.feature.hidesystemui.SystemUiHideMemory
import com.ilyin.settings.feature.langpicker.LanguagePickerVM
import com.ilyin.settings.feature.langpicker.LanguagePickerVMImpl
import com.ilyin.settings.feature.nightmode.NightModeMemory
import com.ilyin.settings.feature.nightmode.NightModeSettingVM
import com.ilyin.settings.feature.nightmode.NightModeSettingVMImpl
import com.ilyin.settings.feature.sound.SoundEnableMemory
import com.ilyin.settings.feature.themepicker.AppThemePickerVM
import com.ilyin.settings.feature.themepicker.AppThemePickerVMImpl
import com.ilyin.settings.feature.themepicker.ThemeController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameSettingsVmImpl @Inject constructor(
  savedStateHandle: SavedStateHandle,
  nightModeMemory: NightModeMemory,
  soundEnableMemory: SoundEnableMemory,
  systemUiHideMemory: SystemUiHideMemory,
  themeController: ThemeController,
  localeController: LocaleController,
  gameSounds: GameSounds,
) : ViewModel(), GameSettingsVM {

  override val themePickerVm: AppThemePickerVM = AppThemePickerVMImpl(
    savedStateHandle = savedStateHandle,
    themeController = themeController,
    onShow = {
      gameSounds.playGeneralClick()
    },
    onThemePicked = {
      gameSounds.playGeneralClick()
    },
  )

  override val nightModeVm: NightModeSettingVM = NightModeSettingVMImpl(
    savedStateHandle = savedStateHandle,
    memory = nightModeMemory,
    onToggle = {
      gameSounds.playGeneralClick()
    },
  )

  override val langPickerVm: LanguagePickerVM = LanguagePickerVMImpl(
    savedStateHandle = savedStateHandle,
    localeController = localeController,
    onShow = {
      gameSounds.playGeneralClick()
    },
  )

  override val hideSystemUiSettingVm: HideSystemUiSettingVM = HideSystemUiSettingVMImpl(
    savedStateHandle = savedStateHandle,
    memory = systemUiHideMemory,
    onToggle = {
      gameSounds.playGeneralClick()
    },
  )

  override val soundEnabledOptionVm: SoundEnabledOptionVM = SoundEnabledOptionVMImpl(
    savedStateHandle = savedStateHandle,
    memory = soundEnableMemory,
    gameSounds = gameSounds,
  )

}