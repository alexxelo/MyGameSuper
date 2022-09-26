package com.example.core.views


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.AppViewModel
import com.example.core.colors.ThemeMaterial3
import com.example.core.navigation.AppNavGraph

@Composable
fun GameAppView(modifier: Modifier = Modifier, vm: AppViewModel, languageChanged: () -> Unit = {},) {
  val hideSystem by vm.hideSystemUi.observeAsState()
  val nighMode by vm.nightMode.observeAsState()
  val selectedTheme by vm.selectedTheme.observeAsState()
  val selectedThemeSafe = selectedTheme ?: return
  val nighModeSafe = nighMode ?: return


  ThemeMaterial3(appTheme = selectedThemeSafe, darkTheme = nighModeSafe) {

    AppNavGraph(modifier = modifier, languageChanged = languageChanged)
  }
}


@Preview
@Composable
fun GameAppViewPreview(modifier: Modifier = Modifier) {
  //GameAppView(modifier = modifier)
}