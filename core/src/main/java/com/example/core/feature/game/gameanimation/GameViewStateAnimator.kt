package com.example.core.feature.game.gameanimation

import com.example.core.feature.game.gameviewstate.GameViewState

interface GameViewStateAnimator {

    val endState: GameViewState

    fun animate(fraction: Float): GameViewState
}