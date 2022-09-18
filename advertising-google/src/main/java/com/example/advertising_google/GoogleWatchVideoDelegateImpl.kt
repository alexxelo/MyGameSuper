package com.example.advertising_google

import android.app.Activity
import android.content.Context
import com.example.advertising.AdsInitializer
import com.example.advertising.watchvideo.VideoAdState
import com.example.advertising.watchvideo.VideoWatchState
import com.example.advertising.watchvideo.WatchVideoDelegate
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.ilyin.tools_android.RxTrackable
import com.ilyin.tools_android.RxTrackableDelegate
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

class GoogleWatchVideoDelegateImpl constructor(
  private val ctx: Context,
  private val adsInitializer: AdsInitializer,
  private val adId: String,
) : RxTrackable by RxTrackableDelegate(), WatchVideoDelegate {

  private var rewardedAd: RewardedAd? = null

  private val fullScreenContentCallback: FullScreenContentCallback = object : FullScreenContentCallback() {
    override fun onAdShowedFullScreenContent() {
      videoWatchSubject.onNext(VideoWatchState.AD_SHOWED)
    }

    override fun onAdDismissedFullScreenContent() {
      rewardedAd = null
      videoWatchSubject.onNext(VideoWatchState.AD_DISMISSED)
    }
  }

  private val videoAdStateSubject = BehaviorSubject.create<VideoAdState>()
  private val videoWatchSubject = BehaviorSubject.create<VideoWatchState>()
  private val rewardSubject = PublishSubject.create<RewardItem>()

  private val onReload = BehaviorSubject.create<Unit>()
  private val onAdsInitialized = BehaviorSubject.create<Unit>()
  private var adsInitialized = false

  init {
    onLoadTrigger()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({ loadRewardedVideoAd() }, Timber::e)
//      .connect()
  }

  private fun initialize() {
    adsInitializer.initRx()
      .subscribe({ successfullyInitialized ->
        if (successfullyInitialized) {
          onAdsInitialized.onNext(Unit)
        } else {
          videoAdStateSubject.onNext(VideoAdState.NO_AD)
        }
        adsInitialized = successfullyInitialized
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
      .mergeWith(onReload)
      .mergeWith(onAdsInitialized)
  }

  private fun loadRewardedVideoAd() {
    videoAdStateSubject.onNext(VideoAdState.AD_LOADING)
    val rewardLoadCallBack = object : RewardedAdLoadCallback() {
      override fun onAdLoaded(rewardedAd: RewardedAd) {
        super.onAdLoaded(rewardedAd)
        this@GoogleWatchVideoDelegateImpl.onAdLoaded(rewardedAd)
      }

      override fun onAdFailedToLoad(err: LoadAdError) {
        super.onAdFailedToLoad(err)
        Timber.e(err.message)
        videoAdStateSubject.onNext(VideoAdState.NO_AD)
      }
    }
    RewardedAd.load(ctx, adId, AdManager.adRequest(), rewardLoadCallBack)
  }

  private fun onAdLoaded(rewardedAd: RewardedAd) {
    this.rewardedAd = rewardedAd
    rewardedAd.fullScreenContentCallback = fullScreenContentCallback
    videoAdStateSubject.onNext(VideoAdState.AD_LOADED)
  }

  override fun release() {
    rewardedAd = null
    rxRelease()
  }

  override fun watch(activity: Activity) {
    rewardedAd?.show(activity, ::onEarnedReward)
  }

  private fun onEarnedReward(reward: RewardItem) {
    rewardSubject.onNext(reward)
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
}