package com.ilyin.settings.feature.hidesystemui

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.ilyin.tools_android.VMPart
import com.ilyin.tools_android.VMPartImpl

class HideSystemUiSettingVMImpl constructor(
  savedStateHandle: SavedStateHandle,
  private val memory: SystemUiHideMemory,
  private val onToggle: () -> Unit = {},
) : HideSystemUiSettingVM, VMPart by VMPartImpl(
  savedStateHandle = savedStateHandle,
  name = "NightModeSettingVMImpl",
) {

  private val _checked = getLiveData("checked", memory.isHidden)
  override val checked: LiveData<Boolean> = _checked

  override fun toggle() {
    val newValue = !(checked.value ?: false)
    memory.isHidden = newValue
    _checked.value = newValue
    onToggle()
  }
}