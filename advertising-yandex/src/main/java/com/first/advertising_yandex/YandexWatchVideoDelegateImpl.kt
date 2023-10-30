package com.first.advertising_yandex


import android.app.Activity
import android.content.Context
import com.first.advertising.AdsInitializer
import com.first.advertising.watchvideo.VideoAdState
import com.first.advertising.watchvideo.VideoWatchState
import com.first.advertising.watchvideo.WatchVideoDelegate
import com.ilyin.tools_android.RxTrackable
import com.ilyin.tools_android.RxTrackableDelegate
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

class YandexWatchVideoDelegateImpl constructor(
  private val ctx: Context,
  private val adsInitializer: AdsInitializer,
  private val adId: String,
) : RxTrackable by RxTrackableDelegate(), WatchVideoDelegate {

  private var adLoader: RewardedAdLoader? = createAdLoader()
  private var rewardedAd: RewardedAd? = null

  private val adEventListener: RewardedAdEventListener = object : RewardedAdEventListener {
    override fun onAdShown() {
      videoWatchSubject.onNext(VideoWatchState.AD_SHOWED)
    }

    override fun onAdFailedToShow(err: AdError) {
    }

    override fun onAdDismissed() {
      destroyRewardedAd()
      videoWatchSubject.onNext(VideoWatchState.AD_DISMISSED)
    }

    override fun onAdClicked() {
    }

    override fun onAdImpression(impression: ImpressionData?) {
    }

    override fun onRewarded(reward: Reward) {
      rewardSubject.onNext(reward)
    }
  }

  private val videoAdStateSubject = BehaviorSubject.create<VideoAdState>()
  private val videoWatchSubject = BehaviorSubject.create<VideoWatchState>()
  private val rewardSubject = PublishSubject.create<Reward>()

  //  private val onResumed = BehaviorSubject.create<Unit>()
  private val onReload = BehaviorSubject.create<Unit>()
  private val onAdsInitialized = BehaviorSubject.create<Unit>()
  private var adsInitialized = false

  init {
//    initialize()
    onLoadTrigger()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({ loadRewardedVideoAd() }, Timber::e)
//      .connect()
//    loadRewardedVideoAd()
  }

  private fun createAdLoader(): RewardedAdLoader {
    return RewardedAdLoader(ctx).apply {
      setAdLoadListener(object : RewardedAdLoadListener {
        override fun onAdLoaded(newRewardedAd: RewardedAd) {
          Timber.d("Яндекс реклама загружена $adId")
          rewardedAd = newRewardedAd
          videoAdStateSubject.onNext(VideoAdState.AD_LOADED)
        }

        override fun onAdFailedToLoad(err: AdRequestError) {
          Timber.d("Unable to load Yandex ads errCode = ${err.code}, description = ${err.description}")
          destroyRewardedAd()
          videoAdStateSubject.onNext(VideoAdState.NO_AD)
        }
      })
    }
  }

  private fun initialize() {
    adsInitializer.initRx()
      .subscribe({
        onAdsInitialized.onNext(Unit)
        adsInitialized = true
      }, Timber::e)
      .connect()
  }

  override fun videoAdState(): Observable<VideoAdState> {
    return videoAdStateSubject
      .onErrorResumeNext { Observable.just(VideoAdState.AD_LOADING) }
  }

  override fun onWatched(): Observable<Unit> {
    return rewardSubject.map { }
  }

  private fun onLoadTrigger(): Observable<Unit> {
    return videoWatchSubject
      .filter { it == VideoWatchState.AD_DISMISSED }
      .map { }
//      .mergeWith(onResumed)
      .mergeWith(onReload)
      .mergeWith(onAdsInitialized)
  }

  private fun loadRewardedVideoAd() {
    Timber.d("Началась загрузка яндекс рекламы $adId")
    videoAdStateSubject.onNext(VideoAdState.AD_LOADING)
    adLoader?.loadAd(createAdRequestConfiguration())
  }

  override fun release() {
    adLoader?.setAdLoadListener(null)
    adLoader = null
    destroyRewardedAd()
    rxRelease()
  }

  override fun watch(activity: Activity) {
    rewardedAd?.apply {
      setAdEventListener(adEventListener)
      show(activity)
    }
  }

  override fun reload() {
    if (adsInitialized) {
      onReload.onNext(Unit)
    } else {
      initialize()
    }
  }

  override fun isLoaded(): Boolean {
    return rewardedAd != null
  }

  override fun isLoading(): Boolean {
    return videoAdStateSubject.value == VideoAdState.AD_LOADING
  }

  private fun createAdRequestConfiguration(): AdRequestConfiguration {
    return AdRequestConfiguration.Builder(adId).build()
  }

  private fun destroyRewardedAd() {
    rewardedAd?.setAdEventListener(null)
    rewardedAd = null
  }
}