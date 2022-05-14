package com.example.core.feature.game.gameviewstate.transformer

import com.example.core.feature.game.gameviewstate.GameViewState
import com.example.engine2.game.result.RequestResultPart
import com.example.engine2.game.state.GameState

interface GameViewStateTransformer {

    fun transform(
        initialViewState: GameViewState,
        requestResultPart: RequestResultPart,
        resultGameState: GameState,
    ): GameViewState
}