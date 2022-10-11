package com.example.core.feature.mainmenu


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.auth.SignInController
import com.example.core.feature.mainmenu.market.MarketCardVM
import com.example.core.feature.mainmenu.market.MarketCardVMImpl
import com.example.core.feature.sounds.GameSounds
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainMenuVMImpl @Inject constructor(
  savedStateHandle: SavedStateHandle,
  signInBtnController: SignInController,
  private val gameSounds: GameSounds,

  ) : ViewModel(), MainMenuVM {


  override val marketCardVM: MarketCardVM = MarketCardVMImpl(
    savedStateHandle = savedStateHandle,
    signInBtnController =  signInBtnController,
  )

  override fun playClickSound() {
    gameSounds.playGeneralClick()
  }

  override fun onCleared() {
    super.onCleared()
    marketCardVM.clear()

  }
}