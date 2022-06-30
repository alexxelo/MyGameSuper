package com.example.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.GameViewUtils.drawNodeElement
import com.example.core.feature.game.GameView2
import com.example.engine2.game.state.GameState
import com.example.engine2.game.state.GameState.Companion.Score
import com.ilyin.ui_core_compose.colors.MdColors

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

    Column(modifier = Modifier.align(Alignment.Center), verticalArrangement = Arrangement.spacedBy(16.dp)) {
      ScoreResult(score = Score, modifier = Modifier.align(Alignment.CenterHorizontally))

      MaxElement(gameStateMax, modifier = Modifier.align(Alignment.CenterHorizontally))
      TextButton(onClick = onClickNewPlay) {
        Text(
          text = "New Game",
          textAlign = TextAlign.Center
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
  //MenuEnd()
}