package com.ilyin.ui_core_compose

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.res.integerResource
import com.ilyin.ui_core_compose.colors.MdColors
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit


@Composable
fun InfiniteTimer(frameDurationMillis: Long = 16L): Long {
  var currentTime by remember { mutableStateOf(0L) }
  DisposableEffect(key1 = Unit, effect = {
    val disposable = Observable.interval(frameDurationMillis, TimeUnit.MILLISECONDS)
      .subscribe({
        currentTime += frameDurationMillis
      }, {
        Log.e("tag", "")
      })
    this.onDispose {
      disposable.dispose()
    }
  })
  return currentTime
}

@Composable
fun FiniteTimer(frameDurationMillis: Long = 16L, totalDurationMillis: Long = 1000): Long {
  var currentTime by remember { mutableStateOf(0L) }
  DisposableEffect(key1 = Unit, effect = {
    val disposable = Observable.interval(frameDurationMillis, TimeUnit.MILLISECONDS)
      .takeUntil { it * frameDurationMillis < totalDurationMillis }
      .subscribe({
        currentTime += frameDurationMillis
      }, {
        Log.e("tag", "")
      })
    this.onDispose {
      disposable.dispose()
    }
  })
  return currentTime
}

@Composable
fun androidx.compose.material3.MaterialTheme.isDark(): Boolean {
  return colorScheme.error == MdColors.red.c200
}

@Composable
fun androidx.compose.material3.MaterialTheme.isLight(): Boolean {
  return !isDark()
}

@Composable
fun isScreenNormal(): Boolean {
  return !isScreenWide()
}

@Composable
fun isScreenWide(): Boolean {
  return when (integerResource(id = R.integer.screen_type)) {
    integerResource(id = R.integer.screen_type_land),
    integerResource(id = R.integer.screen_type_sw720dp),
    -> true

    integerResource(id = R.integer.screen_type_sw600dp),
    integerResource(id = R.integer.screen_type_normal),
    -> false
    else -> false
  }
}
