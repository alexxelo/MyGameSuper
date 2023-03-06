package com.first.advertising.interstitial

import android.app.Activity
import io.reactivex.rxjava3.core.Completable

interface InterstitialDelegate {

  fun watchRx(activity: Activity): Completable

  val isAdLoaded: Boolean

  var isAdLoading: Boolean

  fun loadAd()
}