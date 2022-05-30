package com.ilyinp.core.feature.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ilyinp.core.feature.settings.sound.SoundEnabledOptionVM
import com.ilyinp.core.feature.settings.sound.SoundEnabledOptionVMImpl
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
import com.ilyinp.core.sounds.GameSounds
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
      gameSounds.playGeneralInterfaceClick()
    },
    onThemePicked = {
      gameSounds.playGeneralInterfaceClick()
    },
  )

  override val nightModeVm: NightModeSettingVM = NightModeSettingVMImpl(
    savedStateHandle = savedStateHandle,
    memory = nightModeMemory,
    onToggle = {
      gameSounds.playGeneralInterfaceClick()
    },
  )

  override val langPickerVm: LanguagePickerVM = LanguagePickerVMImpl(
    savedStateHandle = savedStateHandle,
    localeController = localeController,
    onShow = {
      gameSounds.playGeneralInterfaceClick()
    },
  )

  override val hideSystemUiSettingVm: HideSystemUiSettingVM = HideSystemUiSettingVMImpl(
    savedStateHandle = savedStateHandle,
    memory = systemUiHideMemory,
    onToggle = {
      gameSounds.playGeneralInterfaceClick()
    },
  )

  override val soundEnabledOptionVm: SoundEnabledOptionVM = SoundEnabledOptionVMImpl(
    savedStateHandle = savedStateHandle,
    memory = soundEnableMemory,
    gameSounds = gameSounds,
  )
}