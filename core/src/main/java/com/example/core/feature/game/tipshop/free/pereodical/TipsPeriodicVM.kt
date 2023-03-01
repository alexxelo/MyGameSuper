package com.example.core.feature.game.tipshop.free.pereodical

import androidx.lifecycle.LiveData

interface TipsPeriodicVM {

  val secondsRemains: LiveData<Long>

  fun onObtainClick()

  fun onCleared()

  fun reduceTimerByAdWatch()

  companion object {
    const val FREE_TIPS_COOLDOWN_MILLIS: Long = 7 * 60 * 1000
    const val FREE_TIPS = 2
  }
}