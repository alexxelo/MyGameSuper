package com.example.mygame

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GameApp: Application() {

  override fun onCreate() {
    super.onCreate()
  }

}