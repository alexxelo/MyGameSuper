package com.example.engine2.game.state.dynamic

import com.example.engine2.game.result.RequestResultPart
import com.example.engine2.game.state.GameState

class GameStateDynamic constructor(val steps: List<Step>) {

    class Step constructor(val state1: GameState, val resultPart: RequestResultPart, val state2: GameState)

    companion object {

        fun from(initial: GameState, requestResultsAndStates: List<Pair<RequestResultPart, GameState>>): GameStateDynamic {
            if (requestResultsAndStates.isEmpty()) throw RuntimeException("Request results must not be empty.")
            val steps: MutableList<Step> = mutableListOf()
            var state1 = initial
            requestResultsAndStates.forEach {
                val resultPart = it.first
                val state2 = it.second
                steps.add(
                    Step(
                        state1 = state1,
                        resultPart = resultPart,
                        state2 = state2,
                    )
                )
                state1 = state2
            }
            return GameStateDynamic(
                steps = steps
            )
        }
    }
}