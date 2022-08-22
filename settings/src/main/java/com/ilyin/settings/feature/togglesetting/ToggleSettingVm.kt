package com.ilyin.settings.feature.togglesetting

import androidx.lifecycle.LiveData

interface ToggleSettingVm {

  val checked: LiveData<Boolean>
  val enabled: LiveData<Boolean>
    get() = object : LiveData<Boolean>(true) {}

  fun toggle()
}