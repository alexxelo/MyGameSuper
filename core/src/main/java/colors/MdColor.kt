package com.ilyin.ui_core_compose.colors

import androidx.compose.ui.graphics.Color

data class MdColor constructor(
  val id: String,
  val c50: Color,
  val c100: Color = c50,
  val c200: Color = c100,
  val c300: Color = c200,
  val c400: Color = c300,
  val c500: Color = c400,
  val c600: Color = c500,
  val c700: Color = c600,
  val c800: Color = c700,
  val c850: Color = c800,
  val c900: Color = c800,
  val cA100: Color = c900,
  val cA200: Color = cA100,
  val cA400: Color = cA200,
  val cA700: Color = cA400,
)