package com.example.core.feature.game.gameviewstate

import androidx.compose.ui.geometry.Offset
import com.example.engine2.game.state.GameState

data class GameViewStateDimensions constructor(
    val widthPx: Float,
    val heightPx: Float,
    val outerCircleRadiusPx: Float,
    val maxNodeDistance: Float,
    val center: Offset,
    val nodeRadiusPx: Float,
    val angleStep: Float,
    val startAngle: Float,
) {

    companion object {

        fun createFrom(gameState: GameState, widthPx: Float, heightPx: Float, startAngle: Float = 0f): GameViewStateDimensions {
            val outerCircleRadius = widthPx / 2 * 0.9f
            val wCenter = widthPx / 2
            val hCenter = heightPx / 2
            val center = Offset(wCenter, hCenter)
            val nodeRadius = outerCircleRadius / 8
            val angleStep = 360f / gameState.nodes.size
            val maxNodeDistance = outerCircleRadius - 1.5f * nodeRadius

            return GameViewStateDimensions(
                widthPx = widthPx,
                heightPx = heightPx,
                outerCircleRadiusPx = outerCircleRadius,
                maxNodeDistance = maxNodeDistance,
                center = center,
                nodeRadiusPx = nodeRadius,
                angleStep = angleStep,
                startAngle = startAngle,
            )
        }
    }
}