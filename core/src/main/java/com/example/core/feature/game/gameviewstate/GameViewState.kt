package com.example.core.feature.game.gameviewstate

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.core.feature.game.gameviewstate.fabric.GameViewStateFabric
import com.example.core.feature.game.gameviewstate.fabric.GameViewStateFabricImpl
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.atan2

data class GameViewState constructor(
    val dimens: GameViewStateDimensions,
    val nodesView: List<NodeView>,
) {

    fun draw(drawScope: DrawScope) {
        drawBg(drawScope)
        nodesView.forEach {
            it.draw(dimens.center, drawScope)
        }
    }

    private fun drawBg(drawScope: DrawScope) {
        // отрисовка поля
        drawScope.drawCircle(
            color = Color.Black,
            radius = dimens.outerCircleRadiusPx,
            center = dimens.center,
            style = Stroke()
        )
    }

    fun click(clickPoint: Offset): ClickResult {
        val angle: Float = clickAngle(clickPoint)
        val indexLeftNode: Int = (angle / dimens.angleStep).toInt()
        val clickedNode: NodeView? = findClickNode(clickPoint)
        return ClickResult(
            leftNodeIndex = indexLeftNode,
            clickedNodeId = clickedNode?.id,
        )
    }

    private fun clickAngle(clickPoint: Offset): Float {
        val d = clickPoint - dimens.center
        val aRad = atan2(y = d.y, x = d.x)
        var a = (aRad * 180 / PI).toFloat()
        if (a < 0) {
            a += 360
        }
        return a
    }

    private fun findClickNode(clickPoint: Offset): NodeView? {
        return nodesView.find { nodeView ->
            isNodeClicked(nodeView, clickPoint)
        }
    }

    private fun isNodeClicked(nodeView: NodeView, clickPoint: Offset): Boolean {
        val range = (nodeView.center(dimens.center) - clickPoint).getDistance().absoluteValue
        return range < nodeView.radiusPx
    }

    companion object : GameViewStateFabric by GameViewStateFabricImpl()
}