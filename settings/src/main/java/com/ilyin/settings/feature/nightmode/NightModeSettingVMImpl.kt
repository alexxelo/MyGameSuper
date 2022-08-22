package com.ilyin.settings.feature.nightmode

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.ilyin.tools_android.VMPart
import com.ilyin.tools_android.VMPartImpl

class NightModeSettingVMImpl constructor(
  savedStateHandle: SavedStateHandle,
  private val memory: NightModeMemory,
  private val onToggle: () -> Unit = {},
) : NightModeSettingVM, VMPart by VMPartImpl(
  savedStateHandle = savedStateHandle,
  name = "NightModeSettingVMImpl",
) {

  private val _checked = getLiveData("_checked", memory.isNightMode)
  override val checked: LiveData<Boolean> = _checked

  override fun toggle() {
    val newValue = !(checked.value ?: false)
    memory.isNightMode = newValue
    _checked.value = newValue
    onToggle()
  }
}