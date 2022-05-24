package com.example.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.feature.game.GameView2
import com.example.engine2.game.state.GameState
import com.example.engine2.game.state.GameState.Companion.Score
import com.ilyin.ui_core_compose.colors.MdColors

@Composable
fun MenuEnd(
  onClickNewPlay: () -> Unit = {},

  ) {
  Box(
    Modifier
      .fillMaxSize()
      .background(color = MdColors.pink.c100),
  ) {
    MenuButton()

    Column(modifier = Modifier.align(Alignment.Center), verticalArrangement = Arrangement.spacedBy(16.dp)) {
      //ScoreResult(score = Score, modifier = Modifier.align(Alignment.CenterHorizontally))
      TextButton(onClick = { onClickNewPlay() }) {
        Text(
          text = "New Game",
          textAlign = TextAlign.Center
        )

      }
    }
  }


}

@Composable
@Preview
fun MenuEndViewPreview() {
  MenuEnd()
}