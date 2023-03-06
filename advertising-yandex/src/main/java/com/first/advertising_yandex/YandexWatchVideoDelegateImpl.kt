package com.first.advertising_yandex

import android.app.Activity
import android.content.Context
import com.first.advertising.AdsInitializer
import com.first.advertising.watchvideo.VideoAdState
import com.first.advertising.watchvideo.VideoWatchState
import com.first.advertising.watchvideo.WatchVideoDelegate
import com.ilyin.tools_android.RxTrackable
import com.ilyin.tools_android.RxTrackableDelegate


import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener

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

  private var rewardedAd: RewardedAd? = null

  private val rewardAdEventListener: RewardedAdEventListener = object : RewardedAdEventListener {

    override fun onAdLoaded() {
      this@YandexWatchVideoDelegateImpl.onAdLoaded()
    }

    override fun onAdFailedToLoad(p0: AdRequestError) {
      Timber.d("Unable to load Yandex ads errCode = ${p0.code}, description = ${p0.description}")
      videoAdStateSubject.onNext(VideoAdState.NO_AD)
    }

    override fun onAdShown() {
      videoWatchSubject.onNext(VideoWatchState.AD_SHOWED)
    }

    override fun onAdDismissed() {
      rewardedAd?.destroy()
      rewardedAd = null
      videoWatchSubject.onNext(VideoWatchState.AD_DISMISSED)
    }

    override fun onRewarded(p0: Reward) {
      rewardSubject.onNext(p0)
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
    videoAdStateSubject.onNext(VideoAdState.AD_LOADING)
    val newRewardedAd = RewardedAd(ctx)
    newRewardedAd.setAdUnitId(adId)
    newRewardedAd.setRewardedAdEventListener(rewardAdEventListener)
    rewardedAd = newRewardedAd
    newRewardedAd.loadAd(AdRequest.Builder().build())
  }

  private fun onAdLoaded() {
    videoAdStateSubject.onNext(VideoAdState.AD_LOADED)
  }

  override fun release() {
    rewardedAd?.destroy()
    rewardedAd = null
    rxRelease()
  }

  override fun watch(activity: Activity) {
    rewardedAd?.show()
  }

  override fun reload() {
    if (adsInitialized) {
      onReload.onNext(Unit)
    } else {
      initialize()
    }
  }

  override fun isLoaded(): Boolean {
    return rewardedAd?.isLoaded ?: false
  }

  override fun isLoading(): Boolean {
    return videoAdStateSubject.value == VideoAdState.AD_LOADING
  }
}