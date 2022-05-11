package com.example.core.feature.game.gameviewstate

import androidx.compose.ui.geometry.Offset
import com.example.engine2.GameState

data class GameViewStateDimensions constructor(
    val widthPx: Float,
    val heightPx: Float,
    val outerCircleRadiusPx: Float,
    val center: Offset,
    val nodeRadiusPx: Float,
    val angleStep: Float,
) {

    companion object {

        fun createFrom(gameState: GameState, widthPx: Float, heightPx: Float): GameViewStateDimensions {
            val outerCircleRadius = widthPx / 2 * 0.9f
            val wCenter = widthPx / 2
            val hCenter = heightPx / 2
            val center = Offset(wCenter, hCenter)
            val nodeRadius = outerCircleRadius / 8
            val angleStep = 360f / gameState.nodes.size

            return GameViewStateDimensions(
                widthPx = widthPx,
                heightPx = heightPx,
                outerCircleRadiusPx = outerCircleRadius,
                center = center,
                nodeRadiusPx = nodeRadius,
                angleStep = angleStep,
            )
        }
    }
}