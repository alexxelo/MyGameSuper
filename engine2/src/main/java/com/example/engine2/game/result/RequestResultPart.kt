package com.example.engine2.game.result

sealed class RequestResultPart {

    data class Dispatch constructor(
        val leftNodeId: Int,
        val dispatchedNodeId: Int,
        val newActiveNodeId: Int,
    ) : RequestResultPart()

    data class Merge constructor(val nodeId1: Int, val nodeId2: Int) : RequestResultPart()

    data class Extract constructor(val nodeId: Int) : RequestResultPart()

    object TurnMinusToPlus : RequestResultPart()

    object DoNothing : RequestResultPart()
}