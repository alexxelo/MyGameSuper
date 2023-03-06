package com.first.core.feature.game.gameviewstate

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import com.first.core.BuildConfig
import com.first.core.GameViewUtils.nodeContentColor
import com.first.engine2.game.Action
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
      val distance = distancePx
      val angleNodeRad = (angle / 180 * PI).toFloat()
      return Offset(
        x = distance * cos(angleNodeRad),
        y = distance * sin(angleNodeRad),
      )
    }
/*
  private val centerDebugOffset: Offset
    get() {
      val distance = distancePx * 0.8f
      val angleNodeRad = (angle / 180 * PI).toFloat()
      return Offset(
        x = distance * cos(angleNodeRad),
        y = distance * sin(angleNodeRad),
      )
    }*/

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

  //iterator number for elements and angle
/*

  private fun drawDebug(center: Offset, drawScope: DrawScope) {
    val debugPaint = Paint().apply {
      this.textSize = 30f
      color = android.graphics.Color.BLACK
    }
    val debugText = "$id — ${angle.toInt()}"

    val nameWidth = debugPaint.measureText(debugText)

    drawScope.drawIntoCanvas {
      it.nativeCanvas.drawText(
        debugText,
        center.x - nameWidth / 2,
        center.y + debugPaint.textSize / 2,
        debugPaint
      )
    }
  }
*/

  fun DrawContent(center: Offset, drawScope: DrawScope)


  fun draw(gameCenter: Offset, drawScope: DrawScope) {
    val center = center(gameCenter)
    drawBg(center, drawScope)
    DrawContent(center, drawScope)


    if (BuildConfig.DEBUG) {
      //val debugCenter = gameCenter + centerDebugOffset
      //drawDebug(debugCenter, drawScope)
    }

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

  val textColor: Int = nodeContentColor(bgColor)

  private val paint = Paint().apply {
    this.textSize = 50f
    color = textColor
  }
  private val paintAM = Paint().apply {
    this.textSize = 30f
    color = textColor
  }

  override fun DrawContent(center: Offset, drawScope: DrawScope) {
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

  override fun DrawContent(center: Offset, drawScope: DrawScope) {
    val halfRadius = radiusPx / 6
    val strokeWidth = radiusPx / 15

    if (type == Action.MINUS) {
      drawScope.drawLine(
        color = Color.White,
        start = center - Offset(x = halfRadius, y = 0f),
        end = center + Offset(x = halfRadius, y = 0f),
        strokeWidth = strokeWidth
      )
    }
    if (type == Action.PLUS) {
      drawScope.drawLine(
        color = Color.White,
        start = center - Offset(x = halfRadius, y = 0f),
        end = center + Offset(x = halfRadius, y = 0f),
        strokeWidth = strokeWidth
      )
      drawScope.drawLine(
        color = Color.White,
        start = center - Offset(x = 0f, y = halfRadius),
        end = center + Offset(x = 0f, y = halfRadius),
        strokeWidth = strokeWidth
      )
    }
    if (type == Action.BLACK_PLUS){
      drawScope.drawLine(
        color = Color.White,
        start = center - Offset(x = halfRadius, y = 0f),
        end = center + Offset(x = halfRadius, y = 0f),
        strokeWidth = strokeWidth
      )
      drawScope.drawLine(
        color = Color.White,
        start = center - Offset(x = 0f, y = halfRadius),
        end = center + Offset(x = 0f, y = halfRadius),
        strokeWidth = strokeWidth
      )
    }

  }
}