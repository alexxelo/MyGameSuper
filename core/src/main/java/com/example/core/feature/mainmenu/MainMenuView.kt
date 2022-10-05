package com.example.core.feature.mainmenu

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.R
import com.example.core.feature.mainmenu.market.MarketCardView

@Composable
fun MainMenuView(
  modifier: Modifier = Modifier,
  vm: MainMenuVM,
  onClickPlay: () -> Unit = {},
  onClickTutorial: () -> Unit = {},
  onClickSettings: () -> Unit = {},
  onClickBackToGame: () -> Unit = {}

) {
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
          onClickBackToGame();
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

        Icon(
          painter = painterResource(id = R.drawable.play),
          contentDescription = "",
          modifier = Modifier.size(20.dp)
        )
      }
      Button(
        modifier = Modifier.padding(2.dp),
        onClick = {
          onClickSettings();
          vm.playClickSound()
        }) {
        Icon(
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
        Icon(
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
@Preview
@Composable
fun MainMenuViewPreview() {
  //MainMenuView(modifier = Modifier, vm = )
}

