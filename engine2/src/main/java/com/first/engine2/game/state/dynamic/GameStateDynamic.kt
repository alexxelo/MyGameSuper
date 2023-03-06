package com.first.engine2.game.state.dynamic

import com.first.engine2.game.result.RequestResultPart
import com.first.engine2.game.state.GameState

/**
 * Структура, описывающая изменение состояния игры [GameState] в динамике.
 * Представляет из себя последовательный список шагов (см. [Step]).
 */
class GameStateDynamic constructor(val steps: List<Step>) {

    /**
     * Шаг, описывающий единичное изменение состояния игры
     * @param state1 начальное состояние
     * @param state2 конечное состояние
     * @param requestPart - действие, которое привело игровое состояния от [state1] к [state2]
     *
     * [state1] текущего шага это [state2] предыдущего шага. Пример:
     *
     * шаг1 = { состояние1 —> запрос —> состояние2 }
     *
     * шаг2 = { состояние2 —> запрос —> состояние3 }
     */
    data class Step constructor(val state1: GameState, val requestPart: RequestResultPart, val state2: GameState)

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
                        requestPart = resultPart,
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