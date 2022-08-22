package com.ilyin.settings.feature.themepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ilyin.settings.R
import com.ilyin.ui_core_compose.isLight
import com.ilyin.ui_core_compose.themes.AppTheme

@Composable
fun ThemePickerDialog(
  vm: AppThemePickerVM,
) {
  val showThemePicker by vm.showThemePicker.observeAsState()
  val themes = vm.themes
  val selectedTheme by vm.selectedTheme.observeAsState()

  if (showThemePicker == true) {
    ThemePickerDialog(
      selectedTheme = selectedTheme,
      themes = themes,
      onThemeClick = vm::onThemePicked,
      dismissRequest = vm::dismissRequest,
    )
  }
}

@Composable
fun ThemePickerDialog(
  selectedTheme: AppTheme? = AppTheme.baseTheme(),
  themes: List<AppTheme> = AppTheme.baseThemes(),
  onThemeClick: (AppTheme) -> Unit = {},
  dismissRequest: () -> Unit = {},
) {
  Dialog(onDismissRequest = dismissRequest) {
    ThemePickerDialogContent(
      selectedTheme = selectedTheme,
      themeChunks = themes.chunked(4),
      onThemeClick = { theme ->
        onThemeClick(theme)
        dismissRequest()
      },
    )
  }
}

private val chunkArrangement = Arrangement.spacedBy(4.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemePickerDialogContent(
  modifier: Modifier = Modifier,
  selectedTheme: AppTheme? = AppTheme.baseTheme(),
  themeChunks: List<List<AppTheme>> = AppTheme.baseThemes().chunked(4),
  onThemeClick: (AppTheme) -> Unit = {},
) {
  Card(modifier = modifier) {
    LazyColumn(
      modifier = Modifier,
      contentPadding = PaddingValues(8.dp),
    ) {
      items(themeChunks) { chunk ->
        Row(horizontalArrangement = chunkArrangement) {
          chunk.forEach { theme ->
            ThemeButton(
              onClick = { onThemeClick(theme) },
              theme = theme,
              isSelected = selectedTheme == theme,
            )
          }
        }
      }
    }
  }
}

@Composable
fun ThemeButton(
  onClick: () -> Unit = {},
  theme: AppTheme,
  isSelected: Boolean = false,
) {
  IconButton(
    onClick = onClick,
    modifier = Modifier
      .padding(4.dp)
      .background(
        color = if (isSelected) {
          Color(theme.primary)
            .copy(alpha = if (MaterialTheme.isLight()) 0.25f else 0.75f)
            .compositeOver(MaterialTheme.colorScheme.surfaceVariant)
        } else {
          Color.Transparent
        },
        shape = CircleShape
      )
  ) {
    Icon(
      imageVector = Icons.Default.Circle,
      tint = Color(theme.primary),
      contentDescription = stringResource(id = R.string.select_color),
      modifier = Modifier.border(
        width = 2.dp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = CircleShape,
      )
    )
  }
}

@Preview
@Composable
private fun ThemePickerDialogContentPreview() {
  ThemePickerDialogContent()
}