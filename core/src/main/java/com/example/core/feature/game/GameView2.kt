package com.example.core.feature.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.feature.game.gameanimation.GameViewStateAnimator
import com.example.core.feature.game.gameanimation.GameViewStateAnimatorEmpty
import com.example.core.feature.game.gameanimation.GameViewStateAnimatorGeneral
import com.example.core.feature.game.gamerequest.GameRequestComputerImpl
import com.example.core.feature.game.gameviewstate.ClickResult
import com.example.core.feature.game.gameviewstate.GameViewState
import com.example.core.feature.game.gameviewstate.GameViewStateDimensions
import com.example.core.feature.game.gameviewstate.transformer.GameViewStateTransformer
import com.example.core.feature.game.gameviewstate.transformer.GameViewStateTransformerImpl
import com.example.engine2.game.state.GameState
import com.example.engine2.game.request.GameRequest
import com.example.engine2.game.result.RequestResult
import com.example.engine2.game.result.RequestResultPart
import com.example.engine2.game.state.dynamic.GameStateDynamic

@Composable
fun GameView2(modifier: Modifier = Modifier, gameState: GameState) {
    val coroutine = rememberCoroutineScope()
    var gameStateState: GameState by remember { mutableStateOf(gameState) }

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
                        // Получить информацию о клике
                        val clickResult: ClickResult = animators.last().endState.click(clickPoint)
                        // На основе клика узнать, какое действите нужно выполнить
                        val gameRequest: GameRequest = GameRequestComputerImpl().compute(clickResult, gameStateState)
                        val dynamicState: GameStateDynamic = GameStateDynamic.from(gameStateState.clone(), gameStateState.executeRequest(gameRequest))

                        val transformer = GameViewStateTransformerImpl()
                        val newAnimators: MutableList<GameViewStateAnimator> = arrayListOf()
                        var prevViewState = GameViewState.createFrom(
                            gameState = dynamicState.steps.first().state1,
                            gameAnimator.endState.dimens,
                        )

                        dynamicState.steps.forEach { step ->
                            val nextViewState = transformer.transform(prevViewState, step.resultPart, step.state2)
                            newAnimators.add(GameViewStateAnimatorGeneral(prevViewState, nextViewState))
                            prevViewState = nextViewState
                        }


                        val newState = dynamicState.steps.last().state2
                        gameStateState = newState
                        fractionStart++
                        fractionEnd++
                        animators = newAnimators
                        gameAnimator = newAnimators.first()
                    }

                },
            onDraw = {
                gameViewStateAnimated.draw(this)
            },
        )
    }
}

private fun computeNewStartAngle(requestResult: RequestResult, viewState: GameViewState): Float? {
    val nodeId = requestResult.parts
        .filterIsInstance<RequestResultPart.Dispatch>()
        .firstOrNull()?.dispatchedNodeId
        ?: requestResult.parts
            .filterIsInstance<RequestResultPart.Extract>()
            .firstOrNull()?.nodeId
    return viewState.nodesView.find { it.id == nodeId }?.angle
}

@Preview(device = Devices.NEXUS_5)
@Composable
private fun GameViewPreview() {
    GameView2(
        gameState = GameState.createInitial()
    )
}