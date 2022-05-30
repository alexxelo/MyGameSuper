package com.example.core.di

import android.content.Context
import android.media.SoundPool
import com.ilyin.localization.LocaleController
import com.ilyin.localization.LocaleControllerImpl
import com.ilyin.localization.LocalePref
import com.ilyin.settings.feature.nightmode.NightModeMemory
import com.ilyin.settings.feature.nightmode.NightModeMemoryImpl
import com.ilyin.settings.feature.sound.SoundEnableMemory
import com.ilyinp.core.sounds.GameSounds
import com.ilyinp.core.sounds.GameSoundsImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

  @Singleton
  @Provides
  fun provideGameSounds( soundEnableMemory: SoundEnableMemory): GameSounds {
    return GameSoundsImpl(
      soundMemory = soundEnableMemory,
      soundPool = SoundPool.Builder().setMaxStreams(5).build()
    )
  }

  @Singleton
  @Provides
  fun provideLocaleController( localePref: LocalePref): LocaleController {
    return LocaleControllerImpl(
      memory = localePref
    )
  }

}