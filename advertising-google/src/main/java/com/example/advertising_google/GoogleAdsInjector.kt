package com.example.advertising_google

import android.content.Context
import com.example.advertising.AdsInitializer
import com.example.advertising.AdsInjector
import com.example.advertising.interstitial.InterstitialDelegate
import com.example.advertising.watchvideo.WatchVideoDelegate

class GoogleAdsInjector constructor(private val ctx: Context) : AdsInjector {

  private val adsInitializer: AdsInitializer by lazy {
    GoogleAdsInitializerImpl(ctx)
  }

  override val interstitialDelegate: InterstitialDelegate by lazy {
    GoogleInterstitialDelegateImpl(
      ctx = ctx,
      adsInitializer = adsInitializer,
      adId = "ca-app-pub-3311813745114526/2719401525",
    )
  }

  override val watchVideoDelegate: WatchVideoDelegate by lazy {
    GoogleWatchVideoDelegateImpl(
      ctx = ctx,
      adsInitializer = adsInitializer,
      adId = "ca-app-pub-3311813745114526/6998924862",
    )
//    TestWatchVideoDelegateImpl()
  }
}