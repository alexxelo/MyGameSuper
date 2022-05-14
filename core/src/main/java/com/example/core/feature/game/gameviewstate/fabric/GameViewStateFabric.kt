package com.example.core.feature.game.gameviewstate.fabric

import com.example.core.feature.game.gameviewstate.GameViewState
import com.example.core.feature.game.gameviewstate.GameViewStateDimensions
import com.example.engine2.game.state.GameState

interface GameViewStateFabric {

    fun createFrom(
        gameState: GameState,
        dimens: GameViewStateDimensions,
    ): GameViewState
}