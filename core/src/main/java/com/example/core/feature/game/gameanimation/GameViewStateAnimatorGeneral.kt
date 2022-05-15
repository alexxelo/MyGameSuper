package com.example.core.feature.game.gameanimation

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.core.feature.game.gameviewstate.*
import kotlin.math.absoluteValue

class GameViewStateAnimatorGeneral constructor(private val start: GameViewState, private val end: GameViewState) : GameViewStateAnimator {

    override val endState: GameViewState
        get() = end

    override fun animate(fraction: Float): GameViewState {
        Log.d("fraction", "$fraction")
        return if (fraction == 1f) end else computeState(start, end, fraction)
    }

    fun computeExistingNodes(startNodes: List<NodeView>, endNodes: List<NodeView>): List<Pair<NodeView, NodeView>> {
        return startNodes.mapNotNull { startNodeView ->
            val startNodeId: Int = startNodeView.id
            val pairedView: NodeView? = endNodes.find { it.id == startNodeId }
            pairedView?.let { startNodeView to it }
        }
    }

    fun computeNodesToRemove(startNodes: List<NodeView>, endNodes: List<NodeView>): List<NodeView> {
        val endNodeIds = endNodes.map(NodeView::id)
        return startNodes.filterNot { endNodeIds.contains(it.id) }
    }

    fun computeNodesToAdd(startNodes: List<NodeView>, endNodes: List<NodeView>): List<NodeView> {
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
            nodesView = existingNodesPairsView.map { animate(startNodeState = it.first, endNodeState = it.second, fraction = fraction) }
                    + nodesToRemove.map { animate(startNodeState = it, endNodeState = null, fraction = fraction) }
                    + nodesToAdd.map { animate(startNodeState = null, endNodeState = it, fraction = fraction) }
        )
    }

    private fun computeStartAngle(startNodeState: NodeView?, endNodeState: NodeView?): Float {
        val startNodeIsActive: Boolean = startNodeState?.isActive == true
        return (if (startNodeIsActive) endNodeState?.angle else startNodeState?.angle) ?: 0f
    }

    private fun computeEndAngle(startNodeState: NodeView?, endNodeState: NodeView?, startAngle: Float): Float {
        val endNodeIsActive: Boolean = endNodeState?.isActive == true
        val endAngle: Float = (if (endNodeIsActive) startNodeState?.angle else endNodeState?.angle) ?: 0f


        val cwEndAngle = if (startAngle <= endAngle) endAngle else 360 + endAngle
        val ccwEndAngle = if (startAngle >= endAngle) endAngle else endAngle - 360
        val cwD = (startAngle - cwEndAngle).absoluteValue
        val ccwD = (startAngle - ccwEndAngle).absoluteValue
        return if (cwD < ccwD) cwEndAngle else ccwEndAngle
//        return if (endAngle > startAngle) 360f + startAngle else endAngle
    }

    fun animate(startNodeState: NodeView?, endNodeState: NodeView?, fraction: Float): NodeView {
        val startOrEnd: NodeView = startNodeState ?: endNodeState ?: throw RuntimeException("Аномалия: нода обязательна должна быть либо в начальном, либо в кончальном состоянии")
        val endOrStart: NodeView = endNodeState ?: startNodeState ?: throw RuntimeException("Аномалия: нода обязательна должна быть либо в начальном, либо в кончальном состоянии")
        val anyNode = startOrEnd

        val id: Int = startOrEnd.id
        val bgColor: Color = startOrEnd.bgColor

        val startAngle = computeStartAngle(startNodeState, endNodeState)
        val endAngle = computeEndAngle(startNodeState, endNodeState, startAngle)
        val angle = startAngle + (endAngle - startAngle) * fraction

        val startDistance = startOrEnd.distancePx
        val endDistance = endOrStart.distancePx
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