package com.first.mygame

import android.app.Application
import com.google.firebase.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class GameApp: Application() {

  override fun onCreate() {
    super.onCreate()
    setTimber()
  }
  private fun setTimber() {
    val timberTree = if (BuildConfig.DEBUG) {
      Timber.DebugTree()
    } else {
      Timber.DebugTree()
      //CrashlyticsTree()
    }
    Timber.plant(timberTree)
  }

}