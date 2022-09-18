package com.example.rate

import android.app.Activity
import io.reactivex.rxjava3.core.Single

interface RateManager {
  fun rate(activity: Activity): Single<Boolean>
}