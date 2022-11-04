package com.example.core.feature.tipshop.free.ads

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.example.advertising.watchvideo.VideoAdState
import com.example.advertising.watchvideo.WatchVideoDelegate
import com.example.core.feature.game.interstitial.GameInterstitialController
import com.example.core.feature.sounds.GameSounds
import com.example.core.feature.tip.TipMemory
import com.example.core.feature.tipshop.free.ads.TipsForAdsVM.Companion.TIPS_FOR_AD
import com.ilyin.tools_android.VMPart
import com.ilyin.tools_android.VMPartImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber

class TipsForAdsVMImpl constructor(
  savedStateHandle: SavedStateHandle,
  private val watchVideoDelegate: WatchVideoDelegate,
  private val gameInterstitialController: GameInterstitialController,
  private val gameSounds: GameSounds,
  private val onAdWatched: () -> Unit = {},
  private val tipMemory: TipMemory,

  ) : TipsForAdsVM, VMPart by VMPartImpl(
  savedStateHandle = savedStateHandle,
  name = "TipsForAdsVMImpl"
) {


  private val _adState = getLiveData("adState", VideoAdState.AD_LOADING)
  override val adState: LiveData<VideoAdState> = _adState

  private var watchVideoWatcher: Disposable? = watchVideoDelegate.onWatched()
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe({ onWatched() }, Timber::e)

  private var loadStatusWatcher: Disposable? = watchVideoDelegate.videoAdState()
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe({ _adState.value = it }, Timber::e)

  init {
    if (!watchVideoDelegate.isLoaded() && !watchVideoDelegate.isLoading()) {
      watchVideoDelegate.reload()
    }
  }

  private fun onWatched() {
    gameInterstitialController.markAdShown()
    tipMemory.addTip(TIPS_FOR_AD)
    watchVideoDelegate.reload()
    requestReload()
    onAdWatched()
  }

  override fun requestReload() {
    gameSounds.playGeneralClick()
    if (!watchVideoDelegate.isLoading() && !watchVideoDelegate.isLoaded()) {
      watchVideoDelegate.reload()
    }
  }

  override fun watch(activity: Activity) {
    gameSounds.playGeneralClick()
    watchVideoDelegate.watch(activity)
  }

  override fun onCleared() {
    watchVideoDelegate.release()
    watchVideoWatcher?.dispose()
    watchVideoWatcher = null
    loadStatusWatcher?.dispose()
    loadStatusWatcher = null
  }
}