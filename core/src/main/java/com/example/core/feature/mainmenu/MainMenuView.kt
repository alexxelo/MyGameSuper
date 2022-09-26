package com.example.core.feature.mainmenu

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults.textButtonColors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R
import com.example.core.feature.mainmenu.market.MarketCardView
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
  val menuTextSize = 24.sp
  Box(
    modifier = Modifier
      .fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Button(
        modifier = Modifier.padding(2.dp),
        onClick = {
          onClickPlay();
          vm.playClickSound()
        })
      {

        Text(
          text = stringResource(id = com.ilyin.localization.R.string.play),
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))

        Image(
          painter = painterResource(id = R.drawable.play),
          contentDescription = "",
          modifier = Modifier.size(18.dp)
        )
      }
      Button(
        modifier = Modifier.padding(2.dp),
        onClick = {
          onClickSettings();
          vm.playClickSound()
        }) {
        Image(
          painter = painterResource(id = R.drawable.settings),
          contentDescription = "",
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        Text(
          text = stringResource(id = com.ilyin.localization.R.string.settings),
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Bold,
        )
      }
      Button(
        modifier = Modifier.padding(2.dp),
        onClick = {
          onClickTutorial();
          vm.playClickSound()
        }) {
        Image(
          painter = painterResource(id = R.drawable.questions),
          contentDescription = "",
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        Text(
          text = stringResource(id = com.ilyin.localization.R.string.howtoplay),
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Bold,
        )
      }
      MarketCardView(
        modifier = Modifier.padding(2.dp),
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

