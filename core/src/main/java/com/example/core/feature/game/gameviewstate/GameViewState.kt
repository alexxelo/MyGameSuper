package com.example.core.feature.game.gameviewstate

import android.graphics.Paint
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import com.example.core.BuildConfig
import com.example.core.feature.game.gameviewstate.fabric.GameViewStateFabric
import com.example.core.feature.game.gameviewstate.fabric.GameViewStateFabricImpl
import com.example.engine2.node.NodeElement
import kotlin.math.*

data class GameViewState constructor(
  val dimens: GameViewStateDimensions,
  val nodesView: List<NodeView>,
) {

  val activeNode: NodeView
    get() = nodesView.find { it.centerOffset == Offset.Zero }!!

  val radialNodes: List<NodeView>
    get() = nodesView.filterNot { it.centerOffset == Offset.Zero }


  fun draw(drawScope: DrawScope) {
    drawBg(drawScope)
    nodesView.forEach {
      it.draw(dimens.center, drawScope)
    }
    if (BuildConfig.DEBUG) {
      drawStartAngle(drawScope)
    }
  }


  private fun drawStartAngle(drawScope: DrawScope) {
    val distance = dimens.outerCircleRadiusPx
    val angle = dimens.startAngle
    val angleRad = (angle / 180 * PI).toFloat()
    val center = dimens.center + Offset(
      x = distance * cos(angleRad),
      y = distance * sin(angleRad),
    )

    drawScope.drawCircle(
      color = Color.Blue,
      radius = 10f,
      center = center
    )
    val p = Paint().apply {
      this.textSize = 30f
      color = android.graphics.Color.BLACK
    }
    val txt = "${angle.toInt()}"
    val textWidth = p.measureText(txt)

    drawScope.drawIntoCanvas {
      it.nativeCanvas.drawText(txt, center.x - textWidth / 2, center.y - p.textSize / 2 + 30, p)
    }
  }

  fun drawArc(drawScope: DrawScope, gameViewStateAnimated: GameViewState, lastPattern: Pair<NodeElement, NodeElement>?) {

    val sizeArc = drawScope.size / 1.112f
    val firstElement = gameViewStateAnimated.nodesView.find { it.id == lastPattern?.first?.id }
    val secondElement = gameViewStateAnimated.nodesView.find { it.id == lastPattern?.second?.id }

    var firstAngle = firstElement?.angle
    var secondAngle = secondElement?.angle
    //

    if (secondAngle != null && firstAngle != null) {
      var arcLength: Float = secondAngle - firstAngle
      if ((secondAngle - firstAngle) < 0) {
        arcLength = 360 + (secondAngle - firstAngle)
      }
      drawScope.drawArc(
        color = Color.Green,
        startAngle = firstAngle,
        sweepAngle = arcLength,
        useCenter = false,
        topLeft = Offset((drawScope.size.width - sizeArc.width) / 2f, (drawScope.size.height - sizeArc.height) / 2f),// центр
        size = sizeArc,
        style = Stroke(6f)
      )

    }
  }


  private fun drawBg(drawScope: DrawScope) {
    drawScope.drawCircle(
      color = Color.Black,
      radius = dimens.outerCircleRadiusPx,
      center = dimens.center,
      style = Stroke()
    )
  }

  fun click(clickPoint: Offset): ClickResult {
    val clickAngle: Float = clickAngle(clickPoint)
    Log.d("clickAngle", "$clickAngle")
    val leftNode: NodeView? = leftNodeFrom(clickAngle)
    val clickedNode: NodeView? = findClickNode(clickPoint)
    return ClickResult(
      angle = clickAngle,
      leftRadialNodeId = leftNode?.id,
      clickedNodeId = clickedNode?.id,
    )
  }

  // ??
  fun leftNodeFrom(angle: Float): NodeView? {
    val radialNodes = radialNodes
    return if (radialNodes.isEmpty()) {
      null
    } else {
      radialNodes.sortedBy(NodeView::angle).asReversed().find { it.angle < angle }
        ?: radialNodes.maxByOrNull(NodeView::angle)
        ?: throw RuntimeException("angle ${angle.toInt()} nodes = ${radialNodes.map(NodeView::angle)}")
    }
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