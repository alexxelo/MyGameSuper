package com.example.core.feature.game.tipshop


import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.feature.game.tipshop.free.ads.TipsForAds
import com.example.core.feature.game.tipshop.free.pereodical.TipsFreePeriodicView

@Composable
fun TipsShopView(
  modifier: Modifier = Modifier,
  vm: TipShopVM,
  onClickStore: () -> Unit = {},
  //activity: Activity,
) {
  val isTipShopShown by vm.isTipShopShown.observeAsState(false)
  val activity = LocalContext.current as Activity
  if (isTipShopShown) {
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
      },
      onClickStore = {onClickStore()}

    )
  }
}

// может сделтаь целую вкладку магазина со стрелкой в левом углу для перехода назад
// плюс так магазин будет выглядеть логичнее при переходе из гл меню!!
@Composable
fun TipsShopView(
  modifier: Modifier = Modifier,
  tipsFreePeriodic: @Composable (modifier: Modifier) -> Unit = {},
  tipsForAds: @Composable (modifier: Modifier) -> Unit = {},
  onClickStore: () -> Unit = {},

  ) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color.White)
      .padding(all = 16.dp)
    //verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Row(
      modifier = modifier
        .fillMaxWidth(),
      //.height(32.dp),
      horizontalArrangement = Arrangement.Start,
      //verticalAlignment = Alignment.Top
    ) {
      IconButton(
        onClick = { onClickStore() },
        modifier = modifier
      ) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
      }
    }
    Row(
      modifier = modifier.fillMaxWidth().padding(vertical = 16.dp),
      horizontalArrangement = Arrangement.Center,
    ) {
      Text(
        text = stringResource(id = com.ilyin.localization.R.string.store),
        modifier = Modifier,
        fontSize = 22.sp
      )
    }
    Row(modifier = modifier.fillMaxWidth()) {

      //Text(text = stringResource(id = com.example.core.R.string.antimatter_info))
      Text(text = "Игра закончится когда круг заполнится, используйте Антиматерию чтобы удалить половину атомов на круге")
    }
    Row(
      modifier = modifier.fillMaxWidth().padding(all = 16.dp),
      horizontalArrangement = Arrangement.Center,
    ) {

      Button(
        onClick = {

        }
      )
      {
        Text(text = "2 Antimatter ")

        Text(text = "20,00 RUB")
        //Icon(painter = , contentDescription = )
      }
    }
    Row(
      modifier = modifier.fillMaxWidth().padding(all = 16.dp),
      horizontalArrangement = Arrangement.Center,) {
      Button(
        onClick = {

        }
      )
      {
        Text(text = "5 Antimatter ")

        Text(text = "40,00 RUB")
        //Icon(painter = , contentDescription = )
      }
    }
    Row(
      modifier = modifier.fillMaxWidth().padding(all = 16.dp),
      horizontalArrangement = Arrangement.Center,) {
      Button(
        onClick = {

        }
      )
      {
        Text(text = "10 Antimatter ")

        Text(text = "70,00 RUB")
        //Icon(painter = , contentDescription = )
      }
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