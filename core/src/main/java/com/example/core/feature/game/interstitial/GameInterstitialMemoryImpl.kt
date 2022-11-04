package com.example.core.feature.game.interstitial

import android.content.Context
import android.content.SharedPreferences
import com.ilyin.tools_android.crud.PreferencesWrapper
import com.ilyin.tools_android.util.prefs

class GameInterstitialMemoryImpl constructor(ctx: Context) : GameInterstitialMemory, PreferencesWrapper(ctx.prefs("GameInterstitialMemoryImpl")) {
  override var lastAdShowMillis: Long? by PreferencesWrapper.LongPrefNullable(this, "lastAdShowMillis")

}