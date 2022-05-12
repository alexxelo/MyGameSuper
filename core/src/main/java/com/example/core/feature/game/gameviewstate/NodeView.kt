package com.example.core.feature.game.gameviewstate

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import com.example.core.GameViewUtils.nodeContentColor
import com.example.engine2.game.Action
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

sealed interface NodeView {
    val id: Int
    val angle: Float
    val distancePx: Float
    val radiusPx: Float
    val bgColor: Color

    val centerOffset: Offset
        get() {
            val angleNodeRad = (angle / 180 * PI).toFloat()
            return Offset(
                x = distancePx * cos(angleNodeRad),
                y = distancePx * sin(angleNodeRad),
            )
        }

    val isActive: Boolean
        get() = centerOffset == Offset.Zero

    fun center(gameCenter: Offset): Offset {
        return gameCenter + centerOffset
    }

    fun drawBg(center: Offset, drawScope: DrawScope) {
        drawScope.drawCircle(
            color = bgColor,
            radius = radiusPx,
            center = center
        )
    }

    fun drawContent(center: Offset, drawScope: DrawScope)

    fun draw(gameCenter: Offset, drawScope: DrawScope) {
        val center = center(gameCenter)
        drawBg(center, drawScope)
        drawContent(center, drawScope)
    }
}

data class NodeElementView constructor(
    override val id: Int,
    override val angle: Float,
    override val distancePx: Float,
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

    override fun drawContent(center: Offset, drawScope: DrawScope) {
        val massWidth = paint.measureText(atomicMass)
        val nameWidth = paint.measureText(name)

        drawScope.drawIntoCanvas {
            it.nativeCanvas.drawText(
                name,
                center.x - nameWidth / 2,
                center.y + paint.textSize / 3,
                paint
            )
        }
        drawScope.drawIntoCanvas {
            it.nativeCanvas.drawText(
                atomicMass,
                center.x - massWidth / 4,
                center.y + paintAM.textSize * 2,
                paintAM
            )
        }
    }

}

data class NodeActionView constructor(
    override val id: Int,
    override val angle: Float,
    override val distancePx: Float,
    override val radiusPx: Float,
    override val bgColor: Color,
    val type: Action,
) : NodeView {

    override fun drawContent(center: Offset, drawScope: DrawScope) {
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