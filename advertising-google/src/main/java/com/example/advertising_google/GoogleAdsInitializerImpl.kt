package com.example.advertising_google

import android.content.Context
import com.example.advertising.AdsInitializer
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.AdapterStatus
import com.google.android.gms.ads.initialization.InitializationStatus
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import java.util.concurrent.TimeUnit

class GoogleAdsInitializerImpl constructor(private val appCtx: Context) : AdsInitializer {

  var isInitializingNow = false

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
      Single.create<Boolean> { source ->
        initialize(appCtx, source::onSuccess)
      }
    }.timeout(5, TimeUnit.SECONDS, Single.just(false))
      .doFinally {
        isInitializingNow = false
      }
  }

  override val isInitialized: Boolean
    get() {
      return try {
        MobileAds.getInitializationStatus()
        true
      } catch (e: IllegalStateException) {
        false
      } catch (e: Exception) {
        false
      }
    }

  private fun initialize(ctx: Context, onInitialized: (Boolean) -> Unit = {}) {
    Timber.d("Началась инициализация рекламы")
    val initializationStart = System.currentTimeMillis()
    MobileAds.initialize(ctx) { status ->
      onMobileAdsInitialized(status)
      val someAdIsInitialized = status.adapterStatusMap.any { it.value.initializationState == AdapterStatus.State.READY }
      Timber.d("Инициализация рекламы завершена за ${System.currentTimeMillis() - initializationStart} ms.")
      onInitialized(someAdIsInitialized)
    }
  }

  private fun onMobileAdsInitialized(status: InitializationStatus) {
    val statusMap: Map<String, AdapterStatus> = status.adapterStatusMap
    Timber.d("Инициализация рекламы завершена. Количество адаптеров для рекламы: ${statusMap.size}")
    for (adapterClass in statusMap.keys) {
      val adapterStatus = statusMap[adapterClass]
      adapterStatus?.let {
        val description = it.description
        val latency = it.latency
        val state = it.initializationState
        Timber.d(String.format("Adapter name: %s, Description: %s, Latency: %d, state %s", adapterClass, description, latency, state.toString()))
      }
    }
  }
}