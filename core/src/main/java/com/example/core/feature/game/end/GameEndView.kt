package com.example.core.feature.game.end

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.GameMenuView
import com.example.core.GameScreenVM
import com.example.core.GameViewUtils.drawNodeElement
import com.example.core.utils.MaxElement
import com.example.core.utils.ScoreResult
import com.example.core.MenuButton
import com.example.engine2.game.Element
import com.example.engine2.game.state.GameState
import com.ilyin.ui_core_compose.colors.MdColors
import com.example.engine2.node.NodeElement

@Composable
fun GameEndView(
  vm: GameScreenVM,
  onClickNewPlay: () -> Unit = {},
  onClickMenu: () -> Unit = {},
) {

  val gameState: GameState? by vm.gameState.observeAsState()
  val gameStateSave = gameState ?: return
  val gameStateMax by vm.gameStateMaxNode.observeAsState()
  val gameStateMaxSave = gameStateMax ?: return

  Box(
    Modifier
      .fillMaxSize()
      // цвет закрышивает круг
      .background(color = MdColors.grey.c400),
  ) {

    Column(
      modifier = Modifier.align(Alignment.Center),
      verticalArrangement = Arrangement.spacedBy(32.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      ScoreResult(score = gameStateSave.gameScore, modifier = Modifier)

      MaxElement(gameStateMax, modifier = Modifier)
      Box(modifier = Modifier.background(Color.Green)) {
        Canvas(modifier = Modifier.background(color = Color.Red), onDraw = {
          drawNodeElement(node = NodeElement(Element(gameStateMaxSave), 1), circleRadius = 70f)

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
          text = stringResource(com.ilyin.localization.R.string.newgame),
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Bold,
        )

      }
      Button(
        //modifier = Modifier.padding(all = 20.dp),
        onClick = {
          onClickMenu()
          vm.setGameStateEnd()
        }
      )
      {

        androidx.compose.material3.Text(
          text = stringResource(com.ilyin.localization.R.string.menu),
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Bold,
        )

      }

    }

  }

}


@Composable
@Preview
fun GameEndViewViewPreview() {
  //GameEndView(vm = viewModel())
}