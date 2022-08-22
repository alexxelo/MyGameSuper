package com.ilyin.settings.feature.nightmode

import android.content.Context
import com.ilyin.tools_android.crud.FlagPref
import com.ilyin.tools_android.util.prefs
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class NightModeMemoryImpl constructor(
  ctx: Context,
  defaultValueComputer: () -> Boolean,
) : FlagPref(
  defaultValueComputer = defaultValueComputer,
  prefs = ctx.prefs("NightModePref")
), NightModeMemory {

  private val _isNightModeObservable: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(isNightMode)

  override var isNightMode: Boolean
    get() = value
    set(value) {
      this.value = value
      _isNightModeObservable.onNext(value)
    }

  override val isNightModeObservable: Observable<Boolean> = _isNightModeObservable
}