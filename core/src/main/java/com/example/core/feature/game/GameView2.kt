package com.example.core.feature.game

import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.feature.game.gameanimation.GameViewStateAnimator
import com.example.core.feature.game.gameanimation.GameViewStateAnimatorEmpty
import com.example.core.feature.game.gameanimation.GameViewStateAnimatorGeneral
import com.example.core.feature.game.gameanimation.GameViewStateAnimatorMerge
import com.example.core.feature.game.gamerequest.GameRequestComputerImpl
import com.example.core.feature.game.gameviewstate.*
import com.example.core.feature.game.gameviewstate.transformer.GameViewStateTransformerImpl
import com.example.engine2.game.Action
import com.example.engine2.game.result.RequestResultPart
import com.example.engine2.game.state.GameState
import com.example.engine2.game.state.dynamic.GameStateDynamic
import com.example.engine2.node.NodeAction
import com.example.engine2.node.NodeElement

@Composable
fun GameView2(
  modifier: Modifier = Modifier,
  gameState: GameState,
  onGameStateChanged: (GameState) -> Unit = {},
) {
  var gameStateState: GameState by remember { mutableStateOf(gameState) }
  val lastPattern: Pair<NodeElement, NodeElement>? = gameState.bestPattern?.last()


  BoxWithConstraints(modifier = modifier) {

    val density = LocalDensity.current
    val widthPx = with(density) { maxWidth.toPx() }
    val heightPx = with(density) { maxHeight.toPx() }

    val dimens = GameViewStateDimensions.createFrom(gameState, widthPx, heightPx)
    val initialViewState = GameViewState.createFrom(gameStateState, dimens)
    var animators: List<GameViewStateAnimator> by remember { mutableStateOf(listOf(GameViewStateAnimatorEmpty(initialViewState))) }

    var gameAnimator: GameViewStateAnimator by remember { mutableStateOf(animators.first()) }

    var fractionStart by remember { mutableStateOf(0f) }
    var fractionEnd by remember { mutableStateOf(1f) }
    val fractionAnimatable by animateFloatAsState(
      targetValue = fractionEnd,
      animationSpec = tween(durationMillis = 225),
      finishedListener = {
        if (animators.size > 1) {
          animators = animators.drop(1)
          fractionStart++
          fractionEnd++
          gameAnimator = animators.first()
        }
      }
    )

    val gameViewStateAnimated: GameViewState = gameAnimator.animate(
      fraction = fractionAnimatable - fractionStart,
    )

    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
          detectTapGestures { clickPoint ->
            val animationIsNotComplete = fractionAnimatable < fractionEnd
            if (animationIsNotComplete) return@detectTapGestures

            // Получить информацию о клике
            val clickResult: ClickResult = animators.last().endState.click(clickPoint)
            // На основе клика узнать, какое действие нужно выполнить
            val gameRequest = GameRequestComputerImpl().compute(clickResult, gameStateState)
            // На основе действия изменить состояние игры, получив все промежуточные состояния и запросы на изменение состояния
            val initialGameState = gameStateState.clone()
            val gameRequestResult = gameStateState.executeRequest(gameRequest)
            // Сгруппировать промежуточне состояния в динамическое состояние
            val dynamicState = GameStateDynamic.from(initialGameState, gameRequestResult)

            val newAnimators: List<GameViewStateAnimator> = computeNewAnimators(dynamicState, gameAnimator)


            val newState = dynamicState.steps.last().state2
            onGameStateChanged(newState)

            gameStateState = newState
            fractionStart++
            fractionEnd++
            animators = newAnimators
            gameAnimator = newAnimators.first()
          }

        },
      onDraw = {
        gameViewStateAnimated.draw(this)
        gameViewStateAnimated.drawArc(this, gameViewStateAnimated, lastPattern)

      },
    )
    AnimateActiveNode(gameStateState, gameViewStateAnimated)


  }
}


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
fun AnimateActiveNode(gameState: GameState, gameViewStateAnimated: GameViewState) {

  val deltaXAnim = rememberInfiniteTransition()
  val dxMinus by deltaXAnim.animateFloat(
    initialValue = 120f,
    targetValue = 0f,
    animationSpec = infiniteRepeatable(
      animation = tween(3000)
    )
  )
  val dxPlus by deltaXAnim.animateFloat(
    initialValue = 0f,
    targetValue = 120f,
    animationSpec = infiniteRepeatable(
      animation = tween(3000)
    )
  )
  val colorAnim = rememberInfiniteTransition()
  val dcMinus by colorAnim.animateColor(
    initialValue = Color.Blue,
    targetValue = Color.Blue,
    animationSpec = infiniteRepeatable(
      animation = tween(3000)
    )
  )
  val dcPlus by colorAnim.animateColor(
    initialValue = Color.Red,
    targetValue = Color.Red,
    animationSpec = infiniteRepeatable(
      animation = tween(3000)
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

    } else if (node.action == Action.PLUS) {
      DrawCircle(
        circleRadius = dxPlus,
        offsetX = gameViewStateAnimated.dimens.center.x,
        offsetY = gameViewStateAnimated.dimens.center.y,
        color = dcPlus
      )
    }
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

private fun computeNewAnimators(dynamicState: GameStateDynamic, lastAnimator: GameViewStateAnimator): List<GameViewStateAnimator> {
  val viewStateTransformer = GameViewStateTransformerImpl()
  val newAnimators: MutableList<GameViewStateAnimator> = arrayListOf()
  var prevViewState = GameViewState.createFrom(
    gameState = dynamicState.steps.first().state1,
    lastAnimator.endState.dimens,
  )

  dynamicState.steps.forEach { step ->
    val resultPart = step.requestPart
    val nextViewState = viewStateTransformer.transform(prevViewState, resultPart, step.state2)
    val animator = when (resultPart) {
      is RequestResultPart.Merge -> GameViewStateAnimatorMerge(prevViewState, resultPart, nextViewState)
      else -> GameViewStateAnimatorGeneral(prevViewState, nextViewState)
    }
    newAnimators.add(animator)
    prevViewState = nextViewState
  }
  return newAnimators
}

@Preview(device = Devices.NEXUS_5)
@Composable
private fun GameViewPreview() {
  /*GameView2(
    gameState = GameState.createInitial()
  )*/
}