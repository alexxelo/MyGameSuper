package com.example.core.feature.tipshop


import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.feature.tipshop.free.ads.TipsForAds
import com.example.core.feature.tipshop.free.pereodical.TipsFreePeriodicView

@Composable
fun TipsShopView(
  modifier: Modifier = Modifier,
  vm: TipShopVM,
  activity: Activity,
) {
  TipsShopView(
    modifier = modifier,
    tipsFreePeriodic = { m ->
      TipsFreePeriodicView(
        modifier = m,
        vm = vm.tipPeriodicVm
      )
    },
    tipsForAds = { m ->
      TipsForAds(
        modifier = m,
        vm = vm.tipsForAdsVm,
        activity = activity
      )

    }
  )
}

@Composable
fun TipsShopView(
  modifier: Modifier = Modifier,
  tipsFreePeriodic: @Composable (modifier: Modifier) -> Unit = {},
  tipsForAds: @Composable (modifier: Modifier) -> Unit = {}
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {

    tipsForAds(modifier = modifier.fillMaxWidth())
  }

}

@Preview
@Composable
fun TipsShopViewPreview(modifier: Modifier = Modifier) {
  TipsShopView(
    modifier = modifier,
    tipsFreePeriodic ={},
    tipsForAds = { }
  )
}