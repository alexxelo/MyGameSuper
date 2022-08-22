package com.ilyin.settings.feature.hidesystemui

import android.content.Context
import com.ilyin.tools_android.crud.FlagPref
import com.ilyin.tools_android.util.prefs
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class SystemUiHideMemoryImpl constructor(ctx: Context) : SystemUiHideMemory, FlagPref(prefs = ctx.prefs("SystemUiHideMemoryImpl")) {

  private val _isHiddenObservable: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(isHidden)

  override var isHidden: Boolean
    get() = value
    set(value) {
      this.value = value
      _isHiddenObservable.onNext(value)
    }

  override val isHiddenObservable: Observable<Boolean> = _isHiddenObservable
}