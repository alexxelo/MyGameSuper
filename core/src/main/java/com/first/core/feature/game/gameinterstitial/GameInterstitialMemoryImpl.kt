package com.first.core.feature.game.gameinterstitial

import android.content.Context
import com.ilyin.tools_android.crud.PreferencesWrapper
import com.ilyin.tools_android.util.prefs

class GameInterstitialMemoryImpl constructor(ctx: Context) : GameInterstitialMemory, PreferencesWrapper(ctx.prefs("GameInterstitialMemoryImpl")) {
  override var lastAdShowMillis: Long? by LongPrefNullable(this, "lastAdShowMillis")

}