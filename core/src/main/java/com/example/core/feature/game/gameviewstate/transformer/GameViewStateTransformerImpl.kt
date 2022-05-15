package com.example.core.feature.game.gameviewstate.transformer

import com.example.core.feature.game.gameviewstate.GameViewState
import com.example.core.feature.game.gameviewstate.GameViewStateDimensions
import com.example.core.feature.game.gameviewstate.NodeView
import com.example.engine2.game.result.RequestResultPart
import com.example.engine2.game.state.GameState

class GameViewStateTransformerImpl constructor() : GameViewStateTransformer {

    override fun transform(
        initialViewState: GameViewState,
        requestResultPart: RequestResultPart,
        resultGameState: GameState,
    ): GameViewState {
        val dimens = initialViewState.dimens
        return when (requestResultPart) {
            is RequestResultPart.Dispatch -> transformDispatch(initialViewState, requestResultPart, resultGameState)
//            is RequestResultPart.Dispatch -> noTransform(dimens, resultGameState)
            is RequestResultPart.DoNothing -> noTransform(dimens, resultGameState)
            is RequestResultPart.Extract -> transformExtract(initialViewState, requestResultPart, resultGameState)
//            is RequestResultPart.Extract -> noTransform(dimens, resultGameState)
            is RequestResultPart.Merge -> transformMerge(initialViewState, requestResultPart, resultGameState)
//            is RequestResultPart.Merge -> noTransform(dimens, resultGameState)
            is RequestResultPart.TurnMinusToPlus -> noTransform(dimens, resultGameState)
        }
    }

    private fun transformDispatch(
        initialViewState: GameViewState,
        dispatchEvent: RequestResultPart.Dispatch,
        resultGameState: GameState,
    ): GameViewState {
        val dimens: GameViewStateDimensions = initialViewState.dimens
        val dispatchedNodeId = dispatchEvent.dispatchedNodeId
        val leftNode = initialViewState.nodesView.find { it.id == dispatchEvent.leftNodeId }!!
//        val rightNode = initialViewState.nodesView.find { it.id == dispatchEvent.rightNodeId }!!
        val leftNodeAngle = leftNode.angle
        val rightNodeAngle = leftNodeAngle + dimens.angleStep
        val dispatchedNodeRequiredAngle = (leftNodeAngle + rightNodeAngle) / 2
        val dispatchedNodeIndex = resultGameState.nodes.indexOfFirst { it.id == dispatchedNodeId }
        val newAngleStep = GameViewStateDimensions.computeAngleStep(resultGameState)
        val startAngle = (dispatchedNodeRequiredAngle - newAngleStep * dispatchedNodeIndex).let { (it + 360) % 360 }
        return GameViewState.createFrom(
            gameState = resultGameState,
            dimens = dimens.copy(angleStep = newAngleStep, startAngle = startAngle),
        )
    }

    private fun transformExtract(
        initialViewState: GameViewState,
        extractEvent: RequestResultPart.Extract,
        resultGameState: GameState,
    ): GameViewState {
        val dimens: GameViewStateDimensions = initialViewState.dimens
        val extractedId = extractEvent.nodeId

        val extractedNodeAngle: Float = initialViewState.nodesView.find { it.id == extractedId }!!.angle
        val nodeLeftByExtracted: NodeView? = initialViewState.leftNodeFrom(extractedNodeAngle)
        val nodeLeftByExtractedId: Int? = nodeLeftByExtracted?.id
        val nodeLeftByExtractedIndex: Int = resultGameState.nodes.indexOfFirst { it.id == nodeLeftByExtractedId }

        val newAngleStep = GameViewStateDimensions.computeAngleStep(resultGameState)
        val requiredLeftNodeByExtractedAngle = extractedNodeAngle - newAngleStep / 2
        val startAngle = (requiredLeftNodeByExtractedAngle - newAngleStep * nodeLeftByExtractedIndex).let { (it + 360) % 360 }
        return GameViewState.createFrom(
            gameState = resultGameState,
            dimens = dimens.copy(angleStep = newAngleStep, startAngle = startAngle),
        )
    }

    private fun transformMerge(
        initialViewState: GameViewState,
        mergeEvent: RequestResultPart.Merge,
        resultGameState: GameState,
    ): GameViewState {
        val dimens: GameViewStateDimensions = initialViewState.dimens
        val preMergeId = mergeEvent.preMergeId
        val postMergeId = mergeEvent.postMergeId

        val newAngleStep = GameViewStateDimensions.computeAngleStep(resultGameState)

        val preMergeNodeAngle = initialViewState.nodesView.find { it.id == preMergeId }!!.angle
        val requiredPostMergeAngle = preMergeNodeAngle
        val postMergeIndex = resultGameState.nodes.indexOfFirst { it.id == postMergeId }
        val startAngle = (requiredPostMergeAngle - newAngleStep * postMergeIndex).let { (it + 360) % 360 }

        return GameViewState.createFrom(
            gameState = resultGameState,
            dimens = dimens.copy(angleStep = newAngleStep, startAngle = startAngle),
        )
    }

    private fun noTransform(
        prevViewStateDimens: GameViewStateDimensions,
        state: GameState,
    ): GameViewState {
        val newViewStateDimens = prevViewStateDimens.copy(angleStep = GameViewStateDimensions.computeAngleStep(state))
        return GameViewState.createFrom(
            gameState = state,
            dimens = newViewStateDimens,
        )
    }
}