package com.ilyin.settings.feature.nightmode

import io.reactivex.rxjava3.core.Observable

interface NightModeMemory {

  var isNightMode: Boolean

  val isNightModeObservable: Observable<Boolean>

}