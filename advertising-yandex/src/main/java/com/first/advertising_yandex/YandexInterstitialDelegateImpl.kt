package com.first.advertising_yandex

import android.app.Activity
import android.content.Context
import com.first.advertising.AdsInitializer
import com.first.advertising.interstitial.InterstitialDelegate

import com.ilyin.tools_android.RxTrackable
import com.ilyin.tools_android.RxTrackableDelegate

import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

class YandexInterstitialDelegateImpl constructor(
  private val ctx: Context,
  private val adsInitializer: AdsInitializer,
  private val adId: String,
) : RxTrackable by RxTrackableDelegate(),
  InterstitialDelegate {

  private var initializationStart = 0L
  private var interstitialAd: InterstitialAd? = null
  private var showListener: PublishSubject<Unit> = PublishSubject.create()

  private val interstitialAdEventListener = object : InterstitialAdEventListener {
    override fun onAdLoaded() {
      Timber.d("Яндекс реклама $adId загрузилась за ${System.currentTimeMillis() - initializationStart} ms")
      isAdLoading = false
    }

    override fun onAdFailedToLoad(p0: AdRequestError) {
      Timber.d("Не удалось загрузить яндекс рекламу $p0")
      this@YandexInterstitialDelegateImpl.interstitialAd = null
      isAdLoading = false
    }

    override fun onAdShown() {
      showListener.onNext(Unit)
    }

    override fun onAdDismissed() {
    }

    override fun onAdClicked() {
    }

    override fun onLeftApplication() {
    }

    override fun onReturnedToApplication() {
    }

    override fun onImpression(p0: ImpressionData?) {
    }

  }

  override fun watchRx(activity: Activity): Completable {
    val interstitialAd = interstitialAd
    return if (interstitialAd != null && interstitialAd.isLoaded) {
      showAd(interstitialAd)
    } else {
      Completable.complete()
    }
  }

  override val isAdLoaded: Boolean
    get() = interstitialAd?.isLoaded ?: false

  private fun showAd(interstitialAd: InterstitialAd): Completable {
    showListener = PublishSubject.create()
    interstitialAd.show()
    return showListener.firstOrError().ignoreElement()
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
    Timber.d("Началась загрузка яндекс рекламы $adId")

    val newInterstitialAd = InterstitialAd(ctx)
    newInterstitialAd.setAdUnitId(adId)
    newInterstitialAd.setInterstitialAdEventListener(interstitialAdEventListener)
    interstitialAd = newInterstitialAd
    initializationStart = System.currentTimeMillis()
    newInterstitialAd.loadAd(AdRequest.Builder().build())
  }
}