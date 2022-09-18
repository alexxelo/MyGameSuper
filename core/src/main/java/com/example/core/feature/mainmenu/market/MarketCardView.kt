package com.example.core.feature.mainmenu.market

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.feature.mainmenu.market.signin.SignInBtn
import com.example.core.feature.mainmenu.market.signin.SignInBtnPreview


@Composable
fun MarketCardView(
  modifier: Modifier = Modifier,
  vm: MarketCardVM,
  onCardClick: () -> Unit = {},
  onAchievementsClick: () -> Unit = {},
) {
  MarketCardView(
    modifier = modifier,
    signInBtn = { m ->
      SignInBtn(
        modifier = m,
        vm = vm.signInBtnVm,
        onClick = onCardClick,
      )
    },/*
    achievementsBtn = { m ->
      AchievementsBtn(
        modifier = m,
        vm = vm.achievementsBtnVm,
        onClick = onAchievementsClick,
      )
    },*/
  )
}

@Composable
fun MarketCardView(
  modifier: Modifier = Modifier,
  signInBtn: @Composable (modifier: Modifier) -> Unit = {},
  //achievementsBtn: @Composable (modifier: Modifier) -> Unit = {},
) {
  Box(modifier = modifier) {
    signInBtn(
      modifier = Modifier.fillMaxSize(),
    )
    /*achievementsBtn(
      modifier = Modifier
        .padding(12.dp)
        .size(size = 48.dp)
        .align(Alignment.TopEnd),
    )*/
  }
}

@Preview
@Composable
fun MarketCardViewPreview(modifier: Modifier = Modifier) {
  MarketCardView(
    modifier = modifier,
    signInBtn = { SignInBtnPreview(it) },
    //achievementsBtn = { AchievementsBtnPreview(it) },
  )
}