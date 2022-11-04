package com.example.core.utils

import android.content.res.Configuration
import android.icu.text.MeasureFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import timber.log.Timber
import java.util.*

object CoreViewUtils {

  const val LOCALE = "de"
  const val UI_MODE = Configuration.UI_MODE_NIGHT_NO


  @RequiresApi(Build.VERSION_CODES.N)
  fun computeAreaFormatter(locale: java.util.Locale = java.util.Locale.getDefault()): MeasureFormat {
    return MeasureFormat.getInstance(locale, MeasureFormat.FormatWidth.SHORT, android.icu.text.NumberFormat.getInstance(locale))
  }
}

@Composable
fun <T> measureTimeMillisReturnableComposable(tag: String, block: @Composable () -> T): T {
  val start = System.currentTimeMillis()
  val toReturn = block()
  Timber.d(tag + " ${System.currentTimeMillis() - start}")
  return toReturn
}

@Composable
fun <T> measureTimeNanoReturnableComposable(tag: String, block: @Composable () -> T): T {
  val start = System.nanoTime()
  val toReturn = block()
  Timber.d(tag + " ${System.nanoTime() - start}")
  return toReturn
}

@Composable
fun getDefaultLocaleCompose(): java.util.Locale {
  return java.util.Locale(Locale.current.language)
}

val roundedCorner8dp = RoundedCornerShape(8.dp)