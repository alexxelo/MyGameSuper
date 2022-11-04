package com.example.core.feature.tipshop.free.ads

import android.app.Activity
import androidx.lifecycle.LiveData
import com.example.advertising.watchvideo.VideoAdState

interface TipsForAdsVM {

  val adState: LiveData<VideoAdState>

  fun requestReload()

  fun watch(activity: Activity)

  fun onCleared()

  companion object {

    const val TIPS_FOR_AD = 1
  }
}