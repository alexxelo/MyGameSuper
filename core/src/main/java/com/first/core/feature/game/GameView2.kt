package com.first.core.feature.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import com.first.core.feature.game.gameanimation.GameViewStateAnimator
import com.first.core.feature.game.gameanimation.GameViewStateAnimatorEmpty
import com.first.core.feature.game.gameanimation.GameViewStateAnimatorGeneral
import com.first.core.feature.game.gameanimation.GameViewStateAnimatorMerge
import com.first.core.feature.game.gameanimation.animation.AnimateMinus
import com.first.core.feature.game.gameanimation.animation.AnimatePlusSimple
import com.first.core.feature.game.gamerequest.GameRequestComputerImpl
import com.first.core.feature.game.gameviewstate.ClickResult
import com.first.core.feature.game.gameviewstate.GameViewState
import com.first.core.feature.game.gameviewstate.GameViewStateDimensions
import com.first.core.feature.game.gameviewstate.transformer.GameViewStateTransformerImpl
import com.first.engine2.game.result.RequestResultPart
import com.first.engine2.game.state.GameState
import com.first.engine2.game.state.dynamic.GameStateDynamic
import com.first.engine2.node.NodeElement

@Composable
fun GameView2(
  modifier: Modifier = Modifier,
  gameState: GameState,
  onGameStateChanged: (GameState) -> Unit = {},
  useTip: Boolean?

  ) {
  DrawContent(modifier = modifier, gameState = gameState, onGameStateChanged = onGameStateChanged, useTip = useTip)
}

@Composable
fun DrawContent(
  modifier: Modifier,
  gameState: GameState,
  onGameStateChanged: (GameState) -> Unit = {},
  useTip: Boolean?
  ) {
  val lastPattern: Pair<NodeElement, NodeElement>? = gameState.bestPattern?.last()

  var gameStateState: GameState by remember { mutableStateOf(gameState) }

  BoxWithConstraints(modifier = modifier) {

    val density = LocalDensity.current
    val widthPx = with(density) { maxWidth.toPx() }
    val heightPx = with(density) { maxHeight.toPx() }

    val dimens = GameViewStateDimensions.createFrom(gameStateState, widthPx, heightPx)
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
            val gameRequest = GameRequestComputerImpl().compute(clickResult, gameStateState, useTip)
            // На основе действия изменить состояние игры, получив все промежуточные состояния и запросы на изменение состояния
            val initialGameState = gameStateState.clone()
            //
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
        gameViewStateAnimated.draw(this, gameStateState)
        gameViewStateAnimated.drawArc(this, gameViewStateAnimated, lastPattern, gameStateState)

      },
    )
    AnimateMinus(gameState = gameStateState, gameViewStateAnimated = gameViewStateAnimated)
    AnimatePlusSimple(gameState = gameStateState, gameViewStateAnimated = gameViewStateAnimated)


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

@Preview
@Composable
fun GameViewPreview(modifier: Modifier = Modifier) {
  /*GameView2(
    gameState = GameState.createInitial()
  )*/
}