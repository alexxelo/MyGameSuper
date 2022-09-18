package com.example.advertising_google

import android.app.Activity
import android.content.Context
import com.example.advertising.AdsInitializer
import com.example.advertising.interstitial.InterstitialDelegate
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.ilyin.tools_android.RxTrackable
import com.ilyin.tools_android.RxTrackableDelegate
import io.reactivex.rxjava3.core.Completable
import timber.log.Timber

class GoogleInterstitialDelegateImpl constructor(
  private val ctx: Context,
  private val adsInitializer: AdsInitializer,
  private val adId: String,
) : RxTrackable by RxTrackableDelegate(),
  InterstitialDelegate {

  private var interstitialAd: InterstitialAd? = null

  override fun watchRx(activity: Activity): Completable {
    val interstitialAd = interstitialAd
    return if (interstitialAd != null) {
      showAd(activity, interstitialAd)
    } else {
      Completable.complete()
    }
  }

  override val isAdLoaded: Boolean
    get() = interstitialAd != null

  private fun showAd(activity: Activity, interstitialAd: InterstitialAd): Completable {
    return Completable.create { source ->
      interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
        override fun onAdDismissedFullScreenContent() {
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
          source.onComplete()
        }

        override fun onAdShowedFullScreenContent() {
          this@GoogleInterstitialDelegateImpl.interstitialAd = null
          source.onComplete()
        }
      }
      interstitialAd.show(activity)
    }
  }

  override var isAdLoading: Boolean = false

  override fun loadAd() {
    isAdLoading = true
    if (adsInitializer.isInitialized) {
      innerLoad()
    } else {
      adsInitializer.initRx()
        .doOnError { isAdLoading = false }
        .subscribe({ adIsInitialized ->
          if (adIsInitialized) {
            innerLoad()
          } else {
            isAdLoading = false
          }
        }, Timber::e)
        .connect()
    }
  }

  private fun innerLoad() {
    isAdLoading = true
    Timber.d("Началась загрузка рекламы $adId")
    val adRequest = AdManager.adRequest()
    val initializationStart = System.currentTimeMillis()
    InterstitialAd.load(ctx, adId, adRequest, object : InterstitialAdLoadCallback() {
      override fun onAdFailedToLoad(adError: LoadAdError) {
        Timber.d(adError.toString())
        Timber.d("Не удалось загрузить рекламу $adError")
        this@GoogleInterstitialDelegateImpl.interstitialAd = null
        isAdLoading = false
      }

      override fun onAdLoaded(interstitialAd: InterstitialAd) {
        Timber.d("Реклама $adId загрузилась за ${System.currentTimeMillis() - initializationStart} ms")
        this@GoogleInterstitialDelegateImpl.interstitialAd = interstitialAd
        isAdLoading = false
      }
    })
  }
}
