package com.example.core.feature.game.end

import androidx.lifecycle.LiveData

interface GameEndVM {
  val isShow: LiveData<Boolean>

  fun show()
  fun playClick()
}