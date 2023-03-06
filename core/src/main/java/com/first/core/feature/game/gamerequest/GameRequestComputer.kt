package com.first.core.feature.game.gamerequest

import com.first.core.feature.game.gameviewstate.ClickResult
import com.first.engine2.game.Action
import com.first.engine2.game.state.GameState
import com.first.engine2.game.request.GameRequest
import com.first.engine2.node.NodeAction

interface GameRequestComputer {

    /**
     * На основе клика и текущего игрового состояния узнать, какой действие необходимо выполнить (см [GameRequest]).
     * @param clickResult информация о клике
     * @param gameState текущее игровое состояние
     */
    fun compute(clickResult: ClickResult, gameState: GameState, tip:Boolean?): GameRequest
}

class GameRequestComputerImpl : GameRequestComputer {

    override fun compute(clickResult: ClickResult, gameState: GameState, tip: Boolean?): GameRequest {
        val clickedId: Int? = clickResult.clickedNodeId

        val activeNodeIsMinus = (gameState.activeNode as? NodeAction)?.action == Action.MINUS
        val activeNodeIsSphere = (gameState.activeNode as? NodeAction)?.action == Action.SPHERE

        if (gameState.nodes.size >= GameState.MAX_ELEM_COUNT) return GameRequest.DoNothing
        if (clickedId == null && !activeNodeIsMinus && !activeNodeIsSphere) return GameRequest.DispatchNode(leftNodeId = clickResult.leftRadialNodeId)


        val isActiveClicked = clickedId == gameState.activeNode.id
        if (isActiveClicked && gameState.prevActiveNodeMinus) return GameRequest.TurnMinusToPlus



        if (activeNodeIsMinus && clickedId != null && !isActiveClicked) return GameRequest.ExtractWithMinus(nodeId = clickedId)

        if (activeNodeIsSphere && clickedId != null && !isActiveClicked) return GameRequest.CopyWithSphere(nodeId = clickedId)

        if (!activeNodeIsMinus && !activeNodeIsSphere) return GameRequest.DispatchNode(leftNodeId = clickResult.leftRadialNodeId)


        if (tip == true) return GameRequest.TurnToAntimatter

        // isAntimatterClicked

        return GameRequest.DoNothing
    }
}