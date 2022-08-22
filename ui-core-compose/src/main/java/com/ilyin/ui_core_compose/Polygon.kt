package com.ilyin.ui_core_compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

@Preview(name = "NEXUS_7", device = Devices.NEXUS_7)
@Preview(name = "NEXUS_7_2013", device = Devices.NEXUS_7_2013)
@Preview(name = "NEXUS_5", device = Devices.NEXUS_5)
@Preview(name = "NEXUS_6", device = Devices.NEXUS_6)
@Preview(name = "NEXUS_9", device = Devices.NEXUS_9)
@Preview(name = "NEXUS_10", device = Devices.NEXUS_10)
@Preview(name = "NEXUS_5X", device = Devices.NEXUS_5X)
@Preview(name = "NEXUS_6P", device = Devices.NEXUS_6P)
@Preview(name = "PIXEL_C", device = Devices.PIXEL_C)
@Preview(name = "PIXEL", device = Devices.PIXEL)
@Preview(name = "PIXEL_XL", device = Devices.PIXEL_XL)
@Preview(name = "PIXEL_2", device = Devices.PIXEL_2)
@Preview(name = "PIXEL_2_XL", device = Devices.PIXEL_2_XL)
@Preview(name = "PIXEL_3", device = Devices.PIXEL_3)
@Preview(name = "PIXEL_3_XL", device = Devices.PIXEL_3_XL)
@Preview(name = "PIXEL_3A", device = Devices.PIXEL_3A)
@Preview(name = "PIXEL_3A_XL", device = Devices.PIXEL_3A_XL)
@Preview(name = "PIXEL_4", device = Devices.PIXEL_4)
@Preview(name = "PIXEL_4_XL", device = Devices.PIXEL_4_XL)
@Preview(name = "AUTOMOTIVE_1024p", device = Devices.AUTOMOTIVE_1024p)
@Composable
fun Polygon() {
  val dm = LocalContext.current.resources.displayMetrics
  val widthPixels = dm.widthPixels
  val heightPixels = dm.heightPixels
  val densityDpi = dm.densityDpi
  val density = dm.density
  val xdpi = dm.xdpi
  val ydpi = dm.ydpi
  val scaledDensity = dm.scaledDensity

  val widthInch = widthPixels.toFloat() / xdpi
  val heightInch = heightPixels.toFloat() / ydpi
  val diagonalInch = sqrt(widthInch * widthInch + heightInch * heightInch).times(100).toInt().toFloat().div(100)

  val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
  Column(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier.padding(8.dp),
  ) {
    Text(text = "widthPixels = $widthPixels")
    Text(text = "heightPixels = $heightPixels")
    Text(text = "densityDpi = $densityDpi")
    Text(text = "density = $density")
    Text(text = "xdpi = $xdpi")
    Text(text = "ydpi = $ydpi")
    Text(text = "scaledDensity = $scaledDensity")
    Text(text = "dpWidth = ${widthPixels.toFloat() / density}")
    Text(text = "dpHeight = ${heightPixels.toFloat() / density}")
    Text(text = "diagonalInch = $diagonalInch")
    Text(text = "isPortrait = $isPortrait")
  }
}