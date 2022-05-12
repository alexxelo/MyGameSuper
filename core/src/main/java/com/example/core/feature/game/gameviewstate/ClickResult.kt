package com.example.core.feature.game.gameviewstate

data class ClickResult constructor(
    val leftNodeIndex: Int,
    val clickedNodeId: Int? = null
)