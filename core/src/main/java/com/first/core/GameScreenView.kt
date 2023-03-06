package com.first.core

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
import com.first.core.feature.game.GameView2
import com.first.core.feature.game.GameViewPreview
import com.first.core.feature.game.end.GameEndView
import com.first.core.feature.game.tip.TipView
import com.first.core.feature.game.tip.TipViewPreview
import com.first.core.feature.game.tipshop.TipsShopView
import com.first.core.utils.*

import com.first.engine2.game.state.GameState

@Composable
fun GameScreenView(
  modifier: Modifier,
  vm: GameScreenVM,
  onGameEnd: () -> Unit = {},
  onClickMenu: () -> Unit = {},
  onClickShopBack: () -> Unit = {},
  onClickPlayAgain: () -> Unit = {},
  onClickNewGame: () -> Unit = {},
) {
  val gameState: GameState? by vm.gameState.observeAsState()
  val gameStateSafe = gameState ?: return
  val gameStateMax by vm.gameStateMaxNode.observeAsState()
  val showMenu by vm.showMenu.observeAsState()
  val useTip by vm.useTip.observeAsState()

  Box(
    Modifier.fillMaxSize()
    //.background(Color.Red)
  ) {

    MenuButton(vm = vm, onClickMenu = { vm.toggleMenu() })

    var scoreState by remember {
      mutableStateOf(gameStateSafe.gameScore)
    }
    scoreState = gameStateSafe.gameScore

    Column(
      modifier = Modifier.align(Alignment.Center),//.padding(top = 10.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

      TipView(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        vm = vm.tipVm,
        onRequestToUseTip = {
          vm.useTip()
          vm.dispatchTip()
          vm.playClickSound()
          vm.stopTip()

        },
        /*onGameStateChanged = { gameState ->
          vm.setGameState(gameState)
        }*/
        onRequestMoreTips = vm.tipShopVm::showTipShop
      )
      ScoreResultView(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        score = scoreState
      )
      MaxElementView(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        maxElement = gameStateMax,
      )
      LivesCircleView(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        gameState = gameStateSafe,
      )
      // drawing circle
      GameView2(
        modifier = Modifier.aspectRatio(1f),
        gameState = gameStateSafe,
        onGameStateChanged = { gameState ->
          vm.setGameState(gameState)
        },
        useTip = useTip
      )
    }
  }
  if (showMenu == true) {
    GameMenuView(
      onClickBack = {
        vm.toggleMenu()
        vm.playClickSound()
      },
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

  if (GameState.MAX_ELEM_COUNT == gameStateSafe.nodes.size) {
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
  TipsShopView(
    modifier = modifier,
    vm = vm.tipShopVm,
    onClickStore = { onClickShopBack() }
  )
/*
  TipShopDialog(
    modifier = modifier,
    vm = vm.tipShopVm,
  )*/
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
        //Spacer(modifier = Modifier.weight(weight = 1f))
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




