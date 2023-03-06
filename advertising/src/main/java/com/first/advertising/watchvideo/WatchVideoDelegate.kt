package com.first.advertising.watchvideo

import android.app.Activity
import io.reactivex.rxjava3.core.Observable

interface WatchVideoDelegate {

  fun videoAdState(): Observable<VideoAdState>

  fun onWatched(): Observable<Unit>

  fun release()

  fun watch(activity: Activity)

  fun reload()

  fun isLoaded(): Boolean

  fun isLoading(): Boolean
}