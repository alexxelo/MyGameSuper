package com.example.core

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ilyin.ui_core_compose.colors.MdColors
import kotlinx.coroutines.CoroutineScope

@Composable
fun MenuView(
    onClickPlay: () -> Unit = {},
    onClickTutorial: () -> Unit = {},
    onClickSettings: () -> Unit = {},
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
        /*Text(
            text = "Game Name",
            textAlign = TextAlign.Center,
            fontSize = nameTextSize
        )*/
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(onClick = { onClickPlay() }) {
                Text(
                    text = stringResource(id = com.ilyin.localization.R.string.play),
                    textAlign = TextAlign.Center,
                    fontSize = menuTextSize
                )
            }
            TextButton(onClick = { onClickTutorial() }) {
                Text(
                    text = stringResource(id = com.ilyin.localization.R.string.tutorial),
                    textAlign = TextAlign.Center,
                    fontSize = menuTextSize
                )
            }
            TextButton(onClick = { onClickStore() }) {
                Text(
                    text = stringResource(id = com.ilyin.localization.R.string.store),
                    textAlign = TextAlign.Center,
                    fontSize = menuTextSize
                )
            }
            TextButton(onClick = { onClickSettings() }) {
                Text(
                    text = stringResource(id = com.ilyin.localization.R.string.settings),
                    textAlign = TextAlign.Center,
                    fontSize = menuTextSize
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