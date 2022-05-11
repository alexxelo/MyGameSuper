package com.example.core.feature.game.gameviewstate.fabric

import com.example.core.GameViewUtils
import com.example.core.feature.game.gameviewstate.*
import com.example.engine2.game.GameState
import com.example.engine2.node.Node
import com.example.engine2.node.NodeAction
import com.example.engine2.node.NodeElement

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
        val nodeRadiusPx = dimens.nodeRadiusPx
        val nodeId = node.id
        return when (node) {
            is NodeAction -> NodeActionView(
                id = nodeId,
                angle = 0f,
                distancePx = 0f,
                radiusPx = nodeRadiusPx,
                bgColor = GameViewUtils.getNodeActionBgColor(node.action),
                type = node.action,
            )
            is NodeElement -> kotlin.run {
                val mass = node.element.atomicMass
                NodeElementView(
                    id = nodeId,
                    angle = 0f,
                    distancePx = 0f,
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
        val nodeId = node.id
        return when (node) {
            is NodeAction -> kotlin.run {
                val action = node.action
                NodeActionView(
                    id = nodeId,
                    angle = angle,
                    distancePx = distance,
                    radiusPx = nodeRadiusPx,
                    bgColor = GameViewUtils.getNodeActionBgColor(action),
                    type = action,
                )
            }
            is NodeElement -> kotlin.run {
                val mass = node.element.atomicMass
                NodeElementView(
                    id = nodeId,
                    angle = angle,
                    distancePx = distance,
                    radiusPx = nodeRadiusPx,
                    bgColor = GameViewUtils.getNodeElementBgColor(mass),
                    name = GameViewUtils.getNodeElementName(mass),
                    atomicMass = "$mass",
                )
            }
        }
    }
}