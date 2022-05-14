package com.example.core.feature.game.gameviewstate.fabric

import com.example.core.GameViewUtils
import com.example.core.feature.game.gameviewstate.*
import com.example.engine2.game.state.GameState
import com.example.engine2.node.Node
import com.example.engine2.node.NodeAction
import com.example.engine2.node.NodeElement

class GameViewStateFabricImpl : GameViewStateFabric {

    override fun createFrom(gameState: GameState, dimens: GameViewStateDimensions): GameViewState {
        val circleNodes = gameState.nodes.mapIndexed { index, node ->
            createRadialNode(node = node, index = index, dimens = dimens, startAngle = dimens.startAngle)
        }
        val activeNode = createActiveNode(gameState.activeNode, dimens)
        val allNodes = circleNodes + activeNode
        return GameViewState(
            dimens = dimens,
            nodesView = allNodes,
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

    private fun createRadialNode(node: Node, index: Int, dimens: GameViewStateDimensions, startAngle: Float = 0f): NodeView {
        val nodeRadiusPx = dimens.nodeRadiusPx
        val distance = dimens.outerCircleRadiusPx - 1.5f * nodeRadiusPx
        val angleStep = dimens.angleStep
        val angle = startAngle + index * angleStep
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