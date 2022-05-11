package com.example.core.feature.game.gameviewstate.fabric

import androidx.compose.ui.geometry.Offset
import com.example.core.GameViewUtils
import com.example.core.feature.game.gameviewstate.*
import com.example.engine2.GameState
import com.example.engine2.Node
import com.example.engine2.NodeAction
import com.example.engine2.NodeElement
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class GameViewStateFabricImpl : GameViewStateFabric {

    override fun createFrom(gameState: GameState, widthPx: Float, heightPx: Float): GameViewState {
        val dimens = GameViewStateDimensions.createFrom(gameState, widthPx, heightPx)
        return GameViewState(
            dimens = dimens,
            activeNodeView = createActiveNode(gameState.activeNode, dimens),
            nodesView = gameState.nodes.mapIndexed { index, node ->
                createRadialNode(node = node, index = index, dimens = dimens)
            },
        )
    }

    private fun createActiveNode(node: Node, dimens: GameViewStateDimensions): NodeView {
        val center = dimens.center
        val nodeRadiusPx = dimens.nodeRadiusPx
        val nodeId = node.id
        return when (node) {
            is NodeAction -> NodeActionView(
                id = nodeId,
                center = center,
                radiusPx = nodeRadiusPx,
                bgColor = GameViewUtils.getNodeActionBgColor(node.action),
                type = node.action,
            )
            is NodeElement -> kotlin.run {
                val mass = node.element.atomicMass
                NodeElementView(
                    id = nodeId,
                    center = center,
                    radiusPx = nodeRadiusPx,
                    bgColor = GameViewUtils.getNodeElementBgColor(mass),
                    name = GameViewUtils.getNodeElementName(mass),
                    atomicMass = "$mass",
                )
            }
        }
    }

    private fun createRadialNode(node: Node, index: Int, dimens: GameViewStateDimensions): NodeView {
        val nodeRadiusPx = dimens.nodeRadiusPx
        val distance = dimens.outerCircleRadiusPx - 1.5f * nodeRadiusPx
        val angle = index * dimens.angleStep
        val angleNodeRad = (angle / 180 * PI).toFloat()
        val centerOffset = Offset(
            x = distance * cos(angleNodeRad),
            y = distance * sin(angleNodeRad),
        )
        val center = dimens.center + centerOffset
        val nodeId = node.id
        return when (node) {
            is NodeAction -> kotlin.run {
                val action = node.action
                NodeActionView(
                    id = nodeId,
                    center = center,
                    radiusPx = nodeRadiusPx,
                    bgColor = GameViewUtils.getNodeActionBgColor(action),
                    type = action,
                )
            }
            is NodeElement -> kotlin.run {
                val mass = node.element.atomicMass
                NodeElementView(
                    id = nodeId,
                    center = center,
                    radiusPx = nodeRadiusPx,
                    bgColor = GameViewUtils.getNodeElementBgColor(mass),
                    name = GameViewUtils.getNodeElementName(mass),
                    atomicMass = "$mass",
                )
            }
        }
    }
}