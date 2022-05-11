package com.example.core.feature.game.gameviewstate

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import com.example.core.GameViewUtils.nodeContentColor
import com.example.engine2.Action

interface NodeView {
    val id: Int
    val center: Offset
    val radiusPx: Float
    val bgColor: Color

    fun drawBg(drawScope: DrawScope) {
        drawScope.drawCircle(
            color = bgColor,
            radius = radiusPx,
            center = center
        )
    }

    fun drawContent(drawScope: DrawScope)

    fun draw(drawScope: DrawScope) {
        drawBg(drawScope)
        drawContent(drawScope)
    }
}

data class NodeElementView constructor(
    override val id: Int,
    override val center: Offset,
    override val radiusPx: Float,
    override val bgColor: Color,
    val name: String,
    val atomicMass: String,
) : NodeView {

    val textColor = nodeContentColor(bgColor)

    private val paint = Paint().apply {
        this.textSize = 50f
        color = textColor
    }
    private val paintAM = Paint().apply {
        this.textSize = 30f
        color = textColor
    }

    override fun drawContent(drawScope: DrawScope) {
        val numWidth = paint.measureText(atomicMass)
        val textWidth = paint.measureText(name)

        drawScope.drawIntoCanvas {
            it.nativeCanvas.drawText(
                name,
                center.x - textWidth / 2,
                center.y + paint.textSize / 3,
                paint
            )
        }
        drawScope.drawIntoCanvas {
            it.nativeCanvas.drawText(
                atomicMass,
                center.x - numWidth / 4,
                center.y + paintAM.textSize * 2,
                paintAM
            )
        }
    }

}

data class NodeActionView constructor(
    override val id: Int,
    override val center: Offset,
    override val radiusPx: Float,
    override val bgColor: Color,
    val type: Action,
) : NodeView {

    override fun drawContent(drawScope: DrawScope) {
        val halfRadius = radiusPx / 2
        val strokeWidth = radiusPx / 5

        drawScope.drawLine(
            color = Color.Black,
            start = center - Offset(x = halfRadius, y = 0f),
            end = center + Offset(x = halfRadius, y = 0f),
            strokeWidth = strokeWidth
        )
        if (type == Action.PLUS) {
            drawScope.drawLine(
                color = Color.Black,
                start = center - Offset(x = 0f, y = halfRadius),
                end = center + Offset(x = 0f, y = halfRadius),
                strokeWidth = strokeWidth
            )
        }
    }
}