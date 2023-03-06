package com.first.advertising_yandex

import android.content.Context
import com.first.advertising.AdsInitializer
import com.yandex.mobile.ads.common.MobileAds

import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import java.util.concurrent.TimeUnit


class YandexAdsInitializerImpl constructor(private val appCtx: Context) : AdsInitializer {

  private var isInitializingNow = false
  private var isInitializedInternal = false

  override fun init() {
    initialize(appCtx) { }
  }

  override fun initRx(): Single<Boolean> {
    return if (isInitializingNow) {
      Single.timer(1, TimeUnit.SECONDS)
        .flatMap { initRx() }
    } else if (isInitialized) {
      Single.just(true)
    } else {
      isInitializingNow = true
      Single.create { source ->
        initialize(appCtx, source::onSuccess)
      }
    }.timeout(5, TimeUnit.SECONDS, Single.just(false))
      .doFinally {
        isInitializingNow = false
      }
  }

  override val isInitialized: Boolean
    get() = isInitializedInternal

  private fun initialize(ctx: Context, onInitialized: (Boolean) -> Unit = {}) {
    Timber.d("Началась инициализация яндекс рекламы")
    val initializationStart = System.currentTimeMillis()
    MobileAds.initialize(ctx) {
      isInitializedInternal = true
      val someAdIsInitialized = true
      Timber.d("Инициализация яндекс рекламы завершена за ${System.currentTimeMillis() - initializationStart} ms.")
      onInitialized(someAdIsInitialized)
    }
  }
}