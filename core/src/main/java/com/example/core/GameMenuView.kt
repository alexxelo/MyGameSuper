package com.example.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
      .background(color = MdColors.pink.c100)
  ) {

    Column(modifier = Modifier.align(Alignment.Center)) {
      IconButton(
        onClick = {
          onClickBack()
        },
        modifier = Modifier
      ) {
        Icon(imageVector = Icons.Default.Clear, contentDescription = "")
      }
      IconButton(
        onClick = {
          onClickMenu()
        },
        modifier = Modifier
      ) {
        Icon(imageVector = Icons.Default.Menu, contentDescription = "")
      }
      IconButton(
        onClick = {
          onClickPlayAgain()
        },
        modifier = Modifier
      ) {
        Icon(imageVector = Icons.Default.Refresh, contentDescription = "")
      }
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