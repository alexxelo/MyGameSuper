package com.example.core.feature.game.gameviewstate.transformer

import com.example.core.feature.game.gameviewstate.GameViewState
import com.example.core.feature.game.gameviewstate.GameViewStateDimensions
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
            is RequestResultPart.Merge -> noTransform(dimens, resultGameState)
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
        val newAngleStep = 360f / resultGameState.nodes.size
        val angleStart = (dispatchedNodeRequiredAngle - newAngleStep * dispatchedNodeIndex).let { (it + 360) % 360  }
        return GameViewState.createFrom(
            gameState = resultGameState,
            dimens = dimens.copy(angleStep = newAngleStep, startAngle = angleStart),
        )
    }

    private fun transformExtract(
        initialViewState: GameViewState,
        extractEvent: RequestResultPart.Extract,
        resultGameState: GameState,
    ): GameViewState {
        val dimens: GameViewStateDimensions = initialViewState.dimens
        val extractedId = extractEvent.nodeId

        val extractedNodeAngle = initialViewState.nodesView.find { it.id == extractedId }!!.angle
        val nodeLeftByExtracted = initialViewState.leftNodeFrom(extractedNodeAngle)
        val nodeLeftByExtractedId = nodeLeftByExtracted.id
        val nodeLeftByExtractedIndex = resultGameState.nodes.indexOfFirst { it.id == nodeLeftByExtractedId }

        val newAngleStep = 360f / resultGameState.nodes.size
        val requiredLeftNodeByExtractedAngle = extractedNodeAngle - newAngleStep / 2
        val angleStart = (requiredLeftNodeByExtractedAngle - newAngleStep * nodeLeftByExtractedIndex).let { (it + 360) % 360  }
        return GameViewState.createFrom(
            gameState = resultGameState,
            dimens = dimens.copy(angleStep = newAngleStep, startAngle = angleStart),
        )
    }

    private fun noTransform(
        prevViewStateDimens: GameViewStateDimensions,
        state: GameState,
    ): GameViewState {
        val newViewStateDimens = prevViewStateDimens.copy(angleStep = 360f / state.nodes.size)
        return GameViewState.createFrom(
            gameState = state,
            dimens = newViewStateDimens,
        )
    }
}