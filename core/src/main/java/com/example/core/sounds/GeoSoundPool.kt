package com.ilyinp.core.sounds

class GeoSoundPool constructor(
  val isFake: Boolean = true,
  val uiGeneralClick: Int = 0,
) {

  companion object {

    fun fake(): GeoSoundPool {
      return GeoSoundPool(isFake = true)
    }
  }
}