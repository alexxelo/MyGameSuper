package com.ilyin.settings.feature.themepicker


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilyin.settings.R
import com.ilyin.ui_core_compose.colors.MdColors
import com.ilyin.ui_core_compose.isLight

@Composable
fun ColorSchemeOption(
  modifier: Modifier = Modifier,
  vm: AppThemePickerVM,
) {
  val selectedTheme by vm.selectedTheme.observeAsState()
  val showThemePicker by vm.showThemePicker.observeAsState()

  ColorSchemeOption(
    modifier = modifier,
    onClick = vm::show,
    color = selectedTheme?.primary?.let(::Color) ?: return,
  )

  if (showThemePicker == true) {
    ThemePickerDialog(vm = vm)
  }
}

@Composable
fun ColorSchemeOption(
  modifier: Modifier = Modifier,
  onClick: () -> Unit = {},
  color: Color = MdColors.green.c700,
) {
  val interactionSource = remember { MutableInteractionSource() }
  Row(
    modifier = modifier.clickable(
      interactionSource = interactionSource,
      indication = null,
      onClick = onClick,
    ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Text(
      text = stringResource(R.string.color_theme),
      fontWeight = FontWeight.SemiBold,
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier
        .weight(1f)
        .padding(start = 16.dp),
    )
    IconButton(
      onClick = onClick,
      modifier = Modifier
        .padding(end = 16.dp)
        .background(
          color = color.copy(alpha = if (MaterialTheme.isLight()) 0.25f else 0.75f),
          shape = CircleShape
        ),
      interactionSource = interactionSource
    ) {
      Icon(
        tint = color,
        imageVector = Icons.Default.Circle,
        contentDescription = stringResource(R.string.select_color),
        modifier = Modifier.border(
          width = 2.dp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          shape = CircleShape,
        )
      )
    }
  }
}

@Preview
@Composable
fun ColorSchemeOptionPreview(modifier: Modifier = Modifier) {
  ColorSchemeOption(modifier = modifier)
}