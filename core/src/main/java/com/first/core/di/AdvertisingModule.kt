package com.first.core.di

import android.content.Context
import android.telephony.TelephonyManager
import com.first.core.feature.game.gameinterstitial.GameInterstitialController
import com.first.core.feature.game.gameinterstitial.GameInterstitialMemory
import com.first.core.feature.game.gameinterstitial.GameInterstitialMemoryImpl
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