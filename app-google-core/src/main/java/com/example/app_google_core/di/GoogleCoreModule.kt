package com.example.app_google_core.di

import android.content.Context
import com.example.advertising.AdsInjector
import com.example.advertising_google.GoogleAdsInjector
import com.example.app_google_core.auth.GoogleSignInControllerImpl
import com.example.auth.SignInController
import com.example.auth_google.GoogleSignInFlow
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.games.Games
import com.google.android.play.core.review.ReviewManagerFactory
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
  fun provideAdsInjector(@ApplicationContext ctx: Context): AdsInjector {
    return GoogleAdsInjector(ctx = ctx)
  }


  @Singleton
  @Provides
  fun provideSignInBtnController(googleSignInFlow: GoogleSignInFlow): SignInController {
    return GoogleSignInControllerImpl(
      signInFlow = googleSignInFlow,
    )
  }

}