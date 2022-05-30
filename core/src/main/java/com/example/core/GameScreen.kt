package com.example.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.feature.game.GameView2
import com.example.engine2.game.state.GameState
import com.example.engine2.game.state.GameState.Companion.Score
import com.example.engine2.node.Node
import com.example.engine2.node.NodeElement
import com.ilyin.ui_core_compose.colors.MdColors

@Composable
fun GameScreenView(
  vm: GameScreenVM,
  onGameEnd: () -> Unit = {},
  onClickMenu: () -> Unit = {}
  ) {

  val gameState:GameState? by vm.gameState.observeAsState()
  val gameStateSave = gameState?:return

  Box(Modifier.fillMaxSize()) {

    MenuButton(onClickMenu)



    var scoreState by remember {
      mutableStateOf(Score)
    }
    scoreState = Score


    if (GameState.MAX_ELEM_COUNT == gameStateSave.nodes.size) {
      onGameEnd()
    }

    Column(modifier = Modifier.align(Alignment.Center), verticalArrangement = Arrangement.spacedBy(16.dp)) {

      ScoreResult(scoreState, modifier = Modifier.align(Alignment.CenterHorizontally))

      LivesCircle(gameState = gameStateSave, modifier = Modifier.align(Alignment.CenterHorizontally))
      GameView2(
        gameState = gameStateSave,
        onGameStateChanged = { gameState ->
          vm.setGameState(gameState)
        },
        modifier = Modifier.aspectRatio(1f),
      )
    }
  }
}

@Composable
fun ScoreResult(score: Int, modifier: Modifier) {
  Column(modifier = modifier) {
    Text(text = "$score", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
  }
}

@Composable
fun findMaxElement(gameState: GameState): Node? {
  return gameState.nodes.maxByOrNull {
    if (it is NodeElement) {
      it.element.atomicMass
    } else {
      return null
    }
  }
}

@Composable
fun MenuButton(
  onClickMenu: () -> Unit = {}
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


@Composable
fun LivesCircle(gameState: GameState, modifier: Modifier = Modifier) {
  val steps = GameState.MAX_ELEM_COUNT - gameState.nodes.size
  val stepCountBeforeEnd = 3
  if (steps <= stepCountBeforeEnd) {
    val circleColor = when (steps) {
      3 -> MdColors.green.c500
      2 -> MdColors.yellow.c500
      1 -> MdColors.red.c500
      else -> Color.White
    }
    Row(
      modifier = modifier,
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      repeat(steps) {
        Spacer(
          modifier = Modifier
            .size(10.dp)
            .background(
              color = circleColor,
              shape = CircleShape
            )
        )
      }
    }
  } else{
    Spacer(
      modifier = Modifier
        .size(10.dp)
        .background(
          color = Color.Transparent,
          shape = CircleShape
        )
    )
  }
}

@Preview
@Composable
fun GameScreenViewPreview() {
  //GameScreenView()
}




