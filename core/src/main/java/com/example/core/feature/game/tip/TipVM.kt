package com.example.core.feature.game.tip

import androidx.lifecycle.LiveData

interface TipVM {

  val tips:LiveData<Int>

  val enableToUse: Boolean

  fun addTips(tips:Int)

  fun onCleared()

}