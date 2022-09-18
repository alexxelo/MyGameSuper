package com.example.core.feature.mainmenu

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import com.ilyin.ui_core_compose.colors.MdColor
import com.ilyin.ui_core_compose.isLight

object MainMenuTokens {

  val borderStroke = 4.dp

  @Composable
  fun computeBtnColor(color: MdColor): Color {
    return if (MaterialTheme.isLight()) {
      color.c300
    } else {
      color.c800
    }
      .copy(alpha = 0.90f)
      .compositeOver(MaterialTheme.colorScheme.primaryContainer)
  }

  @Composable
  fun cardBorderColor(): Color {
    return MaterialTheme.colorScheme.primary.copy(alpha = 0.32f).compositeOver(MaterialTheme.colorScheme.background)
  }

  @Composable
  fun cardBorder(): BorderStroke {
    return BorderStroke(
      width = borderStroke,
      color = cardBorderColor()
    )
  }
}