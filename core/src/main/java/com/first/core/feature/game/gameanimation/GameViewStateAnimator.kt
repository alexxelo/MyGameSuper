package com.first.core.feature.game.gameanimation

import com.first.core.feature.game.gameviewstate.GameViewState

interface GameViewStateAnimator {

    val endState: GameViewState

    fun animate(fraction: Float): GameViewState
}