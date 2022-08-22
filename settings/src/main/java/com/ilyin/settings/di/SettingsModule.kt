package com.ilyin.settings.di

import android.content.Context
import com.ilyin.localization.LocalePref
import com.ilyin.settings.feature.hidesystemui.SystemUiHideMemory
import com.ilyin.settings.feature.hidesystemui.SystemUiHideMemoryImpl
import com.ilyin.settings.feature.nightmode.NightModeMemory
import com.ilyin.settings.feature.nightmode.NightModeMemoryImpl
import com.ilyin.settings.feature.sound.SoundEnableMemory
import com.ilyin.settings.feature.sound.SoundEnableMemoryImpl
import com.ilyin.settings.feature.themepicker.SelectedThemeMemory
import com.ilyin.settings.feature.themepicker.SelectedThemeMemoryImpl
import com.ilyin.settings.feature.themepicker.ThemeController
import com.ilyin.settings.feature.themepicker.ThemeControllerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

  @Singleton
  @Provides
  fun provideNightModeMemory(@ApplicationContext ctx: Context): NightModeMemory {
    return NightModeMemoryImpl(
      ctx = ctx,
      defaultValueComputer = { false }
    )
  }

  @Singleton
  @Provides
  fun provideHideSystemUiMemory(@ApplicationContext ctx: Context): SystemUiHideMemory {
    return SystemUiHideMemoryImpl(ctx = ctx)
  }

  @Singleton
  @Provides
  fun selectedThemeMemory(@ApplicationContext ctx: Context): SelectedThemeMemory {
    return SelectedThemeMemoryImpl(ctx = ctx)
  }

  @Singleton
  @Provides
  fun themeController(memory: SelectedThemeMemory): ThemeController {
    return ThemeControllerImpl(memory = memory)
  }

  @Singleton
  @Provides
  fun soundMemory(@ApplicationContext ctx: Context): SoundEnableMemory {
    return SoundEnableMemoryImpl(ctx = ctx)
  }
  @Singleton
  @Provides
  fun provideLocalPref(@ApplicationContext ctx: Context): LocalePref {
    return LocalePref(ctx = ctx)
  }
}