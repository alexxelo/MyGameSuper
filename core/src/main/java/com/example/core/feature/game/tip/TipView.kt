package com.example.core.feature.game.tip


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TipView(
  modifier: Modifier = Modifier,
  vm: TipVM,
  onRequestToUseTip: () -> Unit = {},
  onRequestMoreTips: () -> Unit = {},
) {
  val tips by vm.tips.observeAsState()
  val tipsSafe = tips ?: return


  TipView(
    modifier = modifier,
    tips = tipsSafe,
    enableToUse = true,
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
    Text(text = "$tips")
    Spacer(modifier = Modifier)
    IconButton(
      onClick = onTipClick,
      enabled = enableToUse
    ) {
      Icon(
        imageVector = Icons.Default.AddTask,
        contentDescription = ""
      )
    }
    Spacer(modifier = Modifier)
    IconButton(
      onClick = onAddClick,
    ) {
      Icon(
        imageVector = Icons.Default.Add,
        contentDescription = stringResource(id = com.example.core.R.string.get_tips)
      )

    }
  }
}

@Preview
@Composable
fun TipViewPreview(modifier: Modifier = Modifier) {
  TipView(modifier = modifier, tips = 3, enableToUse = true)
}