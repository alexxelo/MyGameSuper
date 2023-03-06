package com.first.core

import androidx.lifecycle.LiveData
import com.ilyin.ui_core_compose.themes.AppTheme

interface AppViewModel {
  val nightMode: LiveData<Boolean>
  val hideSystemUi: LiveData<Boolean>
  val selectedTheme: LiveData<AppTheme>

}