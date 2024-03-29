package com.first.core.feature.settings

import com.first.core.feature.settings.sound.SoundEnabledOptionVM
import com.ilyin.settings.feature.hidesystemui.HideSystemUiSettingVM
import com.ilyin.settings.feature.langpicker.LanguagePickerVM
import com.ilyin.settings.feature.nightmode.NightModeSettingVM
import com.ilyin.settings.feature.themepicker.AppThemePickerVM

interface GameSettingsVM {

  val themePickerVm: AppThemePickerVM
  val nightModeVm: NightModeSettingVM
  val langPickerVm: LanguagePickerVM
  val hideSystemUiSettingVm: HideSystemUiSettingVM
  val soundEnabledOptionVm: SoundEnabledOptionVM
}