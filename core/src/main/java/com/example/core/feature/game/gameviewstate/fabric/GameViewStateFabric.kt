package com.example.core.feature.game.gameviewstate.fabric

import com.example.core.feature.game.gameviewstate.GameViewState
import com.example.engine2.GameState

interface GameViewStateFabric {

    fun createFrom(gameState: GameState, widthPx: Float, heightPx: Float): GameViewState
}