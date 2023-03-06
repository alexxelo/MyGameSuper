package com.first.core.feature.game.tip

import android.content.Context
import com.ilyin.tools_android.crud.PreferencesWrapper
import com.ilyin.tools_android.util.prefs
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class TipMemoryImpl constructor(ctx: Context) : TipMemory, PreferencesWrapper(ctx.prefs("HelpMemoryImpl")) {

  override var tips: Int by IntPref(this, "tip", INITIAL_TIPS)

  override fun addTip(tips: Int){
    val newHints = this.tips + tips
    this.tips = newHints
    tipsObservable.onNext(newHints)
  }
  private val tipsObservable = BehaviorSubject.createDefault(tips)

  override val onTipsChanged: Observable<Int> = tipsObservable



  companion object {
    private const val INITIAL_TIPS = 2
  }

}