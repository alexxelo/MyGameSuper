package com.example.core.feature.game.gameanimation

sealed class GameAnimationPart {

    class NodeBoopAnimation constructor(val nodeId: Int, val boopScale: Float): GameAnimationPart()

    class NodeDistanceAnimation constructor(val nodeId: Int, val targetDistance: Float): GameAnimationPart()

    class NodeMergeAnimation constructor(
        val nodeMove1: NodeAngleMoveAnimation,
        val nodeMove2: NodeAngleMoveAnimation,
    )

    class NodeCircleMoveAnimation


    class NodeAngleMoveAnimation constructor(val nodeId: Int, val targetAngle: Float)
}