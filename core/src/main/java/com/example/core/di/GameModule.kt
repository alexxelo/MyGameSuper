package com.example.core.di

import android.content.Context
import android.media.SoundPool
import com.example.core.feature.memory.GameStateMemory
import com.example.core.feature.memory.GameStateMemoryImpl
import com.example.core.feature.sounds.AtomSoundPoolLoader
import com.example.core.feature.sounds.AtomSoundPoolLoaderImpl
import com.example.core.feature.sounds.GameSounds
import com.example.core.feature.sounds.GameSoundsImpl
import com.ilyin.settings.feature.sound.SoundEnableMemory
import com.ilyin.tools_android.filesystem.AndroidAppFileSystem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GameModule {

  @Singleton
  @Provides
  fun provideSoundPoolLoader(@ApplicationContext ctx: Context, soundPool: SoundPool): AtomSoundPoolLoader {
    return AtomSoundPoolLoaderImpl(ctx = ctx, soundPool = soundPool)
  }
  @Singleton
  @Provides
  fun provideGameStateMemory(@ApplicationContext ctx: Context): GameStateMemory {
    return GameStateMemoryImpl(context = ctx)
  }

  @Singleton
  @Provides
  fun provideGameSounds(
    soundMemory: SoundEnableMemory,
    soundPoolLoader: AtomSoundPoolLoader,
    soundPool: SoundPool,
  ): GameSounds {
    return GameSoundsImpl(
      soundMemory = soundMemory,
      soundPoolLoader = soundPoolLoader,
      soundPool = soundPool,
    )
  }
/*
  @Singleton
  @Provides
  fun provideGameSettingsMemory(@ApplicationContext ctx: Context): GameSettingsMemory {
    return GameSettingsMemoryImpl(
      ctx = ctx,
      defaultGameSettingsComputer = {
        GameSettings.defaultSort(listOf(Regions.EUROPE))
      },
    )
  }*/


  @Singleton
  @Provides
  fun provideAndroidFileSystem(@ApplicationContext ctx: Context): AndroidAppFileSystem {
    return AndroidAppFileSystem(ctx = ctx)
  }

}