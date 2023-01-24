package com.example.app_google_core.di

import android.content.Context
import android.telephony.TelephonyManager
import com.example.advertising.AdsInitializer
import com.example.advertising.interstitial.InterstitialDelegate
import com.example.advertising.watchvideo.WatchVideoDelegate
import com.example.advertising_google.GoogleAdsInitializerImpl
import com.example.advertising_google.GoogleInterstitialDelegateImpl
import com.example.advertising_google.GoogleWatchVideoDelegateImpl
import com.example.advertising_yandex.YandexAdsInitializerImpl
import com.example.advertising_yandex.YandexInterstitialDelegateImpl
import com.example.advertising_yandex.YandexWatchVideoDelegateImpl
import com.example.app_google_core.auth.GoogleSignInControllerImpl
import com.example.auth.SignInController
import com.example.auth_google.GoogleSignInFlow
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.games.Games
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleCoreModule {

  @Singleton
  @Provides
  fun provideGoogleSignInOptions(): GoogleSignInOptions {
    return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
      .requestScopes(Games.SCOPE_GAMES_SNAPSHOTS)
//    .requestEmail()
      .build()
  }

  @Singleton
  @Provides
  fun provideSignInBtnController(googleSignInFlow: GoogleSignInFlow): SignInController {
    return GoogleSignInControllerImpl(
      signInFlow = googleSignInFlow,
    )
  }

  @Singleton
  @Provides
  fun provideAdsInitializer(
    @ApplicationContext ctx: Context,
    telephonyManager: TelephonyManager,
  ): AdsInitializer {
    val countryIso = telephonyManager.networkCountryIso
    return if (countryIso == "ru") {
      YandexAdsInitializerImpl(ctx)
    } else {
      GoogleAdsInitializerImpl(ctx)
    }
  }

  @Singleton
  @Provides
  fun provideInterstitialDelegate(
    @ApplicationContext ctx: Context,
    adsInitializer: AdsInitializer,
    telephonyManager: TelephonyManager,
  ): InterstitialDelegate {
    val countryIso = telephonyManager.networkCountryIso
    return if (countryIso == "ru") {
      YandexInterstitialDelegateImpl(
        ctx = ctx,
        adsInitializer = adsInitializer,
        adId = "R-M-2004180-2",
      )
    } else {
      GoogleInterstitialDelegateImpl(
        ctx = ctx,
        adsInitializer = adsInitializer,
        adId = "ca-app-pub-3311813745114526/2719401525",
      )
    }
  }

  @Singleton
  @Provides
  fun provideWatchVideoDelegate(
    @ApplicationContext ctx: Context,
    adsInitializer: AdsInitializer,
    telephonyManager: TelephonyManager,
  ): WatchVideoDelegate {
    val countryIso = telephonyManager.networkCountryIso
    return if (countryIso == "ru") {
      YandexWatchVideoDelegateImpl(
        ctx = ctx,
        adsInitializer = adsInitializer,
        adId = "R-M-12004180-1",
      )
    } else {
      GoogleWatchVideoDelegateImpl(
        ctx = ctx,
        adsInitializer = adsInitializer,
        adId = "ca-app-pub-13311813745114526/6998924862",
      )
    }
  }
}