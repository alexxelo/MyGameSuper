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
import com.example.core.feature.game.gamerequest.GameRequestComputerImpl
import com.example.core.feature.game.gameviewstate.ClickResult
import com.example.core.feature.game.gameviewstate.GameViewState
import com.example.engine2.game.GameState
import com.example.engine2.game.request.GameRequest
import com.example.engine2.game.result.RequestResult
import com.example.engine2.game.result.RequestResultPart

@Composable
fun GameView2(modifier: Modifier = Modifier, gameState: GameState) {
    val coroutine = rememberCoroutineScope()
    var gameStateState: GameState by remember { mutableStateOf(gameState) }

    BoxWithConstraints(modifier = modifier) {

        val density = LocalDensity.current
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }

        val initialViewState = GameViewState.createFrom(gameStateState, widthPx, heightPx)
        var gameViewStates: List<GameViewState> by remember { mutableStateOf(listOf(initialViewState)) }

        var gameViewStateStart: GameViewState by remember { mutableStateOf(initialViewState) }
        var gameViewStateEnd: GameViewState? by remember { mutableStateOf(null) }

        var fractionStart by remember { mutableStateOf(0f) }
        var fractionEnd by remember { mutableStateOf(1f) }
        val fractionAnimatable by animateFloatAsState(
            targetValue = fractionEnd,
            animationSpec = tween(durationMillis = 225),
            finishedListener = {
                if (gameViewStates.isNotEmpty()) {
                    gameViewStates = gameViewStates.drop(1)
                }
                if (gameViewStates.isNotEmpty()) {
                    fractionStart++
                    fractionEnd++
                    val newStart = gameViewStates[0]
                    gameViewStateStart = newStart
                    val newEnd = gameViewStates.getOrNull(1)
                    gameViewStateEnd = newEnd
                }
            }
        )

        val gameAnimator: GameViewStateAnimator by remember { mutableStateOf(GameViewStateAnimator()) }
        val gameViewStateAnimated: GameViewState = gameAnimator.animate(
            start = gameViewStateStart,
            end = gameViewStateEnd,
            fraction = fractionAnimatable - fractionStart,
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { clickPoint ->
                        // Получить информацию о клике
                        val clickResult: ClickResult = gameViewStateStart.click(clickPoint)
                        // На основе клика узнать, какое действите нужно выполнить
                        val gameRequest: GameRequest = GameRequestComputerImpl().compute(clickResult, gameStateState)
                        val subStates = listOf(gameStateState.clone()) + gameStateState.executeRequest(gameRequest)

                        val newState = subStates.last()
                        gameStateState = newState
                        gameViewStates = subStates.map { subState ->
                            GameViewState.createFrom(
                                gameState = subState,
                                widthPx = widthPx,
                                heightPx = heightPx,
                            )
                        }
                        fractionStart++
                        fractionEnd++
                        val newStart = gameViewStates[0]
                        gameViewStateStart = newStart
                        val newEnd = gameViewStates.getOrNull(1)
                        gameViewStateEnd = newEnd
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