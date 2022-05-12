package com.example.core.feature.game

import androidx.compose.animation.core.animateFloatAsState
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
import com.example.engine2.game.Action
import com.example.engine2.game.Element
import com.example.engine2.game.GameState
import com.example.engine2.game.request.GameRequest
import com.example.engine2.game.result.RequestResult
import com.example.engine2.game.result.RequestResultPart
import com.example.engine2.node.NodeAction
import com.example.engine2.node.NodeElement

@Composable
fun GameView2(modifier: Modifier = Modifier, gameState: GameState) {
    val coroutine = rememberCoroutineScope()
    var gameStateState: GameState by remember { mutableStateOf(gameState) }

    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }
        var fractionStart by remember { mutableStateOf(0f) }
        var fractionEnd by remember { mutableStateOf(1f) }
        val fractionAnimatable by animateFloatAsState(targetValue = fractionEnd)
        var gameViewStateStart: GameViewState? by remember { mutableStateOf(null) }
        var gameViewStateEnd: GameViewState by remember { mutableStateOf(GameViewState.createFrom(gameStateState, widthPx, heightPx)) }
        val gameAnimator: GameViewStateAnimator by remember { mutableStateOf(GameViewStateAnimator(gameViewStateEnd)) }
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
                        val clickResult: ClickResult = gameViewStateEnd.click(clickPoint)
                        val gameRequest: GameRequest = GameRequestComputerImpl().compute(clickResult, gameStateState)
                        val requestResult: RequestResult = gameStateState.executeRequest(gameRequest)

                        val newState = gameStateState.clone()
                        val newViewState = GameViewState.createFrom(
                            gameState = newState,
                            widthPx = widthPx,
                            heightPx = heightPx,
                        )
                        gameStateState = newState
                        fractionStart++
                        fractionEnd++
                        gameViewStateStart = gameViewStateEnd.copy()
                        gameViewStateEnd = newViewState

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
        ?:requestResult.parts
            .filterIsInstance<RequestResultPart.Extract>()
            .firstOrNull()?.nodeId
    return viewState.nodesView.find { it.id == nodeId }?.angle
}

@Preview(device = Devices.NEXUS_5)
@Composable
private fun GameViewPreview() {
    GameView2(
        gameState = GameState(
            nodes = mutableListOf(
                NodeElement(element = Element(2), 1),
                NodeElement(element = Element(2), 2),
                NodeElement(element = Element(4), 3),
                NodeElement(element = Element(4), 4),
                NodeElement(element = Element(4), 5),
                NodeElement(element = Element(4), 6),
                NodeElement(element = Element(3), 7),
                NodeAction(action = Action.PLUS, 8)
            ),
            initialActiveNode = NodeElement(
                element = Element(3),
                9
            )//NodeAction(action = Action.MINUS)
        )
    )
}