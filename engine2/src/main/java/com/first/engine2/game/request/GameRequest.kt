package com.first.engine2.game.request

import com.first.engine2.game.state.GameState

/**
 * Возможные действия, которые можно выполнить над [GameState]
 */
sealed class GameRequest {

    /**
     * Отправить активную ноду в круг обычных нод.
     */
    class DispatchNode constructor(val leftNodeId: Int?): GameRequest()

    /**
     * Забрать из круг ноду, чтобы сделать ее активной. Активная нода заменяет собой минус.
     * @param nodeId id ноды, которую нужно забрать из круга
     */
    class ExtractWithMinus constructor(val nodeId: Int) : GameRequest()

    /**
    * Скопировать ноду из круга и сделать её активной. Нода заменяет сферу.
    * */
    class CopyWithSphere constructor(val nodeId: Int) : GameRequest()

    /**
     * Заменить активную ноду на антиматерию.
     * */
    object TurnToAntimatter : GameRequest()

    /**
     * Превратить минус в плюс
     */
    object TurnMinusToPlus : GameRequest()


    object DoNothing : GameRequest()
}