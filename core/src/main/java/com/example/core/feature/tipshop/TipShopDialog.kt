package com.example.core.feature.tipshop

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TipShopDialog(
  modifier: Modifier = Modifier,
  vm: TipShopVM
) {

  val isTipShopShown by vm.isTipShopShown.observeAsState(false)
  val activity = LocalContext.current as Activity
  if (isTipShopShown)
  {
    Dialog(onDismissRequest = vm::dismissTipShop) {
      TipShopDialogContent(
        modifier = modifier,
        vm = vm,
        activity = activity,
      )
    }
  }

}
@Composable
fun TipShopDialogContent(
  modifier: Modifier = Modifier,
  vm: TipShopVM,
  activity: Activity,
){

  ElevatedCard(modifier = modifier) {
    TipsShopView(
      modifier = Modifier.padding(16.dp),
      activity = activity,
      vm = vm,
    )
  }
}