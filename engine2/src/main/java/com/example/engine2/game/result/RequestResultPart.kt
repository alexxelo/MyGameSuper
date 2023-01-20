package com.example.engine2.game.result

sealed class RequestResultPart {

    data class Dispatch constructor(
        val dispatchedNodeId: Int,
        val leftNodeId: Int,
        val rightNodeId: Int,
        val newActiveNodeId: Int,
    ) : RequestResultPart()

    /**
     * @param nodeId1 id первой ноды, которая будет смержена
     * @param nodeId2 id второй ноды, которая будет смержена
     * @param preMergeId id ноды-плюсика, который будет мержить вокруг себя [nodeId1] и [nodeId2]
     * @param postMergeId id ноды, которая появилась в результате слияния [nodeId1] и [nodeId2]
     */
    data class Merge constructor(
        val nodeId1: Int,
        val nodeId2: Int,
        val preMergeId: Int,
        val postMergeId: Int,
    ) : RequestResultPart()

    data class Extract constructor(val nodeId: Int) : RequestResultPart()

    data class Copy constructor(val nodeId: Int) : RequestResultPart()

    object TurnMinusToPlus : RequestResultPart()

    object TurnToAntimatter : RequestResultPart()

    object DoNothing : RequestResultPart()
}