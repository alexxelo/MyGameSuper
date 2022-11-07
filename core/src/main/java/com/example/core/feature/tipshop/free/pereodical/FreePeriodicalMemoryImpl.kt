package com.example.core.feature.tipshop.free.pereodical

import android.content.Context
import com.ilyin.tools_android.crud.PreferencesWrapper
import com.ilyin.tools_android.util.prefs

class FreePeriodicalMemoryImpl constructor(ctx:Context): FreePeriodicalMemory, PreferencesWrapper(ctx.prefs("FreePeriodicalMemoryImpl")){
  override var lastRewardTimeMillis: Long by LongPref(this, "lastSeenTimeMillis", -1)
}