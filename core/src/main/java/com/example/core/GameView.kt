package com.example.core

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.GameViewUtils.drawNode
import com.example.engine2.*
import com.ilyin.ui_core_compose.colors.MdColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.*

@Composable
fun GameView(gameState: GameState) {

    val coroutine: CoroutineScope = rememberCoroutineScope()

    var gameStateState: GameState by remember { mutableStateOf(gameState) }
    var angleStep by remember { mutableStateOf(360f / gameStateState.nodes.size) }
    angleStep = 360f / gameStateState.nodes.size

    BoxWithConstraints() {

        val density = LocalDensity.current
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }
        val outerCircleRadius = widthPx / 2 * 0.9f
        val wCenter = widthPx / 2
        val hCenter = heightPx / 2
        val center = Offset(wCenter, hCenter)
        val hLiveCircle = heightPx / 5
        val nodeRadius = outerCircleRadius / 8


        var nodeRadiusA by remember { mutableStateOf(nodeRadius) }
        val scaleAnimatable by animateFloatAsState(targetValue = nodeRadiusA)

        var offset by remember { mutableStateOf(center) }


        val distance = outerCircleRadius - 1.5f * nodeRadius
//        val angleStep = 360f / gameStateState.nodes.size
        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
//                    val angleStep = 360f / gameStateState.nodes.size
                    val d = it - center
                    val aRad = atan2(y = d.y, x = d.x)
                    var a = (aRad * 180 / PI)// + 180
                    if (a < 0) a += 360
                    val indexLeftNode = (a / angleStep).toInt()
                    //  анимация для активной ноды
                    if (isActiveNodeClick(
                            clickPoint = it,
                            centerFull = center,
                            nodeRadius = nodeRadius
                        )
                    ) {
                        if (gameStateState.onActiveNodeClick()) {
                            coroutine.launch {
                                nodeRadiusA *= 1.5f
                                delay(300)
                                nodeRadiusA /= 1.5f
                            }
                        }
                    } else {
                        // каждый раз при клике на поле или ноду
                        val clickNode = findClickNode(
                            gameState = gameStateState,
                            clickPoint = it,
                            angleStep = angleStep,
                            centerFull = center,
                            distance = distance,
                            nodeRadius = nodeRadius
                        )
                        coroutine.launch {
                            // сдвинулись все потому что произошел перерассчет их центра
                            //offset = centerTest
                        }

                        gameStateState.dispatchActiveNodeAt(indexLeftNode, clickNode)
                    }

                    val newState = gameStateState.clone()
                    gameStateState = newState
                }

            }, onDraw = {
            if (gameStateState.nodes.size == 17) {
                this.drawCircle(
                    color = MdColors.green.c400,
                    radius = 15f,
                    center = Offset(x = center.x - 50f, y = hLiveCircle),
                )
                this.drawCircle(
                    color = MdColors.green.c400,
                    radius = 15f,
                    center = Offset(x = center.x, y = hLiveCircle),
                )
                this.drawCircle(
                    color = MdColors.green.c400,
                    radius = 15f,
                    center = Offset(x = center.x + 50f, y = hLiveCircle),
                )
            }
            if (gameStateState.nodes.size == 18) {
                this.drawCircle(
                    color = MdColors.yellow.c400,
                    radius = 15f,
                    center = Offset(x = center.x - 25f, y = hLiveCircle),
                )
                this.drawCircle(
                    color = MdColors.yellow.c400,
                    radius = 15f,
                    center = Offset(x = center.x + 25f, y = hLiveCircle),
                )
            }
            if (gameStateState.nodes.size == 19) {
                this.drawCircle(
                    color = MdColors.red.c400,
                    radius = 15f,
                    center = Offset(x = center.x, y = hLiveCircle),
                )

            }
            // отрисовка поля
            this.drawCircle(
                color = Color.Black,
                radius = outerCircleRadius,
                center = center,
                style = Stroke()
            )
            drawNode(
                node = gameStateState.activeNode,
                circleRadius = scaleAnimatable,
                center = center,
            )
            // отрисовка нод на поле выполняется каждый раз когда кидается нода
            gameStateState.nodes.forEachIndexed { index, node ->
                drawNode(
                    node = node,
                    circleRadius = nodeRadius,
                    center = computeNodeCenter(
                        index = index,
                        angleStep = angleStep,
                        centerFull = offset, //Offset(offsetX,offsetY),//center,
                        distance = distance
                    ),
                )
            }
        })
    }
}

fun computeNodeCenter(index: Int, angleStep: Float, centerFull: Offset, distance: Float): Offset {
    val angle = index * angleStep
    val angleNodeRad = (angle / 180 * PI).toFloat()
    return centerFull + Offset(
        x = distance * cos(angleNodeRad),
        y = distance * sin(angleNodeRad)
    )
}

fun isActiveNodeClick(
    clickPoint: Offset, centerFull: Offset,
    nodeRadius: Float,
): Boolean {
    val range = (centerFull - clickPoint).getDistance().absoluteValue
    return range < nodeRadius
}

fun isNodeClicked(
    gameState: GameState, clickPoint: Offset,
    angleStep: Float,
    centerFull: Offset,
    distance: Float,
    nodeRadius: Float, node: Node
): Boolean {
    val index = gameState.nodes.indexOf(node)
    val center = computeNodeCenter(
        index = index,
        angleStep = angleStep,
        centerFull = centerFull,
        distance = distance
    )
    val range = (center - clickPoint).getDistance().absoluteValue
    return range < nodeRadius
}

fun findClickNode(
    gameState: GameState,
    clickPoint: Offset,
    angleStep: Float,
    centerFull: Offset,
    distance: Float,
    nodeRadius: Float
): Node? {
    return gameState.nodes.find { n ->
        isNodeClicked(
            gameState = gameState,
            clickPoint = clickPoint,
            angleStep = angleStep,
            centerFull = centerFull,
            distance = distance,
            nodeRadius = nodeRadius,
            node = n
        )
    }
}

@Preview(device = Devices.NEXUS_5)
@Composable
fun GameViewPreview() {
    GameView(
        gameState = GameState(
            nodes = mutableListOf(
                NodeElement(element = Element(2), 1),
                NodeElement(element = Element(2), 2),
                NodeElement(element = Element(4), 3),
                NodeElement(element = Element(4), 4),
                NodeElement(element = Element(4), 5),
                NodeElement(element = Element(4), 6),
                NodeElement(element = Element(3), 7),
                NodeAction(action = Action.PLUS, 8)
            ),
            initialActiveNode = NodeElement(
                element = Element(3),
                9
            )//NodeAction(action = Action.MINUS)
        )
    )
}