package com.example.core

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ilyin.ui_core_compose.colors.MdColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun MenuView(
    onClickPlay: () -> Unit = {},
    onClickTutorial: () -> Unit = {},
    onClickStore: () -> Unit = {}

) {
    val menuTextSize = 24.sp
    val nameTextSize = 32.sp
    val coroutine: CoroutineScope = rememberCoroutineScope()
    var colorChange by remember { mutableStateOf(Color.Black) }
    val colorAnimatable by animateColorAsState(
        targetValue = colorChange,
        animationSpec = tween(durationMillis = 2000)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MdColors.pink.c100),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Game Name",
            //Modifier.padding(vertical = 75.dp),
            textAlign = TextAlign.Center,
            fontSize = nameTextSize
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {



            TextButton(onClick = { onClickPlay() }) {
                Text(
                    text = "Play",
                    textAlign = TextAlign.Center,
                    fontSize = menuTextSize
                )
            }

            TextButton(onClick = { onClickTutorial() }) {
                Text(
                    text = "Tutorial",
                    textAlign = TextAlign.Center,
                    fontSize = menuTextSize
                )
            }

            TextButton(onClick = { onClickStore() }) {
                Text(
                    text = "Store",
                    textAlign = TextAlign.Center,
                    fontSize = menuTextSize
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val checkedState = remember { mutableStateOf(false) }
                Text(
                    text = "Sound",
                    Modifier.padding(vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    color = colorAnimatable,
                    fontSize = menuTextSize
                )
                Switch(
                    checked = checkedState.value,
                    onCheckedChange = { checkedState.value = it },
                )

            }
        }
    }
}

@Composable
@Preview
fun MenuViewPreview() {
    MenuView()
}