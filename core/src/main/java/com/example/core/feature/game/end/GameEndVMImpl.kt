package com.example.core.feature.game.end

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.example.core.feature.sounds.GameSounds
import com.ilyin.tools_android.VMPart

class GameEndVMImpl constructor(
  savedStateHandle: SavedStateHandle,
  private val gameSounds: GameSounds,

  ) : GameEndVM, VMPart by VMPart.create(
  savedStateHandle = savedStateHandle,
  name = "GameEndVMImpl",
) {


  private val _isShow = getLiveData("isShow", false)
  override val isShow: LiveData<Boolean> = _isShow

  override fun show() {

  }

  override fun playClick() {
    gameSounds.playGeneralClick()
  }
}