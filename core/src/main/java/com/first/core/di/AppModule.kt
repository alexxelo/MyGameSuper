package com.first.core.di

import android.content.Context
import android.media.SoundPool
import android.telephony.TelephonyManager
import com.ilyin.localization.LocaleController
import com.ilyin.localization.LocaleControllerImpl
import com.ilyin.localization.LocalePref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

  @Singleton
  @Provides
  fun provideLocaleMemory(@ApplicationContext ctx: Context): LocalePref {
    return LocalePref(ctx = ctx)
  }

  @Singleton
  @Provides
  fun provideLocaleController(memory: LocalePref): LocaleController {
    return LocaleControllerImpl(memory = memory)
  }

  @Singleton
  @Provides
  fun provideSoundPool(): SoundPool {
    return SoundPool.Builder().setMaxStreams(5).build()
  }
/*
  @Singleton
  @Provides
  fun provideAppEnterSessionMemory(@ApplicationContext ctx: Context): AppEnterCounter {
    return AppEnterCounter(ctx = ctx)
  }*/

  @Singleton
  @Provides
  fun provideTelephonyManager(@ApplicationContext ctx: Context): TelephonyManager {
    return ctx.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
  }

}