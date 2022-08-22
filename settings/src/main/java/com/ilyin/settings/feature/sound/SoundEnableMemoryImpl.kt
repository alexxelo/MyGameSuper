package com.ilyin.settings.feature.sound

import android.content.Context
import com.ilyin.tools_android.crud.FlagPref
import com.ilyin.tools_android.util.prefs
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class SoundEnableMemoryImpl constructor(ctx: Context) : FlagPref(prefs = ctx.prefs("SoundEnableMemoryImpl")), SoundEnableMemory {

  private val _isEnabledObservable: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(enabled)
  override val isEnabledObservable: Observable<Boolean> = _isEnabledObservable

  override var enabled: Boolean
    get() = value
    set(value) {
      this.value = value
      _isEnabledObservable.onNext(value)
    }
}