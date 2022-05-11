package com.example.core.feature.game

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
import com.example.core.feature.game.gamerequest.GameRequestComputerImpl
import com.example.core.feature.game.gameviewstate.ClickResult
import com.example.core.feature.game.gameviewstate.GameViewState
import com.example.engine2.*
import com.example.engine2.game.Action
import com.example.engine2.game.Element
import com.example.engine2.game.GameState
import com.example.engine2.game.request.GameRequest
import com.example.engine2.node.NodeAction
import com.example.engine2.node.NodeElement

@Composable
fun GameView2(modifier: Modifier = Modifier, gameState: GameState) {
    var gameStateState: GameState by remember { mutableStateOf(gameState) }

    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }
        var gameViewStateState: GameViewState by remember { mutableStateOf(GameViewState.createFrom(gameState, widthPx, heightPx)) }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { clickPoint ->
                        val clickResult: ClickResult = gameViewStateState.click(clickPoint)
                        val gameRequest: GameRequest = GameRequestComputerImpl().compute(clickResult, gameStateState)
                        gameStateState.executeRequest(gameRequest)

                        val newState = gameStateState.clone()
                        val newViewState = GameViewState.createFrom(newState, widthPx, heightPx)
                        gameStateState = newState
                        gameViewStateState = newViewState
                    }

                },
            onDraw = {
                gameViewStateState.draw(this)
            },
        )
    }
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