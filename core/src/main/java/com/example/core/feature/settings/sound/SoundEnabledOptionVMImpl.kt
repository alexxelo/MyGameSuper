package com.example.core.feature.settings.sound

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.example.core.feature.sounds.GameSounds
import com.ilyin.settings.feature.sound.SoundEnableMemory
import com.ilyin.tools_android.VMPart

class SoundEnabledOptionVMImpl constructor(
  savedStateHandle: SavedStateHandle,
  private val memory: SoundEnableMemory,
  private val gameSounds: GameSounds,
) : SoundEnabledOptionVM, VMPart by VMPart.create(
  savedStateHandle = savedStateHandle,
  name = "SoundEnabledOptionVMImpl"
) {

  private val _checked = getLiveData("checked", memory.enabled)
  override val checked: LiveData<Boolean> = _checked

  override fun toggle() {
    val newValue = !(_checked.value ?: false)
    memory.enabled = newValue
    _checked.value = newValue
    if (newValue) {
      gameSounds.playGeneralClick()
    }
  }
}