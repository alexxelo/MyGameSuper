package com.example.mygame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.core.navigation.AppNavGraph
import com.example.mygame.ui.theme.MyGameTheme
import com.ilyin.ui_core_compose.colors.MdColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyGameTheme {
                // Корабли лавировали
                //MainContent()
                /*
                MenuView()

                Box(modifier = Modifier.fillMaxSize()) {
                    Greeting(name = "Hello")
                }
                */


                AppNavGraph()


            }
        }
    }
}

/*
@Composable
fun MainContent(){
    val isEnabled = remember { mutableStateOf(true) }
    val isTop = remember { mutableStateOf(true) }

    val offset: Offset by animateOffsetAsState(
        targetValue = if (isTop.value)
            // start position and end position
            Offset(0f, 0f) else Offset(100f, 400f),
        animationSpec = tween(
            durationMillis = 2000, // duration
            easing = FastOutSlowInEasing
        ),
        finishedListener = {
            // disable the button
            isEnabled.value = true
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MdColors.blue.c300)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                isTop.value = !isTop.value
                isEnabled.value = false
            },
            colors = ButtonDefaults.buttonColors(
                MdColors.green.c300, MdColors.yellow.c500
            ),
            enabled = isEnabled.value
        ) {
            Text(
                text = "Animate Offset Change",
                modifier = Modifier.padding(12.dp)
            )
        }

        Icon(
            Icons.Filled.AccountBox,
            contentDescription = "Localized description",
            Modifier
                .size(150.dp)
                .offset(offset.x.dp, offset.y.dp),
            tint = MdColors.pink.c300
        )
    }
}


*/

@Composable
fun Greeting(name: String) {

    val wCenter = 10f / 2
    val hCenter = 10f / 2
    val center = Offset(wCenter, hCenter)

    val coroutine: CoroutineScope = rememberCoroutineScope()
    var offset by remember { mutableStateOf(center) }
    val offsetAnimation by animateOffsetAsState(targetValue = offset)

    var offsetX: Dp by remember { mutableStateOf(40.dp) }
    val offsetXAnimation: Dp by animateDpAsState(targetValue = offsetX)
    var offsetY: Dp by remember { mutableStateOf(40.dp) }

    val offsetYAnimation: Dp by animateDpAsState(targetValue = offsetY)
    var scale by remember { mutableStateOf(1f) }
    val scaleAnimatable by animateFloatAsState(targetValue = scale)

    var rotation by remember { mutableStateOf(0f) }
    val rotationAnimatable by animateFloatAsState(
        targetValue = rotation, animationSpec = tween(durationMillis = 2000)
    )

    var colorChange by remember { mutableStateOf(MdColors.green.c400) }
    val colorAnimatable by animateColorAsState(
        targetValue = colorChange,
        animationSpec = tween(durationMillis = 2000)
    )

    Offset(offset.x, offset.y)
    Button(
        onClick = {
            coroutine.launch {
                offsetX = 200.dp
                //delay(500)
                //offsetY = 100.dp
                /*scale = 2f
                delay(300)
                scale = 1f
                delay(300)
                scale = 3f
                delay(300)
                scale = 1f
                rotation = 360f*/
                offset = Offset(x = 20f, y = 20f)

            }
        },

        modifier = Modifier
            .offset(
                x = offsetXAnimation,
                y = offsetYAnimation
            )
            .rotate(degrees = rotationAnimatable)
            .scale(scale = scaleAnimatable),

        colors = ButtonDefaults.buttonColors(backgroundColor = colorAnimatable)


    ) {
        Text(text = "Hello $name!")
    }


}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyGameTheme {
        Greeting("Android")
    }
}