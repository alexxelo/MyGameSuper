package com.example.core.feature.game.tipshop

import androidx.lifecycle.LiveData
import com.example.core.feature.game.tipshop.free.ads.TipsForAdsVM
import com.example.core.feature.game.tipshop.free.pereodical.TipsPeriodicVM

interface TipShopVM {


  val tipPeriodicVm: TipsPeriodicVM

  val tipsForAdsVm: TipsForAdsVM

  val isTipShopShown: LiveData<Boolean>

  fun showTipShop()

  fun dismissTipShop()

  fun onCleared()
}