package com.ilyin.settings.feature.sound

import io.reactivex.rxjava3.core.Observable

interface SoundEnableMemory {

  var enabled: Boolean

  val isEnabledObservable: Observable<Boolean>
}