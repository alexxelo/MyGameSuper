package com.example.engine2.game.request

sealed class GameRequest {

    class DispatchNode constructor(val leftNodeIndex: Int): GameRequest()

    class ExtractWithMinus constructor(val nodeId: Int) : GameRequest()

    object TurnMinusToPlus : GameRequest()

    object DoNothing : GameRequest()
}