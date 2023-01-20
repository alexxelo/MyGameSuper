package com.example.core.feature.tipshop


import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Row(modifier = modifier.fillMaxWidth()) {
      Text(text = "Store")
      IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.Clear, contentDescription = "")
      }
    }
    Row(modifier = modifier.fillMaxWidth()) {
      Text(text = "Info about antimatter")
    }
    Row() {

      Button(
        onClick = {}

      )
      {
        Text(text = "2 Antimatter ")

        Text(text = "20,00 RUB")
        //Icon(painter = , contentDescription = )
      }
    }
    Row() {

    }
    Row() {

    }
    Row() {


      if (true) {
        tipsForAds(modifier = modifier.fillMaxWidth())
      }
    }
  }

}

@Preview
@Composable
fun TipsShopViewPreview(modifier: Modifier = Modifier) {
  TipsShopView(
    modifier = modifier,
    tipsFreePeriodic = {},
    tipsForAds = { }
  )
}