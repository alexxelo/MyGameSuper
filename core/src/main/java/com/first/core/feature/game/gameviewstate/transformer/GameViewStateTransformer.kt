package com.first.core.feature.game.gameviewstate.transformer

import com.first.core.feature.game.gameviewstate.GameViewState
import com.first.engine2.game.result.RequestResultPart
import com.first.engine2.game.state.GameState

interface GameViewStateTransformer {

    fun transform(
        initialViewState: GameViewState,
        requestResultPart: RequestResultPart,
        resultGameState: GameState,
    ): GameViewState
}