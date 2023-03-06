package com.first.core.feature.mainmenu.market

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.first.core.feature.mainmenu.market.signin.SignInBtn
import com.first.core.feature.mainmenu.market.signin.SignInBtnPreview


@Composable
fun MarketCardView(
  modifier: Modifier = Modifier,
  vm: MarketCardVM,
  onCardClick: () -> Unit = {},
) {
  MarketCardView(
    modifier = modifier,
    signInBtn = { m ->
      SignInBtn(
        modifier = m,
        vm = vm.signInBtnVm,
        onClick = onCardClick,
      )
    },
  )
}

@Composable
fun MarketCardView(
  modifier: Modifier = Modifier,
  signInBtn: @Composable (modifier: Modifier) -> Unit = {},
) {
    signInBtn(modifier)
}

@Preview
@Composable
fun MarketCardViewPreview(modifier: Modifier = Modifier) {
  MarketCardView(
    modifier = modifier,
    signInBtn = { SignInBtnPreview(it) },
  )
}