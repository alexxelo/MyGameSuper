package com.example.core.feature.tip

import io.reactivex.rxjava3.core.Observable

interface TipMemory {

  var tips: Int

  fun addTip(tips: Int)

  val onTipsChanged: Observable<Int>

}