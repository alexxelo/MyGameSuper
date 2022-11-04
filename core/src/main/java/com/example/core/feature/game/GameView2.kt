package com.example.core.feature.game

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.feature.game.gameanimation.GameViewStateAnimator
import com.example.core.feature.game.gameanimation.GameViewStateAnimatorEmpty
import com.example.core.feature.game.gameanimation.GameViewStateAnimatorGeneral
import com.example.core.feature.game.gameanimation.GameViewStateAnimatorMerge
import com.example.core.feature.game.gamerequest.GameRequestComputerImpl
import com.example.core.feature.game.gameviewstate.ClickResult
import com.example.core.feature.game.gameviewstate.GameViewState
import com.example.core.feature.game.gameviewstate.GameViewStateDimensions
import com.example.core.feature.game.gameviewstate.transformer.GameViewStateTransformerImpl
import com.example.engine2.game.Action
import com.example.engine2.game.result.RequestResultPart
import com.example.engine2.game.state.GameState
import com.example.engine2.game.state.dynamic.GameStateDynamic
import com.example.engine2.node.NodeAction
import com.example.engine2.node.NodeElement
import com.ilyin.ui_core_compose.colors.MdColors

@Composable
fun GameView2(
  modifier: Modifier = Modifier,
  gameState: GameState,
  onGameStateChanged: (GameState) -> Unit = {},
) {
  var gameStateState: GameState by remember { mutableStateOf(gameState) }
  val lastPattern: Pair<NodeElement, NodeElement>? = gameState.bestPattern?.last()


  BoxWithConstraints(modifier = modifier) {

    val density = LocalDensity.current
    val widthPx = with(density) { maxWidth.toPx() }
    val heightPx = with(density) { maxHeight.toPx() }

    val dimens = GameViewStateDimensions.createFrom(gameState, widthPx, heightPx)
    val initialViewState = GameViewState.createFrom(gameStateState, dimens)
    var animators: List<GameViewStateAnimator> by remember { mutableStateOf(listOf(GameViewStateAnimatorEmpty(initialViewState))) }

    var gameAnimator: GameViewStateAnimator by remember { mutableStateOf(animators.first()) }

    var fractionStart by remember { mutableStateOf(0f) }
    var fractionEnd by remember { mutableStateOf(1f) }
    val fractionAnimatable by animateFloatAsState(
      targetValue = fractionEnd,
      animationSpec = tween(durationMillis = 225),
      finishedListener = {
        if (animators.size > 1) {
          animators = animators.drop(1)
          fractionStart++
          fractionEnd++
          gameAnimator = animators.first()
        }
      }
    )

    val gameViewStateAnimated: GameViewState = gameAnimator.animate(
      fraction = fractionAnimatable - fractionStart,
    )

    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
          detectTapGestures { clickPoint ->
            val animationIsNotComplete = fractionAnimatable < fractionEnd
            if (animationIsNotComplete) return@detectTapGestures

            // Получить информацию о клике
            val clickResult: ClickResult = animators.last().endState.click(clickPoint)
            // На основе клика узнать, какое действие нужно выполнить
            val gameRequest = GameRequestComputerImpl().compute(clickResult, gameStateState)
            // На основе действия изменить состояние игры, получив все промежуточные состояния и запросы на изменение состояния
            val initialGameState = gameStateState.clone()
            val gameRequestResult = gameStateState.executeRequest(gameRequest)
            // Сгруппировать промежуточне состояния в динамическое состояние
            val dynamicState = GameStateDynamic.from(initialGameState, gameRequestResult)

            val newAnimators: List<GameViewStateAnimator> = computeNewAnimators(dynamicState, gameAnimator)


            val newState = dynamicState.steps.last().state2
            onGameStateChanged(newState)

            gameStateState = newState
            fractionStart++
            fractionEnd++
            animators = newAnimators
            gameAnimator = newAnimators.first()
          }

        },
      onDraw = {
        gameViewStateAnimated.draw(this, gameStateState)
        gameViewStateAnimated.drawArc(this, gameViewStateAnimated, lastPattern, gameStateState)

      },
    )
    AnimateMinus(gameState = gameStateState, gameViewStateAnimated = gameViewStateAnimated)
    AnimatePlusSimple(gameState = gameStateState, gameViewStateAnimated = gameViewStateAnimated)
    //AnimatePlusNode(gameStateState, gameViewStateAnimated)


  }
}


