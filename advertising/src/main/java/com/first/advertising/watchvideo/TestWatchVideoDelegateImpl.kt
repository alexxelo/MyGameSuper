package com.first.advertising.watchvideo

import android.app.Activity
import com.ilyin.tools_android.RxTrackable
import com.ilyin.tools_android.RxTrackableDelegate
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class TestWatchVideoDelegateImpl constructor() : RxTrackable by RxTrackableDelegate(), WatchVideoDelegate {

  private val videoAdStateSubject = BehaviorSubject.create<VideoAdState>()
  private val videoWatchSubject = BehaviorSubject.create<VideoWatchState>()
  private val rewardSubject = PublishSubject.create<Int>()

  private val onReload = BehaviorSubject.create<Unit>()
  private val onAdsInitialized = BehaviorSubject.create<Unit>()
  private var adsInitialized = false

  init {
    onLoadTrigger()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({ loadRewardedVideoAd() }, Timber::e)
      .connect()
  }

  private fun initialize() {
    Single.just(1)
      .delay(2, TimeUnit.SECONDS)
      .subscribe({
        onAdsInitialized.onNext(Unit)
        adsInitialized = true
      }, Timber::e)
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
    Single.just(1)
      .delay(2, TimeUnit.SECONDS)
      .subscribe({
        videoAdStateSubject.onNext(VideoAdState.AD_LOADED)
      }, Timber::e)
      .connect()
  }

  override fun release() {
    rxRelease()
  }

  override fun watch(activity: Activity) {
    onEarnedReward(1)
  }

  private fun onEarnedReward(reward: Int) {
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
    return videoAdStateSubject.value == VideoAdState.AD_LOADED
  }

  override fun isLoading(): Boolean {
    return videoAdStateSubject.value == VideoAdState.AD_LOADING
  }
}
