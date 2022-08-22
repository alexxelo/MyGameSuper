package com.ilyin.settings.feature.langpicker

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ilyin.localization.AppLanguage
import com.ilyin.localization.LanguageUtils

@Composable
fun LanguagePickerDialog(
  vm: LanguagePickerVM,
  languageChanged: () -> Unit = {},
) {
  val languages = vm.languages
  val showLangPicker by vm.showLangPicker.observeAsState()
  val selectedLang by vm.selectedLang.observeAsState()

  val selectedLangSafe = selectedLang ?: return

  if (showLangPicker == true) {
    LanguagePickerDialog(
      languages = languages,
      selectedLanguage = selectedLangSafe,
      onLanguageClick = {
        if (vm.onLangPicked(it)) {
          languageChanged()
        }
        vm.dismissRequest()
      },
      dismissRequest = vm::dismissRequest,
    )
  }
}

@Composable
fun LanguagePickerDialog(
  languages: List<AppLanguage>,
  selectedLanguage: AppLanguage,
  onLanguageClick: (AppLanguage) -> Unit = {},
  dismissRequest: () -> Unit = {},
) {
  Dialog(onDismissRequest = dismissRequest) {
    LanguagePickerDialogContent(
      languages = languages,
      selectedLanguage = selectedLanguage,
      onLanguageClick = {
        onLanguageClick(it)
        dismissRequest()
      },
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguagePickerDialogContent(
  modifier: Modifier = Modifier,
  languages: List<AppLanguage>,
  selectedLanguage: AppLanguage,
  onLanguageClick: (AppLanguage) -> Unit = {},
) {
  Card(modifier = modifier) {
    LazyColumn(
      contentPadding = PaddingValues(all = 8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      content = {
        items(languages) { lang ->
          LanguageItem(
            lang = lang,
            isSelected = lang == selectedLanguage,
            onClick = { onLanguageClick(lang) },
          )
        }
      },
      modifier = Modifier.fillMaxSize(1f),
    )
  }
}

private val langItemShape = RoundedCornerShape(12.dp)
private val imageModifier = Modifier.size(32.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageItem(
  modifier: Modifier = Modifier,
  lang: AppLanguage,
  isSelected: Boolean,
  onClick: () -> Unit = {},
) {
  val interactionSource = remember { MutableInteractionSource() }
  Card(
    colors = CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
    modifier = modifier
      .clip(langItemShape)
      .clickable(
        interactionSource = interactionSource,
        indication = rememberRipple(),
        onClick = onClick,
      ),
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .padding(8.dp)
        .semantics(true) {}
    ) {
      Text(
        text = LanguageUtils.computeLanguageDisplay(lang),
        modifier = Modifier.weight(1f),
      )

      Image(
        painter = painterResource(id = LanguageUtils.getDrawableBy(lang)),
        contentDescription = null, // decorative
        modifier = imageModifier
      )
    }
  }
}

@Composable
@Preview
fun LanguagePickerDialogContentPreview(modifier: Modifier = Modifier) {
  val languages = AppLanguage.allLanguages()
  LanguagePickerDialogContent(
    modifier = modifier,
    languages = languages,
    selectedLanguage = languages[3],
  )
}