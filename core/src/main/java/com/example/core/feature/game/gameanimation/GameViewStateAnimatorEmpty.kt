package com.example.core.feature.game.gameanimation

import com.example.core.feature.game.gameviewstate.*

class GameViewStateAnimatorEmpty constructor(private val state: GameViewState): GameViewStateAnimator {

    override fun animate(fraction: Float): GameViewState {
        return state
    }

    override val endState: GameViewState
        get() = state
}