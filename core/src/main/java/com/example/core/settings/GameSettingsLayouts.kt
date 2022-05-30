package com.ilyinp.core.feature.settings

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilyin.ui_core_compose.isScreenNormal

@Composable
fun GameSettingsLayouts(
  modifier: Modifier = Modifier,
  nightModeView: @Composable (Modifier) -> Unit = {},
  hideSystemUiView: @Composable (Modifier) -> Unit = {},
  themePickerView: @Composable (Modifier) -> Unit = {},
  languagePickerView: @Composable (Modifier) -> Unit = {},
  soundView: @Composable (Modifier) -> Unit = {},
  musicView: @Composable (Modifier) -> Unit = {},
) {
  Box(modifier = modifier) {
    Spacer(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.65f)),
    )
    if (isScreenNormal()) {
      LayoutNormal(
        modifier = modifier,
        nightModeView = nightModeView,
        hideSystemUiView = hideSystemUiView,
        themePickerView = themePickerView,
        languagePickerView = languagePickerView,
        soundView = soundView,
        musicView = musicView,
      )
    } else {
      LayoutWide(
        modifier = modifier,
        nightModeView = nightModeView,
        hideSystemUiView = hideSystemUiView,
        themePickerView = themePickerView,
        languagePickerView = languagePickerView,
        soundView = soundView,
        musicView = musicView,
      )
    }
  }
}

@Composable
private fun LayoutWide(
  modifier: Modifier = Modifier,
  nightModeView: @Composable (Modifier) -> Unit = {},
  hideSystemUiView: @Composable (Modifier) -> Unit = {},
  themePickerView: @Composable (Modifier) -> Unit = {},
  languagePickerView: @Composable (Modifier) -> Unit = {},
  soundView: @Composable (Modifier) -> Unit = {},
  musicView: @Composable (Modifier) -> Unit = {},
) {
  Column(
    modifier = modifier.verticalScroll(
      state = rememberScrollState(),
      flingBehavior = ScrollableDefaults.flingBehavior(),
    ),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Title()
    Row(
      modifier = Modifier
        .fillMaxWidth(1f)
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      Card1(
        modifier = Modifier.weight(1f),
        nightModeView = nightModeView,
        hideSystemUiView = hideSystemUiView,
        themePickerView = themePickerView,
        languagePickerView = languagePickerView,
      )
      Card2(
        modifier = Modifier.weight(1f),
        soundView = soundView,
        musicView = musicView,
      )
    }
  }
}

@Composable
private fun LayoutNormal(
  modifier: Modifier = Modifier,
  nightModeView: @Composable (Modifier) -> Unit = {},
  hideSystemUiView: @Composable (Modifier) -> Unit = {},
  themePickerView: @Composable (Modifier) -> Unit = {},
  languagePickerView: @Composable (Modifier) -> Unit = {},
  soundView: @Composable (Modifier) -> Unit = {},
  musicView: @Composable (Modifier) -> Unit = {},
) {
  Column(
    modifier = modifier
      .verticalScroll(
        state = rememberScrollState(),
        flingBehavior = ScrollableDefaults.flingBehavior(),
      ),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Title()
    Card1(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      nightModeView = nightModeView,
      hideSystemUiView = hideSystemUiView,
      themePickerView = themePickerView,
      languagePickerView = languagePickerView,
    )
    Card2(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      soundView = soundView,
      musicView = musicView,
    )
    Spacer(modifier = Modifier.size(48.dp))
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Card1(
  modifier: Modifier = Modifier,
  nightModeView: @Composable (Modifier) -> Unit = {},
  hideSystemUiView: @Composable (Modifier) -> Unit = {},
  themePickerView: @Composable (Modifier) -> Unit = {},
  languagePickerView: @Composable (Modifier) -> Unit = {},
) {
  Card(modifier = modifier) {
    Column(
      verticalArrangement = Arrangement.spacedBy(4.dp),
      modifier = Modifier
        .padding(vertical = 8.dp),
    ) {
      nightModeView(Modifier.fillMaxWidth())
      hideSystemUiView(Modifier.fillMaxWidth())
      themePickerView(Modifier.fillMaxWidth())
      languagePickerView(Modifier.fillMaxWidth())
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Card2(
  modifier: Modifier = Modifier,
  soundView: @Composable (Modifier) -> Unit = {},
  musicView: @Composable (Modifier) -> Unit = {},
) {
  Card(modifier = modifier.animateContentSize()) {
    Column(
      verticalArrangement = Arrangement.spacedBy(4.dp),
      modifier = Modifier
        .padding(vertical = 8.dp)
        .animateContentSize(),
    ) {
      soundView(Modifier.fillMaxWidth())
      musicView(Modifier.fillMaxWidth())
    }
  }
}

@Composable
fun Title(modifier: Modifier = Modifier) {
  Text(
    text = stringResource(id = com.ilyin.settings.R.string.settings),
    style = MaterialTheme.typography.headlineSmall,
    fontWeight = FontWeight.Bold,
    modifier = modifier
      .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
      .padding(16.dp)
      .fillMaxWidth()
  )
}

@Preview
@Composable
fun AlchemySettingsLayoutsPreview(modifier: Modifier = Modifier) {
  GameSettingsViewPreview(modifier = modifier)
}