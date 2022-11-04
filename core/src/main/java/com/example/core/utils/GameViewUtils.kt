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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.GameViewUtils
import com.example.engine2.game.state.GameState
import com.example.engine2.node.NodeElement
import com.ilyin.ui_core_compose.colors.MdColors


@Composable
fun LivesCircleView(gameState: GameState, modifier: Modifier = Modifier) {
  val steps = GameState.MAX_ELEM_COUNT - gameState.nodes.size
  LivesCircleView(modifier, steps)
}

@Composable
fun LivesCircleView(modifier: Modifier, steps: Int) {
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
fun LivesCircleViewPreview(modifier: Modifier = Modifier) {
  LivesCircleView(modifier = modifier, steps = 3)
}

@Composable
fun ScoreResultView(modifier: Modifier, score: Int) {
  Column(modifier = modifier) {
    Text(text = "$score", fontSize = 26.sp, fontWeight = FontWeight.SemiBold)
  }
}

@Preview
@Composable
fun ScoreResultViewPreview(modifier: Modifier = Modifier) {
  ScoreResultView(modifier = modifier, score = 12)
}

@Composable
fun getStringResourceByName(aString: String): String {
  val context = LocalContext.current
  val packageName = context.packageName
  val resId = context.resources.getIdentifier(aString, "string", packageName)
  return context.getString(resId)
}

@Composable
fun MaxElementView(modifier: Modifier, maxElement: Int?) {
  if (maxElement == null) return
  if (maxElement == 0) return
  val maxElementName: String = maxElement.let { GameViewUtils.getNodeElementName(it) }
  val maxElementNew: String = getStringResourceByName(maxElementName)

  Column(modifier = modifier) {
    Text(
      text = maxElementNew,
      fontSize = 20.sp,
      fontWeight = FontWeight.Medium,
      color = GameViewUtils.getNodeElementBgColor(maxElement)
    )
  }
}

@Preview
@Composable
fun MaxElementViewPreview(modifier: Modifier = Modifier) {
  MaxElementView(modifier = modifier, maxElement = 3)
}