@Composable
fun DrawCircle(circleRadius: Float, offsetX: Float, offsetY: Float, color: Color) {
  Canvas(modifier = Modifier) {
    drawCircle(
      color = color,
      center = Offset(offsetX, offsetY),
      radius = circleRadius,
      alpha = 0.3f
    )
  }
}

@Composable
fun AnimateMinus(gameState: GameState, gameViewStateAnimated: GameViewState) {
  val infiniteTransition = rememberInfiniteTransition()

  val dxMinus by infiniteTransition.animateFloat(
    initialValue = 130f,
    targetValue = 0f,
    animationSpec = infiniteRepeatable(
      animation = tween(3000, easing = LinearEasing),
    )
  )

  val colorAnim = rememberInfiniteTransition()
  val dcMinus by colorAnim.animateColor(
    initialValue = Color.White,
    targetValue = MdColors.blue.c600,
    animationSpec = infiniteRepeatable(
      animation = tween(3000, easing = LinearOutSlowInEasing),
    )
  )
  val node = gameState.activeNode
  if (node is NodeAction) {
    if (node.action == Action.MINUS) {

      DrawCircle(
        circleRadius = dxMinus,
        offsetX = gameViewStateAnimated.dimens.center.x,
        offsetY = gameViewStateAnimated.dimens.center.y,
        color = dcMinus
      )
    }
  }
}

@Composable
fun AnimatePlusSimple(gameState: GameState, gameViewStateAnimated: GameViewState) {
  val infiniteTransition = rememberInfiniteTransition()

  val dxPlus by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 130f,
    animationSpec = infiniteRepeatable(
      animation = tween(3000, 200, easing = LinearEasing),
    )
  )

  val colorAnim = rememberInfiniteTransition()
  val dcPlus by colorAnim.animateColor(
    initialValue = MdColors.red.c600,
    targetValue = Color.White,
    animationSpec = infiniteRepeatable(
      animation = tween(1000,2200, easing = LinearEasing),
    )
  )
  val node = gameState.activeNode
  if (node is NodeAction) {

    if (node.action == Action.PLUS) {
      DrawCircle(
        circleRadius = dxPlus,
        offsetX = gameViewStateAnimated.dimens.center.x,
        offsetY = gameViewStateAnimated.dimens.center.y,
        color = dcPlus
      )
    }

    if (gameState.allPluses.isNotEmpty()) {
      gameState.allPluses.forEach { nodePlus ->
        val offset = gameViewStateAnimated.nodesView.find { it.id == nodePlus.id }!!.centerOffset + gameViewStateAnimated.dimens.center
        DrawCircle(
          circleRadius = dxPlus,
          offsetX = offset.x,
          offsetY = offset.y,
          color = dcPlus
        )
      }
    }
  }
}

