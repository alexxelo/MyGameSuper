package com.ilyin.settings.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilyin.settings.feature.togglesetting.ToggleSettingVm

@Composable
fun GeneralToggleOption(
  modifier: Modifier = Modifier,
  vm: ToggleSettingVm,
  text: String = "",
  enabledText: String = text,
  disabledText: String = enabledText,
) {
  val checked by vm.checked.observeAsState()
  val enabled by vm.enabled.observeAsState()

  val checkedSafe = checked ?: return
  val enabledSafe = enabled ?: return

  GeneralToggleOption(
    modifier = modifier,
    enabledText = enabledText,
    disabledText = disabledText,
    checked = checkedSafe,
    enabled = enabledSafe,
    onToggle = vm::toggle,
  )
}

@Composable
fun GeneralToggleOption(
  modifier: Modifier = Modifier,
  enabledText: String = "",
  disabledText: String = enabledText,
  checked: Boolean = false,
  enabled: Boolean = false,
  onChanged: (Boolean) -> Unit = {},
  onToggle: () -> Unit = {},
) {
  val onCheckedChanged: (Boolean) -> Unit = {
    onChanged(!checked)
    onToggle()
  }
  val interactionSource = remember { MutableInteractionSource() }
  Row(
    modifier = modifier.clickable(
      interactionSource = interactionSource,
      indication = null,
      enabled = enabled,
      onClick = {
        onCheckedChanged(!checked)
      }),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text(
      text = if (checked) enabledText else disabledText,
      fontWeight = FontWeight.SemiBold,
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier
        .weight(1f)
        .padding(start = 16.dp),
    )
    Switch(
      enabled = enabled,
      checked = checked,
      onCheckedChange = onCheckedChanged,
      modifier = Modifier.padding(end = 16.dp),
      interactionSource = interactionSource
    )
  }
}

@Preview
@Composable
fun GeneralOptionPreview(modifier: Modifier = Modifier) {
  GeneralToggleOption(
    modifier = modifier,
    enabledText = "Включить настроечку",
  )
}

@Preview
@Composable
fun GeneralOptionPreview2(modifier: Modifier = Modifier) {
  GeneralToggleOption(
    modifier = modifier,
    enabledText = "Включить настроечку",
    enabled = true,
  )
}