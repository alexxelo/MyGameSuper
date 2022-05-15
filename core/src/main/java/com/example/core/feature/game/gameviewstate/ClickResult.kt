package com.example.core.feature.game.gameviewstate

/**
 * Информация о клике по игровому полю.
 * @param leftRadialNodeId id ноды, которая находится на круге слева (против часовой стрелки) от точки клика.
 * @param clickedNodeId id ноды, которой игрок коснулся при клике. Может быть null, если клик был не по ноде, а по пустому пространству.
 */
data class ClickResult constructor(
    val angle: Float,
    val leftRadialNodeId: Int?,
    val clickedNodeId: Int? = null
)