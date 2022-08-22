package com.ilyin.settings.feature.langpicker


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilyin.localization.AppLanguage
import com.ilyin.localization.LanguageUtils
import com.ilyin.localization.R

@Composable
fun LangPickerOption(
  modifier: Modifier = Modifier,
  vm: LanguagePickerVM,
  languageChanged: () -> Unit = {},
) {
  val showLangPicker by vm.showLangPicker.observeAsState()
  val selectedLang by vm.selectedLang.observeAsState()

  val selectedLangSafe = selectedLang ?: return

  LangPickerOption(
    modifier = modifier,
    appLanguage = selectedLangSafe,
    onClick = vm::show
  )

  if (showLangPicker == true) {
    LanguagePickerDialog(
      vm = vm,
      languageChanged = languageChanged,
    )
  }
}

@Composable
fun LangPickerOption(
  modifier: Modifier = Modifier,
  appLanguage: AppLanguage,
  onClick: () -> Unit = {},
) {
  val interactionSource = remember { MutableInteractionSource() }
  Row(
    modifier = modifier.clickable(
      interactionSource = interactionSource,
      indication = null,
      onClick = onClick,
    ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Text(
      text = stringResource(id = R.string.language),
      fontWeight = FontWeight.SemiBold,
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier
        .weight(1f)
        .padding(start = 16.dp),
    )
    Text(
      text = LanguageUtils.computeLanguageDisplay(appLanguage),
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier.alpha(alpha = 0.54f)
    )
    IconButton(
      onClick = onClick,
      modifier = Modifier.padding(end = 16.dp),
      interactionSource = interactionSource
    ) {
      Image(
        painter = painterResource(id = LanguageUtils.getDrawableBy(appLanguage)),
        contentDescription = stringResource(id = R.string.menu_language_content_descr),
      )
    }
  }
}

@Preview
@Composable
fun LangPickerOptionPreview(modifier: Modifier = Modifier) {
  LangPickerOption(
    modifier = modifier,
    appLanguage = AppLanguage.DEFAULT,
  )
}