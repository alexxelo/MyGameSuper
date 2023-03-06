package com.first.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilyin.ui_core_compose.colors.MdColors

@Composable
fun GameMenuView(
  onClickMenu: () -> Unit = {},
  onClickPlayAgain: () -> Unit = {},
  onClickBack: () -> Unit = {}
) {
  Box(
    Modifier
      .fillMaxSize()
      .background(color = Color.White)
  ) {

    Column(modifier = Modifier.align(Alignment.Center)) {
      Spacer(modifier = Modifier.weight(1f))

      IconButton(
        onClick = {
          onClickBack()
        },
        modifier = Modifier
      ) {
        Icon(imageVector = Icons.Default.Clear, contentDescription = "",
        modifier = Modifier.size(128.dp))
      }
      Spacer(modifier = Modifier.weight(1f))
      IconButton(
        onClick = {
          onClickMenu()
        },
        modifier = Modifier
      ) {
        Icon(imageVector = Icons.Default.Menu, contentDescription = "",
          modifier = Modifier.size(128.dp))
      }
      Spacer(modifier = Modifier.weight(1f))

      IconButton(
        onClick = {
          onClickPlayAgain()
        },
        modifier = Modifier
      ) {
        Icon(imageVector = Icons.Default.Refresh, contentDescription = "",
          modifier = Modifier.size(128.dp))

      }
      Spacer(modifier = Modifier.weight(1f))

    }
  }
}

@Preview
@Composable
fun GameMenuViewPreview(modifier: Modifier = Modifier) {
  GameMenuView(
    onClickMenu = {},
    onClickBack = {},
    onClickPlayAgain = {}
  )
}