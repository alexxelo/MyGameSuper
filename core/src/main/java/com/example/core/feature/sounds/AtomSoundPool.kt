package com.example.core.feature.sounds

import java.io.Serializable

data class AtomSoundPool constructor(
  val isFake: Boolean = false,
  val newRecord: Int = 0,
  val takeElementWithMinus: Int = 0,
  val turnElementToPlus: Int = 0,
  val gameEnd: Int = 0,
  val generalClick: Int = 0, // node move to circle
): Serializable {

  companion object {

    fun fake(): AtomSoundPool {
      return AtomSoundPool(isFake = true)
    }
  }
}