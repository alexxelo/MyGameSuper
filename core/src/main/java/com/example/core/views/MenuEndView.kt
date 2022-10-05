package com.example.core.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.GameMenuView
import com.example.core.GameScreenVM
import com.example.core.GameViewUtils.drawNodeElement
import com.example.core.utils.MaxElement
import com.example.core.utils.ScoreResult
import com.example.engine2.game.state.GameState.Companion.Score
import com.ilyin.ui_core_compose.colors.MdColors
import com.example.engine2.node.NodeElement

@Composable
fun MenuEnd(
  vm: GameScreenVM,
  onClickNewPlay: () -> Unit = {},
  onClickMenu: () -> Unit = {},

  ) {

  val gameStateMax by vm.gameStateMaxNode.observeAsState()
  val showMenu by vm.showMenu.observeAsState()

  Box(
    Modifier
      .fillMaxSize()
      // цвет закрышивает круг
      .background(color = MdColors.pink.c100),
  ) {
    MenuButton(
      onClickMenu = { vm.toggleMenu() },
    )

    Column(modifier = Modifier.align(Alignment.Center),
      verticalArrangement = Arrangement.spacedBy(32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,) {
      ScoreResult(score = Score, modifier = Modifier)

      MaxElement(gameStateMax, modifier = Modifier)
      Box(modifier = Modifier.background(Color.Green)) {
        Canvas(modifier = Modifier.background(color = Color.Red), onDraw = {
          drawNodeElement(node = NodeElement(gameStateMax!!.element, 1), circleRadius = 70f)

        })
      }
      TextButton(onClick = onClickNewPlay) {
        Text(
          text = stringResource(com.ilyin.localization.R.string.newgame),
          textAlign = TextAlign.Center,
          fontSize =  26.sp
        )

      }
    }
    if (showMenu == true) {
      GameMenuView(
        onClickBack = { vm.toggleMenu() },
        onClickPlayAgain = onClickNewPlay,
        onClickMenu = onClickMenu
      )
    }
  }

}


@Composable
@Preview
fun MenuEndViewPreview() {
  MenuEnd(vm = viewModel())
}