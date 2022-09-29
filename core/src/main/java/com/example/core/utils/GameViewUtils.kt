package com.example.core.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.GameViewUtils
import com.example.engine2.game.state.GameState
import com.example.engine2.node.NodeElement
import com.ilyin.ui_core_compose.colors.MdColors


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
  val maxElementName: String? = maxElement?.element?.let { GameViewUtils.getNodeElementName(it.atomicMass) }
  if (maxElementName != null) {
    val maxElementNew = getStringResourceByName(maxElementName)

    if (maxElementNew != null) {
      Column(modifier = modifier) {
        Text(text = maxElementNew, fontSize = 20.sp, fontWeight = FontWeight.Medium, color = GameViewUtils.getNodeElementBgColor(maxElement.element.atomicMass))
      }
    }
  }
}