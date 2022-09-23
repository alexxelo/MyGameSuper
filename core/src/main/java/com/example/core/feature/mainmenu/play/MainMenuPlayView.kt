package com.example.core.feature.mainmenu.play

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainMenuPlayView(
  modifier: Modifier,
  onClick: () -> Unit = {}
) {

  Column(modifier = Modifier.fillMaxSize()) {
    Text(
      text = "Classic",
      modifier = Modifier.fillMaxWidth()
    )
    TextButton(onClick = { /*TODO*/ }) {
      Text(text = "Play")
    }
  }

}