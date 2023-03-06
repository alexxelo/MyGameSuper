package com.first.auth_google.di

import android.content.Context
import com.first.auth_google.GoogleSignInFlow
import com.first.auth_google.GoogleSignInFlowImpl
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

  @Singleton
  @Provides
  fun provideGoogleSignInFlow(@ApplicationContext ctx: Context, signInOptions: GoogleSignInOptions): GoogleSignInFlow {
    return GoogleSignInFlowImpl(
      appCtx = ctx,
      signInOptions = signInOptions,
    )
  }
}