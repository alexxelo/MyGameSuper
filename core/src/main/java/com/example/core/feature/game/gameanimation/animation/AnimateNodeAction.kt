package com.example.core.feature.game.gameanimation.animation

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.core.feature.game.gameviewstate.GameViewState
import com.example.engine2.game.Action
import com.example.engine2.game.state.GameState
import com.example.engine2.node.NodeAction
import com.ilyin.ui_core_compose.colors.MdColors


@Composable
fun DrawCircle(circleRadius: Float, offsetX: Float, offsetY: Float, color: Color) {
  Canvas(modifier = Modifier) {
    drawCircle(
      color = color,
      center = Offset(offsetX, offsetY),
      radius = circleRadius,
      alpha = 0.3f
    )
  }
}


@Composable
fun AnimatePlusSimple(gameState: GameState, gameViewStateAnimated: GameViewState) {
  val infiniteTransition = rememberInfiniteTransition()

  val dxPlus by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 130f,
    animationSpec = infiniteRepeatable(
      animation = tween(3000, 200, easing = LinearEasing),
    )
  )

  val colorAnim = rememberInfiniteTransition()
  val dcPlus by colorAnim.animateColor(
    initialValue = MdColors.red.c600,
    targetValue = Color.White,
    animationSpec = infiniteRepeatable(
      animation = tween(1000,2200, easing = LinearEasing),
    )
  )
  val node = gameState.activeNode
  if (node is NodeAction) {

    if (node.action == Action.PLUS) {
      DrawCircle(
        circleRadius = dxPlus,
        offsetX = gameViewStateAnimated.dimens.center.x,
        offsetY = gameViewStateAnimated.dimens.center.y,
        color = dcPlus
      )
    }

    if (gameState.allPluses.isNotEmpty()) {
      gameState.allPluses.forEach { nodePlus ->
        val offset = gameViewStateAnimated.nodesView.find { it.id == nodePlus.id }!!.centerOffset + gameViewStateAnimated.dimens.center
        DrawCircle(
          circleRadius = dxPlus,
          offsetX = offset.x,
          offsetY = offset.y,
          color = dcPlus
        )
      }
    }
  }
}


@Composable
fun AnimateMinus(gameState: GameState, gameViewStateAnimated: GameViewState) {
  val infiniteTransition = rememberInfiniteTransition()

  val dxMinus by infiniteTransition.animateFloat(
    initialValue = 130f,
    targetValue = 0f,
    animationSpec = infiniteRepeatable(
      animation = tween(3000, easing = LinearEasing),
    )
  )

  val colorAnim = rememberInfiniteTransition()
  val dcMinus by colorAnim.animateColor(
    initialValue = Color.White,
    targetValue = MdColors.blue.c600,
    animationSpec = infiniteRepeatable(
      animation = tween(3000, easing = LinearOutSlowInEasing),
    )
  )
  val node = gameState.activeNode
  if (node is NodeAction) {
    if (node.action == Action.MINUS) {

      DrawCircle(
        circleRadius = dxMinus,
        offsetX = gameViewStateAnimated.dimens.center.x,
        offsetY = gameViewStateAnimated.dimens.center.y,
        color = dcMinus
      )
    }
  }
}
