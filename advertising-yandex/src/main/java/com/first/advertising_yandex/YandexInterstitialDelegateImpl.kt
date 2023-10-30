package com.first.advertising_yandex

import android.app.Activity
import android.content.Context
import com.first.advertising.AdsInitializer
import com.first.advertising.interstitial.InterstitialDelegate

import com.ilyin.tools_android.RxTrackable
import com.ilyin.tools_android.RxTrackableDelegate
import com.yandex.mobile.ads.common.AdError

import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

class YandexInterstitialDelegateImpl constructor(
  private val ctx: Context,
  private val adsInitializer: AdsInitializer,
  private val adId: String,
) : RxTrackable by RxTrackableDelegate(), InterstitialDelegate {

  private var initializationStart = 0L
  private var interstitialAdLoader: InterstitialAdLoader? = createAdLoader()
  private var interstitialAd: InterstitialAd? = null
  private var showListener: PublishSubject<Unit> = PublishSubject.create()

  private val adEventListener = object : InterstitialAdEventListener {
    override fun onAdShown() {
    }

    override fun onAdFailedToShow(p0: AdError) {
    }

    override fun onAdDismissed() {
      destroyInterstitial()
      showListener.onNext(Unit)
    }

    override fun onAdClicked() {
    }

    override fun onAdImpression(impression: ImpressionData?) {
    }
  }

  private fun createAdLoader(): InterstitialAdLoader {
    return InterstitialAdLoader(ctx).apply {
      setAdLoadListener(object : InterstitialAdLoadListener {
        override fun onAdLoaded(newInterstitialAd: InterstitialAd) {
          interstitialAd = newInterstitialAd
          Timber.d("Яндекс реклама $adId загрузилась за ${System.currentTimeMillis() - initializationStart} ms")
          isAdLoading = false
        }

        override fun onAdFailedToLoad(err: AdRequestError) {
          Timber.d("Не удалось загрузить яндекс рекламу $err")
          destroyInterstitial()
          isAdLoading = false
        }
      })
    }
  }

  override fun watchRx(activity: Activity): Completable {
    val interstitialAd = interstitialAd
    return if (interstitialAd == null) {
      Completable.complete()
    } else {
      showAd(activity, interstitialAd)
    }
  }

  override val isAdLoaded: Boolean
    get() = interstitialAd != null

  private fun showAd(activity: Activity, interstitialAd: InterstitialAd): Completable {
    showListener = PublishSubject.create()
    interstitialAd.apply {
      setAdEventListener(adEventListener)
      show(activity)
    }
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
    initializationStart = System.currentTimeMillis()
    Timber.d("Началась загрузка яндекс рекламы $adId")
    interstitialAdLoader?.loadAd(createAdRequestConfiguration())
  }

  private fun createAdRequestConfiguration(): AdRequestConfiguration {
    return AdRequestConfiguration.Builder(adId).build()
  }

  private fun destroyInterstitial() {
    interstitialAd?.setAdEventListener(null)
    interstitialAd = null
  }

  override fun rxRelease() {
    super.rxRelease()
    interstitialAdLoader?.setAdLoadListener(null)
    interstitialAdLoader = null
    destroyInterstitial()
  }
}