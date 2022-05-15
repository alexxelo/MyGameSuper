package com.example.engine2.game.result

sealed class RequestResultPart {

    data class Dispatch constructor(
        val dispatchedNodeId: Int,
        val leftNodeId: Int,
        val rightNodeId: Int,
        val newActiveNodeId: Int,
    ) : RequestResultPart()

    data class Merge constructor(
        val nodeId1: Int,
        val nodeId2: Int,
        val resultId: Int,
    ) : RequestResultPart()

    data class Extract constructor(val nodeId: Int) : RequestResultPart()

    object TurnMinusToPlus : RequestResultPart()

    object DoNothing : RequestResultPart()
}