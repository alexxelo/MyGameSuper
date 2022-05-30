package com.ilyinp.core.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ilyin.settings.R
import com.ilyin.settings.feature.langpicker.LangPickerOption
import com.ilyin.settings.feature.langpicker.LangPickerOptionPreview
import com.ilyin.settings.feature.themepicker.ColorSchemeOption
import com.ilyin.settings.feature.themepicker.ColorSchemeOptionPreview
import com.ilyin.settings.view.GeneralOptionPreview
import com.ilyin.settings.view.GeneralToggleOption

@Composable
fun GameSettingsView(
  modifier: Modifier = Modifier,
  vm: GameSettingsVM,
  languageChanged: () -> Unit = {},
) {
  GameSettingsView(
    modifier = modifier,
    nightModeView = {
      GeneralToggleOption(
        modifier = it,
        vm = vm.nightModeVm,
        text = stringResource(id = R.string.night_mode),
      )
    },
    hideSystemUiView = {
      GeneralToggleOption(
        modifier = it,
        vm = vm.hideSystemUiSettingVm,
        text = stringResource(R.string.hide_system_ui),
      )
    },
    themePickerView = {
      ColorSchemeOption(
        vm = vm.themePickerVm,
        modifier = it,
      )
    },
    languagePickerView = {
      LangPickerOption(
        vm = vm.langPickerVm,
        modifier = it,
        languageChanged = languageChanged,
      )
    },
    soundView = {
      GeneralToggleOption(
        vm = vm.soundEnabledOptionVm,
        text = stringResource(R.string.enable_sound),
        modifier = it,
      )
    },
  )
}

@Composable
fun GameSettingsView(
  modifier: Modifier = Modifier,
  nightModeView: @Composable (Modifier) -> Unit = {},
  hideSystemUiView: @Composable (Modifier) -> Unit = {},
  themePickerView: @Composable (Modifier) -> Unit = {},
  languagePickerView: @Composable (Modifier) -> Unit = {},
  soundView: @Composable (Modifier) -> Unit = {},
) {
  GameSettingsLayouts(
    modifier = modifier,
    nightModeView = nightModeView,
    hideSystemUiView = hideSystemUiView,
    themePickerView = themePickerView,
    languagePickerView = languagePickerView,
    soundView = soundView,
  )
}

@Preview
@Composable
fun GameSettingsViewPreview(modifier: Modifier = Modifier) {
  GameSettingsView(
    modifier = modifier,
    nightModeView = { GeneralOptionPreview(it) },
    hideSystemUiView = { GeneralOptionPreview(it) },
    themePickerView = { ColorSchemeOptionPreview(it) },
    languagePickerView = { LangPickerOptionPreview(it) },
    soundView = { GeneralOptionPreview(it) },
  )
}