package com.example.core

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.nativeCanvas
import com.example.core.GameViewUtils.drawNode
import com.example.engine2.game.Action
import com.example.engine2.node.Node
import com.example.engine2.node.NodeAction
import com.example.engine2.node.NodeElement
import com.ilyin.ui_core_compose.colors.MdColors

object GameViewUtils {

  fun DrawScope.drawNode(node: Node, circleRadius: Float, center: Offset) {
    if (node is NodeAction) {

      if (node.action == Action.PLUS) {
        this.drawCircle(
          color = Color.Red,
          radius = circleRadius,
          center = center
        )
        this.drawLine(
          color = Color.Black,
          start = center - Offset(x = circleRadius / 2, y = 0f),
          end = center + Offset(x = circleRadius / 2, y = 0f),
          strokeWidth = circleRadius / 5
        )
        this.drawLine(
          color = Color.Black,
          start = center - Offset(x = 0f, y = circleRadius / 2),
          end = center + Offset(x = 0f, y = circleRadius / 2),
          strokeWidth = circleRadius / 5
        )
      } else {
        this.drawCircle(
          color = Color.Blue,
          radius = circleRadius,
          center = center
        )
        this.drawLine(
          color = Color.Black,
          start = center - Offset(x = circleRadius / 2, y = 0f),
          end = center + Offset(x = circleRadius / 2, y = 0f),
          strokeWidth = circleRadius / 5
        )
      }
    } else if (node is NodeElement) {
      drawNodeElement(node = node, circleRadius = circleRadius)
    }
  }

  fun DrawScope.drawNodeElement(node: NodeElement, circleRadius: Float) {

    val mass = node.element.atomicMass
    val bgColor = getNodeElementBgColor(mass)
    val luminance = bgColor.luminance()
    val textColorRaw = if (luminance > 0.5f) Color.Black else Color.White
    val textColor = android.graphics.Color.rgb(
      (textColorRaw.red * 255).toInt(),
      (textColorRaw.green * 255).toInt(),
      (textColorRaw.blue * 255).toInt(),
    )
    this.drawCircle(
      color = bgColor,
      radius = circleRadius,
      center = center
    )
    val paint = Paint().apply {
      this.textSize = 50f
      color = textColor
    }
    val paintAM = Paint().apply {
      this.textSize = 30f
      color = textColor
    }

    val number = "$mass"
    val numWidth = paint.measureText(number)

    val text = getNodeElementName(mass)
    val textWidth = paint.measureText(text)
    this.drawIntoCanvas {
      it.nativeCanvas.drawText(
        text,
        center.x - textWidth / 2,
        center.y + paint.textSize / 3,
        paint
      )
    }
    this.drawIntoCanvas {
      it.nativeCanvas.drawText(
        number,
        center.x - numWidth / 4,
        center.y + paintAM.textSize * 2,
        paintAM
      )
    }

  }

  fun nodeContentColor(bgColor: Color): Int {
    val luminance = bgColor.luminance()
    val textColorRaw = if (luminance > 0.5f) Color.Black else Color.White
    return android.graphics.Color.rgb(
      (textColorRaw.red * 255).toInt(),
      (textColorRaw.green * 255).toInt(),
      (textColorRaw.blue * 255).toInt(),
    )
  }

  fun getNodeElementName(mass: Int): String {
    return when (mass) {
      1 -> "H"
      2 -> "He"
      3 -> "Li"
      4 -> "Be"
      5 -> "B"
      6 -> "C"
      7 -> "N"
      8 -> "O"
      9 -> "F"
      10 -> "Ne"
      11 -> "Na"
      12 -> "Mg"
      13 -> "Al"
      14 -> "Si"
      15 -> "P"
      16 -> "S"
      17 -> "Cl"
      18 -> "Ar"
      19 -> "K"
      20 -> "Ca"
      21 -> "Sc"
      22 -> "Ti"
      23 -> "V"
      24 -> "Cr"
      25 -> "Mn"
      26 -> "Fe"
      27 -> "Co"
      28 -> "Ni"
      29 -> "Cu"
      30 -> "Zn"
      31 -> "Ga"
      32 -> "Ge"
      33 -> "As"
      34 -> "Se"
      35 -> "Br"
      36 -> "Kr"
      37 -> "Rb"
      38 -> "Sr"
      39 -> "Y"
      40 -> "Zr"
      41 -> "Nb"
      42 -> "Mo"
      43 -> "Tc"
      44 -> "Ru"
      45 -> "Rh"
      46 -> "Pd"
      47 -> "Ag"
      48 -> "Cd"
      49 -> "In"
      50 -> "Sn"
      51 -> "Sb"
      52 -> "Te"
      53 -> "I"
      54 -> "Xe"
      55 -> "Cs"
      56 -> "Ba"
      57 -> "La"
      58 -> "Ce"
      59 -> "Pr"
      60 -> "Nd"
      61 -> "Pm"
      62 -> "Sm"
      63 -> "Eu"
      64 -> "Gd"
      65 -> "Tb"
      66 -> "Dy"
      67 -> "Ho"
      68 -> "Er"
      69 -> "Tm"
      70 -> "Yb"
      71 -> "Lu"
      72 -> "Hf"
      73 -> "Ta"
      74 -> "W"
      75 -> "Re"
      76 -> "Os"
      77 -> "Ir"
      78 -> "Pt"
      79 -> "Au"
      80 -> "Hg"
      81 -> "Tl"
      82 -> "Pb"
      83 -> "Bi"
      84 -> "Po"
      85 -> "At"
      86 -> "Rn"
      87 -> "Fr"
      88 -> "Ra"
      89 -> "Ac"
      90 -> "Th"
      91 -> "Pa"
      92 -> "U"
      93 -> "Np"
      94 -> "Pu"
      95 -> "Am"
      96 -> "Cm"
      97 -> "Bk"
      98 -> "Cf"
      99 -> "Es"
      100 -> "Fm"
      101 -> "Md"
      102 -> "No"
      103 -> "Lr"
      104 -> "Rf"
      105 -> "Db"
      106 -> "Sg"
      107 -> "Bh"
      108 -> "Hs"
      109 -> "Mt"
      110 -> "Ds"
      111 -> "Rg"
      112 -> "Cn"
      113 -> "Nh"
      114 -> "Fi"
      115 -> "Mc"
      116 -> "Lv"
      117 -> "Ts"
      118 -> "Og"
      else -> ""
    }
  }

  fun getNodeActionBgColor(action: Action): Color {
    return when (action) {
      Action.PLUS -> MdColors.red.c700
      Action.MINUS -> MdColors.blue.c700
      Action.BLACKPLUS -> Color.Black
      Action.SPFEARE -> MdColors.teal.c100
    }
  }

  private val colors = with(MdColors.colors) {
    map { it.c500 } +
        map { it.c600 } +
        map { it.c700 }
  }

  fun getNodeElementBgColor(mass: Int): Color {
    return colors[mass % colors.size]
  }
}