package com.first.core.feature.game.tipshop.free.pereodical

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.first.core.feature.sounds.GameSounds
import com.first.core.feature.game.tip.TipMemory
import com.first.core.feature.game.tipshop.free.pereodical.TipsPeriodicVM.Companion.FREE_TIPS
import com.first.core.feature.game.tipshop.free.pereodical.TipsPeriodicVM.Companion.FREE_TIPS_COOLDOWN_MILLIS
import com.ilyin.tools_android.VMPart
import com.ilyin.tools_android.VMPartImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.math.max

class TipsPeriodicVMImpl constructor(
  savedStateHandle: SavedStateHandle,
  private val tipMemory: TipMemory,
  private val tipPeriodicalMemory: FreePeriodicalMemory,
  private val gameSounds: GameSounds,


  ) : TipsPeriodicVM, VMPart by VMPartImpl(
  savedStateHandle = savedStateHandle,
  name = "TipsPeriodicVMImpl",
) {
  private var secondsRemain: Long = computeSecondsRemain()


  private val _secondsRemains = getLiveData("secondsRemains", secondsRemain)
  override val secondsRemains: LiveData<Long> = _secondsRemains


  private var timerDisposable: Disposable? = null

  init {
    if (secondsRemain > 0) {
      initiateTimer(secondsRemain)
    }
  }

  override fun onObtainClick() {
    gameSounds.playGeneralClick()
    tipMemory.addTip(FREE_TIPS)
    tipPeriodicalMemory.lastRewardTimeMillis = System.currentTimeMillis()
    initiateTimer(FREE_TIPS_COOLDOWN_MILLIS / 1000 - 1)
  }

  override fun onCleared() {
    timerDisposable?.dispose()
    timerDisposable = null
  }

  override fun reduceTimerByAdWatch() {
    val reduceSeconds = 2 * 60
    tipPeriodicalMemory.lastRewardTimeMillis -= reduceSeconds * 1000
    secondsRemain -= reduceSeconds
  }

  private fun initiateTimer(initialTimeSeconds: Long) {
    setSecondsRemain(initialTimeSeconds)
    timerDisposable = Observable.interval(1, TimeUnit.SECONDS)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        setSecondsRemain(secondsRemain - 1)
      }, Timber::e)
  }

  private fun setSecondsRemain(secondsRemain: Long) {
    this.secondsRemain = secondsRemain
    _secondsRemains.value = secondsRemain
    if (secondsRemain <= 0) {
      timerDisposable?.dispose()
      timerDisposable = null
    }
  }


  private fun computeSecondsRemain(): Long {
    val timeElapsedMillis = System.currentTimeMillis() - tipPeriodicalMemory.lastRewardTimeMillis
    return max(FREE_TIPS_COOLDOWN_MILLIS - timeElapsedMillis, 0) / 1000
  }
}