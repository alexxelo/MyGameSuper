package com.example.core.feature.mainmenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.core.GameScreenView
import com.example.core.feature.mainmenu.market.MarketCardView
import com.example.core.feature.mainmenu.play.MainMenuPlayView
import com.ilyin.ui_core_compose.colors.MdColors

@Composable
fun MainMenuView(
  modifier: Modifier = Modifier,
  vm: MainMenuVM,
  onClickPlay: () -> Unit = {},
  onClickTutorial: () -> Unit = {},
  onClickSettings: () -> Unit = {},
  onClickStore: () -> Unit = {}
) {
  val menuTextSize = 34.sp
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(color = MdColors.pink.c100),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      TextButton(onClick = { onClickPlay() }) {
        Text(
          text = "123",//stringResource(id = com.ilyin.localization.R.string.play),
          textAlign = TextAlign.Center,
          fontSize = menuTextSize
        )
      }
      TextButton(onClick = { onClickTutorial() }) {
        Text(
          text = stringResource(id = com.ilyin.localization.R.string.tutorial),
          textAlign = TextAlign.Center,
          fontSize = menuTextSize
        )
      }
      TextButton(onClick = { onClickStore() }) {
        Text(
          text = stringResource(id = com.ilyin.localization.R.string.store),
          textAlign = TextAlign.Center,
          fontSize = menuTextSize
        )
      }
      TextButton(onClick = { onClickSettings() }) {
        Text(
          text = stringResource(id = com.ilyin.localization.R.string.settings),
          textAlign = TextAlign.Center,
          fontSize = menuTextSize
        )
      }/*
      TextButton(onClick = {}) {
        Text(
          // need change value text
          text = stringResource(id = com.example.auth.R.string.sign_in),
          textAlign = TextAlign.Center,
          fontSize = menuTextSize
        )
      }*/
      MarketCardView(
        modifier = modifier.fillMaxWidth(),
        vm = vm.marketCardVM,
        onCardClick = {
          vm.playClickSound()
        }
      )
    }
  }
}

@Composable
fun MainMenuView(
  modifier: Modifier,
  background: @Composable (modifier: Modifier) -> Unit = {},
  marketCard: @Composable (modifier: Modifier) -> Unit = {},
  play: @Composable (modifier: Modifier) -> Unit = {},
  settings: @Composable (modifier: Modifier) -> Unit = {},
) {

  MainMenuLayouts(
    modifier = modifier,
    background = background,
    marketCard = marketCard,
    play = play,
    settings = settings
  )

}

@Composable
@Preview
fun MainMenuViewPreview(modifier: Modifier = Modifier) {
  //MainMenuView()
}