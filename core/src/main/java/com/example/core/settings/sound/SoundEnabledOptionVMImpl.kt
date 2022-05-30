package com.ilyinp.core.feature.settings.sound

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.ilyin.settings.feature.sound.SoundEnableMemory
import com.ilyin.tools_android.VMPart
import com.ilyin.tools_android.VMPartImpl
import com.ilyinp.core.sounds.GameSounds

class SoundEnabledOptionVMImpl constructor(
  savedStateHandle: SavedStateHandle,
  private val memory: SoundEnableMemory,
  private val gameSounds: GameSounds,
) : SoundEnabledOptionVM, VMPart by VMPartImpl(
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
      gameSounds.playGeneralInterfaceClick()
    }
  }
}