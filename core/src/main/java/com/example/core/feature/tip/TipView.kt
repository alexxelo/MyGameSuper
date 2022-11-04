package com.example.core.feature.tip


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TipView(
  modifier: Modifier = Modifier,
  vm: TipVM,
  onRequestToUseTip: () -> Unit = {},
  onRequestMoreTips: () -> Unit = {},
) {
  val tips by vm.tips.observeAsState()
  val tipsSave = tips ?: return

  val enableToUse by vm.enableToUse.observeAsState()
  val enableToUseSave = enableToUse ?: return

  TipView(
    modifier = modifier,
    tips = tipsSave,
    enableToUse = enableToUseSave,
    onTipClick = {
      if (tips == 0) {
        onRequestMoreTips()
      } else {
        vm.addTips(-1)
        onRequestToUseTip()
      }
    },
    onAddClick = onRequestMoreTips
  )
}

@Composable
fun TipView(
  modifier: Modifier = Modifier,
  tips: Int,
  enableToUse: Boolean,
  onTipClick: () -> Unit = {},
  onAddClick: () -> Unit = {},
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Button(
      onClick = onTipClick,
      enabled = enableToUse
    ) {
      Text(text = "$tips")
      Spacer(modifier = Modifier)
      //Image(painter =, contentDescription =)
      Spacer(modifier = Modifier)
      IconButton(onClick = onAddClick) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "stringResource(R.),"
        )
      }
    }
  }
}

@Preview
@Composable
fun TipViewPreview(modifier: Modifier = Modifier) {
  TipView(modifier = modifier, tips = 3, enableToUse = true)
}