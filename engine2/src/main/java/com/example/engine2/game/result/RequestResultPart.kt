package com.example.engine2.game.result

sealed class RequestResultPart {

    class Dispatch constructor(
        val leftNodeId: Int,
        val dispatchedNodeId: Int,
        val newActiveNodeId: Int,
    ) : RequestResultPart()

    class Merge constructor(val nodeId1: Int, val nodeId2: Int) : RequestResultPart()

    class Extract constructor(val nodeId: Int) : RequestResultPart()

    object TurnMinusToPlus : RequestResultPart()

    object DoNothing : RequestResultPart()
}