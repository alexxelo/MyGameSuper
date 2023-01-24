package com.example.core.di

import android.content.Context
import android.telephony.TelephonyManager
import com.example.advertising.AdsInjector
import com.example.advertising.interstitial.InterstitialDelegate
import com.example.advertising.watchvideo.WatchVideoDelegate
import com.example.core.feature.game.interstitial.GameInterstitialController
import com.example.core.feature.game.interstitial.GameInterstitialMemory
import com.example.core.feature.game.interstitial.GameInterstitialMemoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdvertisingModule {

  @Singleton
  @Provides
  fun provideGameInterstitialMemory(@ApplicationContext ctx: Context): GameInterstitialMemory {
    return GameInterstitialMemoryImpl(ctx = ctx)
  }

  @Singleton
  @Provides
  fun provideGameInterstitialController(
    interstitialMemory: GameInterstitialMemory,
    telephonyManager: TelephonyManager,
  ): GameInterstitialController {
    return GameInterstitialController(
      interstitialMemory = interstitialMemory,
      telephonyManager = telephonyManager,
    )
  }

}