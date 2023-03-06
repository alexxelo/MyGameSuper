package com.first.core.feature.game.tipshop

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.first.advertising.watchvideo.WatchVideoDelegate
import com.first.core.feature.game.gameinterstitial.GameInterstitialController
import com.first.core.feature.sounds.GameSounds
import com.first.core.feature.game.tip.TipMemory
import com.first.core.feature.game.tipshop.free.ads.TipsForAdsVM
import com.first.core.feature.game.tipshop.free.ads.TipsForAdsVMImpl
import com.first.core.feature.game.tipshop.free.pereodical.FreePeriodicalMemory
import com.first.core.feature.game.tipshop.free.pereodical.TipsPeriodicVM
import com.first.core.feature.game.tipshop.free.pereodical.TipsPeriodicVMImpl
import com.ilyin.tools_android.VMPart

class TipShopVMImpl constructor(
  savedStateHandle: SavedStateHandle,
  tipMemory: TipMemory,
  private val gameInterstitialController: GameInterstitialController,
  private val gameSounds: GameSounds,
  tipPeriodicalMemory: FreePeriodicalMemory,

  watchVideoDelegate: WatchVideoDelegate,
  //gameInterstitialController: GameInterstitialController,

) : TipShopVM, VMPart by VMPart.create(
  savedStateHandle = savedStateHandle,
  name = "TipShopVMImpl"
) {

  override val tipPeriodicVm: TipsPeriodicVM = TipsPeriodicVMImpl(
    savedStateHandle = savedStateHandle,
    tipMemory = tipMemory,
    tipPeriodicalMemory = tipPeriodicalMemory,
    gameSounds = gameSounds,
  )

  override val tipsForAdsVm: TipsForAdsVM = TipsForAdsVMImpl(
    savedStateHandle = savedStateHandle,
    watchVideoDelegate = watchVideoDelegate,
    tipMemory = tipMemory,
    gameInterstitialController = gameInterstitialController,
    gameSounds = gameSounds,
    onAdWatched = tipPeriodicVm::reduceTimerByAdWatch,
  )

  private val _isTipShopShown = getLiveData("isTipShopShown", false)
  override val isTipShopShown: LiveData<Boolean> = _isTipShopShown

  override fun showTipShop() {
    gameSounds.playGeneralClick()
    _isTipShopShown.value = true
  }

  override fun dismissTipShop() {
    _isTipShopShown.value = false
  }

  override fun onCleared() {

  }
}