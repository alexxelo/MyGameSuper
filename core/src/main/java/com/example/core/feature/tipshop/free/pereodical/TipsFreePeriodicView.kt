package com.example.core.feature.tipshop.free.pereodical


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.utils.getDefaultLocaleCompose
import java.text.SimpleDateFormat


@Composable
fun TipsFreePeriodicView(
  modifier: Modifier = Modifier,
  vm: TipsPeriodicVM
) {

  val secondsRemains by vm.secondsRemains.observeAsState()
  val secondsRemainsSafe = secondsRemains ?: return
  TipsFreePeriodicView(
    modifier = modifier,
    secondsToRestore = secondsRemainsSafe,
    onBtnClick = vm::onObtainClick,
  )
}

@Composable
fun TipsFreePeriodicView(
  modifier: Modifier = Modifier,
  secondsToRestore: Long,
  onBtnClick: () -> Unit = {},
) {
  val formatter = SimpleDateFormat("m:ss", getDefaultLocaleCompose())

  Column(modifier = modifier) {

    Row(modifier = modifier) {
      Text(text = stringResource(id = com.example.core.R.string.tips_amount, TipsPeriodicVM.FREE_TIPS),)
      Spacer(modifier = Modifier.weight(1f))

      Button(
        onClick = onBtnClick,
        enabled = secondsToRestore <= 0,
      ){
        Text(
          text = if (secondsToRestore > 0) {
            formatter.format(secondsToRestore * 1000)
          } else {
            stringResource(id = com.example.core.R.string.obtain)
          }
        )
      }
    }
    Text(
      text = stringResource(com.example.core.R.string.tips_periodic_description),
      style = MaterialTheme.typography.bodyMedium,
    )
  }
}

@Preview
@Composable
fun TipsFreePeriodicViewPreview(modifier: Modifier = Modifier) {
  TipsFreePeriodicView(modifier = modifier, 30)
}