/*

@Composable
fun AnimatePlus(
  gameState: GameState,
  gameViewStateAnimated: GameViewState,
  animationDelay: Int = 2000,
  circleColor: Color = Color.Red,

  ) {

  // 3 circles
  val circles = listOf(
    remember {
      Animatable(initialValue = 0f);
    },
    remember {
      Animatable(initialValue = 0f)
    },
    remember {
      Animatable(initialValue = 0f)
    }
  )
  val colorC = remember {
    Animatable(initialValue = 0f);
  }
  val colorAnim = rememberInfiniteTransition()
  val dcPlus by colorAnim.animateColor(
    initialValue = MdColors.red.c400,
    targetValue = Color.White,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, 1000, easing = LinearEasing)
    )
  )
  circles.forEachIndexed { index, animatable ->


    LaunchedEffect(Unit) {
      // Use coroutine delay to sync animations
      // divide the animation delay by number of circles
      delay(timeMillis = (animationDelay / 3L) * (index + 1))

      animatable.animateTo(
        targetValue = 130f,
        animationSpec = infiniteRepeatable(
          animation = tween(
            durationMillis = animationDelay,
            easing = LinearEasing
          ),
          repeatMode = RepeatMode.Restart
        )
      )
    }
  }

  circles.forEachIndexed { _, animatable ->


    val node = gameState.activeNode
    if (node is NodeAction) {

      if (node.action == Action.PLUS) {
        DrawCircle(
          circleRadius = animatable.value,
          offsetX = gameViewStateAnimated.dimens.center.x,
          offsetY = gameViewStateAnimated.dimens.center.y,
          color = circleColor.copy(alpha = (1 - colorC.value))
        )
      }
      if (gameState.allPluses.isNotEmpty()) {
        gameState.allPluses.forEach { nodePlus ->
          val offset = gameViewStateAnimated.nodesView.find { it.id == nodePlus.id }!!.centerOffset + gameViewStateAnimated.dimens.center
          DrawCircle(
            circleRadius = animatable.value,
            offsetX = offset.x,
            offsetY = offset.y,
            color = circleColor.copy(alpha = (1 - colorC.value))
          )
        }
      }
    }
  }
}

@Composable
fun AnimatePlusNode(gameState: GameState, gameViewStateAnimated: GameViewState) {

  val infiniteTransition = rememberInfiniteTransition()

  val dxPlus by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 130f,
    animationSpec = infiniteRepeatable(
      animation = tween(2500, easing = FastOutSlowInEasing)
    )
  )
  val dxPlus2 by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 130f,
    animationSpec = infiniteRepeatable(
      animation = tween(2500, 700, easing = FastOutSlowInEasing)
    )
  )
  val dxPlus3 by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 130f,
    animationSpec = infiniteRepeatable(
      animation = tween(2500, 1777, easing = LinearEasing)
    )
  )
  val colorAnim = rememberInfiniteTransition()
  val dcPlus by colorAnim.animateColor(
    initialValue = MdColors.red.c400,
    targetValue = Color.White,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, 1500, easing = LinearEasing)
    )
  )
  val dcPlus2 by colorAnim.animateColor(
    initialValue = MdColors.red.c400,
    targetValue = Color.White,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, 2200, easing = LinearEasing)
    )
  )
  val dcPlus3 by colorAnim.animateColor(
    initialValue = MdColors.red.c400,
    targetValue = Color.White,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, 3777, easing = LinearEasing)
    )
  )

  val node = gameState.activeNode
  if (node is NodeAction) {

    if (node.action == Action.PLUS) {
      DrawCircle(
        circleRadius = dxPlus,
        offsetX = gameViewStateAnimated.dimens.center.x,
        offsetY = gameViewStateAnimated.dimens.center.y,
        color = dcPlus
      )
      DrawCircle(
        circleRadius = dxPlus2,
        offsetX = gameViewStateAnimated.dimens.center.x,
        offsetY = gameViewStateAnimated.dimens.center.y,
        color = dcPlus2
      )

      DrawCircle(
        circleRadius = dxPlus3,
        offsetX = gameViewStateAnimated.dimens.center.x,
        offsetY = gameViewStateAnimated.dimens.center.y,
        color = dcPlus3
      )


    }


    if (gameState.allPluses.isNotEmpty()) {
      gameState.allPluses.forEach { nodePlus ->
        val offset = gameViewStateAnimated.nodesView.find { it.id == nodePlus.id }!!.centerOffset + gameViewStateAnimated.dimens.center
        DrawCircle(
          circleRadius = dxPlus,
          offsetX = offset.x,
          offsetY = offset.y,
          color = dcPlus
        )
        DrawCircle(
          circleRadius = dxPlus2,
          offsetX = offset.x,
          offsetY = offset.y,
          color = dcPlus2
        )

        DrawCircle(
          circleRadius = dxPlus3,
          offsetX = gameViewStateAnimated.dimens.center.x,
          offsetY = gameViewStateAnimated.dimens.center.y,
          color = dcPlus3
        )

      }
    }

  }
}
*/

private fun computeNewAnimators(dynamicState: GameStateDynamic, lastAnimator: GameViewStateAnimator): List<GameViewStateAnimator> {
  val viewStateTransformer = GameViewStateTransformerImpl()
  val newAnimators: MutableList<GameViewStateAnimator> = arrayListOf()
  var prevViewState = GameViewState.createFrom(
    gameState = dynamicState.steps.first().state1,
    lastAnimator.endState.dimens,
  )

  dynamicState.steps.forEach { step ->
    val resultPart = step.requestPart
    val nextViewState = viewStateTransformer.transform(prevViewState, resultPart, step.state2)
    val animator = when (resultPart) {
      is RequestResultPart.Merge -> GameViewStateAnimatorMerge(prevViewState, resultPart, nextViewState)
      else -> GameViewStateAnimatorGeneral(prevViewState, nextViewState)
    }
    newAnimators.add(animator)
    prevViewState = nextViewState
  }
  return newAnimators
}

@Preview(device = Devices.NEXUS_5)
@Composable
fun GameViewPreview(modifier: Modifier = Modifier) {
  /*GameView2(
    gameState = GameState.createInitial()
  )*/
}