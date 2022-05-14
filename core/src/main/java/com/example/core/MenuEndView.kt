package com.example.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.ilyin.ui_core_compose.colors.MdColors

@Composable
fun MenuEnd() {

    Column(
        Modifier
            .background(MdColors.pink.c200)
            .fillMaxSize(),
        //horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
        ) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = "")
        }
        Text(text = "lives")
        Text(text = "score")
        Text(text = "element name")
        Text(text = "element image")
        //Text(text = "element image")
        Text(text = "newGame")
    }


}

@Composable
@Preview
fun MenuEndViewPreview() {
    MenuEnd()
}