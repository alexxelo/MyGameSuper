package com.first.core.feature.game.gameviewstate.fabric

import com.first.core.feature.game.gameviewstate.GameViewState
import com.first.core.feature.game.gameviewstate.GameViewStateDimensions
import com.first.engine2.game.state.GameState

interface GameViewStateFabric {

    fun createFrom(
        gameState: GameState,
        dimens: GameViewStateDimensions,
    ): GameViewState
}