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
import com.example.core.feature.game.GameViewPreview
import com.example.core.feature.game.end.GameEndView
import com.example.core.feature.tip.TipViewPreview
import com.example.core.feature.tipshop.TipShopDialog
import com.example.core.feature.tipshop.TipsShopView
import com.example.core.utils.*

import com.example.engine2.game.state.GameState

@Composable
fun GameScreenView(
  modifier: Modifier,
  vm: GameScreenVM,
  onGameEnd: () -> Unit = {},
  onClickMenu: () -> Unit = {},
  onClickPlayAgain: () -> Unit = {},
  onClickNewGame: () -> Unit = {},
) {
  val gameState: GameState? by vm.gameState.observeAsState()
  val gameStateSave = gameState ?: return
  val gameStateMax by vm.gameStateMaxNode.observeAsState()
  val showMenu by vm.showMenu.observeAsState()

  Box(Modifier.fillMaxSize()) {

    MenuButton(vm = vm, onClickMenu = { vm.toggleMenu() })
    var scoreState by remember {
      mutableStateOf(gameStateSave.gameScore)
    }
    scoreState = gameStateSave.gameScore


    Column(modifier = Modifier.align(Alignment.Center), verticalArrangement = Arrangement.spacedBy(22.dp)) {
      ScoreResultView(modifier = Modifier.align(Alignment.CenterHorizontally), score = scoreState)
      MaxElementView(maxElement = gameStateMax, modifier = Modifier.align(Alignment.CenterHorizontally))
      LivesCircleView(gameState = gameStateSave, modifier = Modifier.align(Alignment.CenterHorizontally))
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
      vm = vm,
      onClickBack = { vm.toggleMenu() },
      onClickPlayAgain = {
        vm.playClickSound()
        onClickPlayAgain()
        vm.setGameStateEnd()
      },
      onClickMenu = {
        onClickMenu()
        vm.playClickSound()
      }
    )
  }

  if (GameState.MAX_ELEM_COUNT == gameStateSave.nodes.size) {
    vm.onGameEnd()
    GameEndView(
      vm = vm,
      onClickMenu = {
        onClickMenu()
        vm.playClickSound()
      },
      onClickNewPlay = {
        vm.playClickSound()
        onClickNewGame()
        vm.setGameStateEnd()
      }
    )
  }
  TipShopDialog(
    modifier = modifier,
    vm = vm.tipShopVm,
  )
}

@Composable
fun GameScreenView(
  modifier: Modifier,
  gameView: @Composable (modifier: Modifier) -> Unit = {},
  livesView: @Composable (modifier: Modifier) -> Unit = {},
  gameMenuView: @Composable (modifier: Modifier) -> Unit = {},
  //timeMachineView: @Composable (modifier: Modifier) -> Unit = {},
  scoreView: @Composable (modifier: Modifier) -> Unit = {},
  maxElementView: @Composable (modifier: Modifier) -> Unit = {},
  tipView: @Composable (modifier: Modifier) -> Unit = {},
  endGameView: @Composable (modifier: Modifier) -> Unit = {},

  ) {
  Box(modifier = modifier) {
    Column(modifier = Modifier.fillMaxSize()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        gameMenuView(modifier = Modifier)
        Spacer(modifier = Modifier.weight(weight = 1f))
        //timeMachineView(modifier = Modifier)
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        tipView(modifier = Modifier)
      }
      Spacer(modifier = Modifier.weight(weight = 1f))
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        scoreView(modifier = Modifier)
      }
      Spacer(modifier = Modifier.weight(weight = 1f))

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        maxElementView(modifier = Modifier)
      }
      Spacer(modifier = Modifier.weight(weight = 1f))
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        livesView(modifier = Modifier)
      }
      Spacer(modifier = Modifier.weight(1f))

      gameView(modifier = Modifier.fillMaxWidth())
    }
    endGameView(modifier = Modifier)

  }

}

@Composable
fun MenuButton(
  vm: GameScreenVM,
  onClickMenu: () -> Unit = {},
) {
  Row(
    Modifier
      .fillMaxWidth()
  ) {
    IconButton(
      onClick = {
        onClickMenu()
        vm.playClickSound()
      },
      modifier = Modifier
    ) {
      Icon(imageVector = Icons.Default.Menu, contentDescription = "")
    }
  }
}

@Preview
@Composable
fun GameScreenViewPreview(modifier: Modifier = Modifier) {
  GameScreenView(
    modifier = modifier,
    gameView = { GameViewPreview() },
    livesView = { LivesCircleViewPreview(it) },
    gameMenuView = {},
    scoreView = { ScoreResultViewPreview(it) },
    maxElementView = { MaxElementViewPreview(it) },
    tipView = { TipViewPreview(it) },
    endGameView = {}
  )
}




