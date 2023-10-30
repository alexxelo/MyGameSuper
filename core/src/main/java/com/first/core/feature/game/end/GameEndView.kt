package com.first.core.feature.game.end

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.first.core.GameScreenVM
import com.first.core.GameViewUtils.drawNodeElement
import com.first.core.utils.MaxElementView
import com.first.core.utils.ScoreResultView
import com.first.engine2.game.Element
import com.first.engine2.game.state.GameState
import com.first.engine2.node.NodeElement

@Composable
fun
    GameEndView(
  modifier: Modifier = Modifier,
  vm: GameScreenVM,
  onClickNewPlay: () -> Unit = {},
  onClickMenu: () -> Unit = {},
) {

  val gameState: GameState? by vm.gameState.observeAsState()
  val gameStateSafe = gameState ?: return
  val gameStateMax by vm.gameStateMaxNode.observeAsState()
  val gameStateMaxSafe: Int = gameStateMax ?: return

  GameEndView(
    modifier = modifier,
    score = gameStateSafe.gameScore,
    maxElement = gameStateMax,
    maxElementSafe = gameStateMaxSafe,
    onClickMenu = {
      onClickMenu()
      vm.setGameStateEnd()
    },
    onClickNewPlay = {
      onClickNewPlay()
    }
  )
}

@Composable
fun GameEndView(
  modifier: Modifier,
  score: Int,
  maxElement: Int?,
  maxElementSafe: Int,
  onClickNewPlay: () -> Unit = {},
  onClickMenu: () -> Unit = {},
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      // цвет закрышивает круг
      .background(color = Color.White),
  ) {

    Column(
      modifier = Modifier.align(Alignment.Center),
      verticalArrangement = Arrangement.spacedBy(32.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      ScoreResultView(score = score, modifier = Modifier)

      MaxElementView(modifier = Modifier, maxElement = maxElement)
      Box(modifier = Modifier.background(Color.Green)) {
        Canvas(modifier = Modifier.background(color = Color.Red), onDraw = {
          drawNodeElement(node = NodeElement(Element(maxElementSafe), 1), circleRadius = 70f)
        })
      }
      Button(
        modifier = Modifier.padding(all = 20.dp),
        onClick = {
          onClickNewPlay()
        }
      )
      {
        androidx.compose.material3.Text(
          text = stringResource(com.first.localization.R.string.newgame),
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Bold,
        )
      }
      Button(
        //modifier = Modifier.padding(all = 20.dp),
        onClick = {
          onClickMenu()
        }
      )
      {
        androidx.compose.material3.Text(
          text = stringResource(com.first.localization.R.string.menu),
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Bold,
        )
      }
    }
  }
}

@Composable
@Preview
fun GameEndViewViewPreview(modifier: Modifier = Modifier) {
  GameEndView(
    modifier = modifier,
    score = 12,
    maxElement = 6,
    maxElementSafe = 6,
    onClickMenu = {},
    onClickNewPlay = {})
}