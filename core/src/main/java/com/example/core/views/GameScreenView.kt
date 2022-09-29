package com.example.core

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.feature.game.GameView2
import com.example.core.utils.LivesCircle
import com.example.core.utils.MaxElement
import com.example.core.utils.ScoreResult
import com.example.core.views.MenuEnd

import com.example.engine2.game.state.GameState
import com.example.engine2.game.state.GameState.Companion.Score

@Composable
fun GameScreenView(
  modifier: Modifier,
  vm: GameScreenVM,
  onGameEnd: () -> Unit = {},
  onClickMenu: () -> Unit = {},
  onClickPlayAgain: () -> Unit = {},
) {
  val gameState: GameState? by vm.gameState.observeAsState()
  val gameStateSave = gameState ?: return
  val gameStateMax by vm.gameStateMaxNode.observeAsState()
  val showMenu by vm.showMenu.observeAsState()

  Box(Modifier.fillMaxSize()) {

    MenuButton { vm.toggleMenu() }

    var scoreState by remember {
      mutableStateOf(Score)
    }
    scoreState = Score


    Column(modifier = Modifier.align(Alignment.Center), verticalArrangement = Arrangement.spacedBy(16.dp)) {
      ScoreResult(scoreState, modifier = Modifier.align(Alignment.CenterHorizontally))
      MaxElement(maxElement = gameStateMax, modifier = Modifier.align(Alignment.CenterHorizontally))
      LivesCircle(gameState = gameStateSave, modifier = Modifier.align(Alignment.CenterHorizontally))
      GameView2(
        gameState = gameStateSave,
        onGameStateChanged = { gameState ->
          vm.setGameState(gameState)
        },
        modifier = Modifier.aspectRatio(1f),
      )
      //AnimateNewElementEffect(Modifier)

    }
  }
  if (showMenu == true) {
    GameMenuView(
      onClickBack = { vm.toggleMenu() },
      onClickPlayAgain = onClickPlayAgain,
      onClickMenu = onClickMenu
    )
  }
  if (GameState.MAX_ELEM_COUNT == gameStateSave.nodes.size) {
    MenuEnd(
      vm = vm,
      onClickMenu = onClickMenu,
      onClickNewPlay = onClickPlayAgain
    )
  }
}

@Composable
fun MenuButton(
  onClickMenu: () -> Unit = {},
) {
  Row(
    Modifier
      .fillMaxWidth()
  ) {
    IconButton(
      onClick = { onClickMenu() },
      modifier = Modifier
    ) {
      Icon(imageVector = Icons.Default.Menu, contentDescription = "")
    }
  }
}

@Preview
@Composable
fun GameScreenViewPreview() {
}




