package com.example.core.feature.game.gamerequest

import com.example.core.feature.game.gameviewstate.ClickResult
import com.example.engine2.game.Action
import com.example.engine2.game.GameState
import com.example.engine2.game.request.GameRequest
import com.example.engine2.node.NodeAction

interface GameRequestComputer {

    fun compute(clickResult: ClickResult, gameState: GameState): GameRequest
}

class GameRequestComputerImpl : GameRequestComputer {

    override fun compute(clickResult: ClickResult, gameState: GameState): GameRequest {
        val clickedId: Int? = clickResult.clickedNodeId

        val activeNodeIsMinus = (gameState.activeNode as? NodeAction)?.action == Action.MINUS

        if (gameState.nodes.size >= GameState.MAX_ELEM_COUNT) return GameRequest.DoNothing
        if (clickedId == null && !activeNodeIsMinus) return GameRequest.DispatchNode(clickResult.leftNodeIndex)

        val isActiveClicked = clickedId == gameState.activeNode.id
        if (isActiveClicked && gameState.prevActiveNodeMinus) return GameRequest.TurnMinusToPlus
        if (activeNodeIsMinus && clickedId != null) return GameRequest.ExtractWithMinus(clickedId)

        return GameRequest.DoNothing
    }
}