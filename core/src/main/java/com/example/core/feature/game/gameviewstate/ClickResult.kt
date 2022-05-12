package com.example.core.feature.game.gameviewstate

/**
 * Информация о клике по игровому полю.
 * @param leftNodeIndex индекс ноды, чей угол находится левее угла, под которым произошел клик.
 * @param clickedNodeId ноды, которой игрок коснулся при клике. Может быть null, если клик был не по ноде, а по пустому пространству.
 */
data class ClickResult constructor(
    val angle: Float,
    val leftNodeIndex: Int,
    val clickedNodeId: Int? = null
)