package com.example.core

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.nativeCanvas
import com.example.engine2.Action
import com.example.engine2.Node
import com.example.engine2.NodeAction
import com.example.engine2.NodeElement
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

    fun getNodeElementBgColor(mass: Int): Color {
        return when (mass) {
            1 -> Color.Blue
            2 -> Color.Green
            3 -> Color.Cyan
            4 -> Color.LightGray
            5 -> Color.Black
            6 -> Color.DarkGray
            7 -> Color.Magenta
            8 -> MdColors.brown.c900
            9 -> MdColors.amber.c600
            10 -> MdColors.blue.c600
            /*
            11 -> MdColors.
            12 -> MdColors.
            13 -> MdColors.
            14 -> MdColors.
            15 -> MdColors.
            16 -> MdColors.
            17 -> MdColors.
            18 -> MdColors.
            19 -> MdColors.
            20 -> MdColors.
            21 -> MdColors.
            22 -> MdColors.
            23 -> MdColors.
            24 -> MdColors.
            25 -> MdColors.
            26 -> MdColors.
            27 -> MdColors.
            28 -> MdColors.
            29 -> MdColors.
            30 -> MdColors.
            31 -> MdColors.
            32 -> MdColors.
            33 -> MdColors.
            34 -> MdColors.
            35 -> MdColors.
            36 -> MdColors.
            37 -> MdColors.
            38 -> MdColors.
            39 -> MdColors.
            40 -> MdColors.
            41 -> MdColors.
            42 -> MdColors.
            43 -> MdColors.
            44 -> MdColors.
            45 -> MdColors.
            46 -> MdColors.
            47 -> MdColors.
            48 -> MdColors.
            49 -> MdColors.
            50 -> MdColors.
            51 -> MdColors.
            52 -> MdColors.
            53 -> MdColors.
            54 -> MdColors.
            55 -> MdColors.
            56 -> MdColors.
            57 -> MdColors.
            58 -> MdColors.
            59 -> MdColors.
            60 -> MdColors.
            61 -> MdColors.
            62 -> MdColors.
            63 -> MdColors.
            64 -> MdColors.
            65 -> MdColors.
            66 -> MdColors.
            67 -> MdColors.
            68 -> MdColors.
            69 -> MdColors.
            70 -> MdColors.
            71 -> MdColors.
            72 -> MdColors.
            73 -> MdColors.
            74 -> MdColors.
            75 -> MdColors.
            76 -> MdColors.
            77 -> MdColors.
            78 -> MdColors.
            79 -> MdColors.
            80 -> MdColors.
            81 -> MdColors.
            82 -> MdColors.
            83 -> MdColors.
            84 -> MdColors.
            85 -> MdColors.
            86 -> MdColors.
            87 -> MdColors.
            88 -> MdColors.
            89 -> MdColors.
            90 -> MdColors.
            91 -> MdColors.
            92 -> MdColors.
            93 -> MdColors.
            94 -> MdColors.
            95 -> MdColors.
            96 -> MdColors.
            97 -> MdColors.
            98 -> MdColors.
            99 -> MdColors.
            100 -> MdColors.
            101 -> MdColors.
            102 -> MdColors.
            103 -> MdColors.
            104 -> MdColors.
            105 -> MdColors.
            106 -> MdColors.
            107 -> MdColors.
            108 -> MdColors.
            109 -> MdColors.
            110 -> MdColors.
            111 -> MdColors.
            112 -> MdColors.
            113 -> MdColors.
            114 -> MdColors.
            115 -> MdColors.
            116 -> MdColors.
            117 -> MdColors.
            118 -> MdColors.
*/
            else -> Color.Yellow
        }
    }
}