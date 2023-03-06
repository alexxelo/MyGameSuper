package com.first.core.feature.game.tip

import io.reactivex.rxjava3.core.Observable

interface TipMemory {

  var tips: Int

  fun addTip(tips: Int)

  val onTipsChanged: Observable<Int>

}