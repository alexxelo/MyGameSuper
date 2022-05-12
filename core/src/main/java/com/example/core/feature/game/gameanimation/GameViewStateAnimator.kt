package com.example.core.feature.game.gameanimation

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.example.core.feature.game.gameviewstate.*

class GameViewStateAnimator {

    @Composable
    fun animate(
        start: GameViewState,
        end: GameViewState?,
        fraction: Float,
    ): GameViewState {
        Log.d("fraction" ,"$fraction")
        return if (end == null) {
            start
        } else {
            if (fraction == 1f) end else computeState(start, end, fraction)
        }
    }

    private fun computeExistingNodes(startNodes: List<NodeView>, endNodes: List<NodeView>): List<Pair<NodeView, NodeView>> {
        return startNodes.mapNotNull { startNodeView ->
            val startNodeId: Int = startNodeView.id
            val pairedView: NodeView? = endNodes.find { it.id == startNodeId }
            pairedView?.let { startNodeView to it }
        }
    }

    private fun computeNodesToRemove(startNodes: List<NodeView>, endNodes: List<NodeView>): List<NodeView> {
        val endNodeIds = endNodes.map(NodeView::id)
        return startNodes.filterNot { endNodeIds.contains(it.id) }
    }

    private fun computeNodesToAdd(startNodes: List<NodeView>, endNodes: List<NodeView>): List<NodeView> {
        val startNodeIds = startNodes.map(NodeView::id)
        return endNodes.filterNot { startNodeIds.contains(it.id) }
    }

    private fun computeState(start: GameViewState, end: GameViewState, fraction: Float): GameViewState {

        val startNodes: List<NodeView> = start.nodesView
        val endNodes: List<NodeView> = end.nodesView

        val existingNodesPairsView: List<Pair<NodeView, NodeView>> = computeExistingNodes(startNodes, endNodes)
        val nodesToRemove: List<NodeView> = computeNodesToRemove(startNodes, endNodes)
        val nodesToAdd: List<NodeView> = computeNodesToAdd(startNodes, endNodes)

        return GameViewState(
            dimens = end.dimens,
            nodesView = existingNodesPairsView.mapNotNull { animate(startNodeState = it.first, endNodeState = it.second, fraction = fraction) }
                    + nodesToRemove.mapNotNull { animate(startNodeState = it, endNodeState = null, fraction = fraction) }
                    + nodesToAdd.mapNotNull { animate(startNodeState = null, endNodeState = it, fraction = fraction) }
        )
    }

    private fun animate(startNodeState: NodeView?, endNodeState: NodeView?, fraction: Float): NodeView? {
        val anyNode: NodeView = startNodeState ?: endNodeState ?: return null
        val startNodeIsActive = startNodeState?.isActive == true
        val endNodeIsActive = endNodeState?.isActive == true

        val id: Int = anyNode.id
        val bgColor: Color = anyNode.bgColor

        val startAngle = (if (startNodeIsActive) endNodeState?.angle else startNodeState?.angle) ?: 0f
        val endAngle = (if (endNodeIsActive) startNodeState?.angle else endNodeState?.angle) ?: 0f
        val angle = startAngle + (endAngle - startAngle) * fraction

        val startDistance = startNodeState?.distancePx ?: endNodeState?.distancePx ?: return null
        val endDistance = endNodeState?.distancePx ?: startNodeState?.distancePx ?: return null
        val distancePx = startDistance + (endDistance - startDistance) * fraction

        val startRadius = startNodeState?.radiusPx ?: 0f
        val endRadius = endNodeState?.radiusPx ?: 0f
        val radiusPx = startRadius + (endRadius - startRadius) * fraction * fraction

        return when (anyNode) {
            is NodeActionView -> NodeActionView(
                id = id,
                angle = angle,
                distancePx = distancePx,
                radiusPx = radiusPx,
                bgColor = bgColor,
                type = anyNode.type
            )
            is NodeElementView -> NodeElementView(
                id = id,
                angle = angle,
                distancePx = distancePx,
                radiusPx = radiusPx,
                bgColor = bgColor,
                name = anyNode.name,
                atomicMass = anyNode.atomicMass,
            )
        }
    }
}