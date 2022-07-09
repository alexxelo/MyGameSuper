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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.GameViewUtils.getNodeElementBgColor
import com.example.core.GameViewUtils.getNodeElementName
import com.example.core.feature.game.GameView2
import com.example.engine2.game.state.GameState
import com.example.engine2.game.state.GameState.Companion.Score
import com.example.engine2.game.state.GameState.Companion.createGame
import com.example.engine2.node.NodeElement
import com.ilyin.ui_core_compose.colors.MdColors

@Composable
fun GameScreenView(
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

    MenuButton({ vm.toggleMenu() })

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
fun ScoreResult(score: Int, modifier: Modifier) {
  Column(modifier = modifier) {
    Text(text = "$score", fontSize = 26.sp, fontWeight = FontWeight.SemiBold)
  }
}

@Composable
fun getStringResourceByName(aString: String): String? {
  val context = LocalContext.current
  val packageName = context.packageName
  val resId = context.resources.getIdentifier(aString, "string", packageName)
  return context.getString(resId)
}

@Composable
fun MaxElement(maxElement: NodeElement?, modifier: Modifier) {
  val maxElementName: String? = maxElement?.element?.let { getNodeElementName(it.atomicMass) }
  if (maxElementName != null) {
    val maxElementNew = getStringResourceByName(maxElementName)

    if (maxElementNew != null) {
      Column(modifier = modifier) {
        Text(text = maxElementNew, fontSize = 20.sp, fontWeight = FontWeight.Medium, color = getNodeElementBgColor(maxElement.element.atomicMass))
      }
    }
  }
}

@Composable
fun MenuButton(
  onClickMenu: () -> Unit = {},

  //onClickMenu: () -> Unit = {},
  //onClickPlayAgain: () -> Unit = {},
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
  } else {
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
}




