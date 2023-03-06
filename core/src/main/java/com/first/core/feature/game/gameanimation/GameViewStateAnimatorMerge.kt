package com.first.core.feature.game.gameanimation

import android.util.Log
import com.first.core.feature.game.gameviewstate.*
import com.first.engine2.game.result.RequestResultPart
import kotlin.math.absoluteValue

class GameViewStateAnimatorMerge constructor(
    private val start: GameViewState,
    private val mergeEvent: RequestResultPart.Merge,
    private val end: GameViewState,
) : GameViewStateAnimator {

    private val generaAnimator = GameViewStateAnimatorGeneral(start, end)

    override val endState: GameViewState
        get() = end

    override fun animate(fraction: Float): GameViewState {
        Log.d("fraction", "$fraction")
        return if (fraction == 1f) end else computeState(start, end, fraction)
    }

    private fun computeState(start: GameViewState, end: GameViewState, fraction: Float): GameViewState {

        val startNodes: List<NodeView> = start.nodesView
        val endNodes: List<NodeView> = end.nodesView

        val existingNodesPairsView: List<Pair<NodeView, NodeView>> = generaAnimator.computeExistingNodes(startNodes, endNodes)
        val nodesToAdd: List<NodeView> = generaAnimator.computeNodesToAdd(startNodes, endNodes)

        val mergedNodeAngle = computeMergedAngle(start)

        val nodesToMergeIds = listOf(mergeEvent.nodeId1, mergeEvent.nodeId2)
        val nodesToMerge: List<NodeView> = startNodes.filter { nodesToMergeIds.contains(it.id) }

        val preMergedNode = startNodes.find { it.id == mergeEvent.preMergeId }!!

        return GameViewState(
            dimens = end.dimens,
            nodesView = existingNodesPairsView.map { generaAnimator.animate(startNodeState = it.first, endNodeState = it.second, fraction = fraction) }
                    + animatePreMerged(startNodeState = preMergedNode, fraction = fraction, mergeAngle = mergedNodeAngle)
                    + nodesToMerge.map { animateMerged(startNodeState = it, fraction = fraction, mergeAngle = mergedNodeAngle) }
                    + nodesToAdd.map { generaAnimator.animate(startNodeState = null, endNodeState = it, fraction = fraction) }
        )
    }

    private fun computeMergedAngle(start: GameViewState): Float {
        val preMergeId = mergeEvent.preMergeId
        val preMergeNode: NodeView = start.nodesView.find { it.id == preMergeId }
            ?: throw RuntimeException("Аномалия: в конечном состоянии обязательно должна присутствовать нода, получившаяся в результате слияния")
        return preMergeNode.angle
    }

    private fun computeEndAngleToMerge(startAngle: Float, mergeAngle: Float): Float {
        val endAngle: Float = mergeAngle

        val cwEndAngle = if (startAngle <= endAngle) endAngle else 360 + endAngle
        val ccwEndAngle = if (startAngle >= endAngle) endAngle else endAngle - 360
        val cwD = (startAngle - cwEndAngle).absoluteValue
        val ccwD = (startAngle - ccwEndAngle).absoluteValue
        return if (cwD < ccwD) cwEndAngle else ccwEndAngle
    }

    private fun animateMerged(startNodeState: NodeView, fraction: Float, mergeAngle: Float): NodeView {
        val startAngle = startNodeState.angle
        val endAngle = computeEndAngleToMerge(startAngle, mergeAngle)
        val angle = startAngle + (endAngle - startAngle) * fraction

        return when (startNodeState) {
            is NodeActionView -> startNodeState.copy(angle = angle)
            is NodeElementView -> startNodeState.copy(angle = angle)
        }
    }

    private fun animatePreMerged(startNodeState: NodeView, fraction: Float, mergeAngle: Float): NodeView {
        val startAngle = startNodeState.angle
        val endAngle = computeEndAngleToMerge(startAngle, mergeAngle)
        val angle = startAngle + (endAngle - startAngle) * fraction

        val radius = generaAnimator.animateRadius(startNodeState, null, fraction)

        return when (startNodeState) {
            is NodeActionView -> startNodeState.copy(angle = angle, radiusPx = radius)
            is NodeElementView -> startNodeState.copy(angle = angle, radiusPx = radius)
        }
    }
}