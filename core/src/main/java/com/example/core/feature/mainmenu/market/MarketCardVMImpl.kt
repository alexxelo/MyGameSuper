package com.example.core.feature.mainmenu.market

import androidx.lifecycle.SavedStateHandle
import com.example.auth.SignInController
import com.example.core.feature.mainmenu.market.signin.SignInBtnVM
import com.example.core.feature.mainmenu.market.signin.SignInBtnVmImpl
import com.ilyin.tools_android.VMPart

class MarketCardVMImpl constructor(
  savedStateHandle: SavedStateHandle,
  signInBtnController: SignInController,
) : MarketCardVM, VMPart by VMPart.create(
  savedStateHandle = savedStateHandle,
  name = "MarketCardVMImpl",
) {

  override val signInBtnVm: SignInBtnVM = SignInBtnVmImpl(
    savedStateHandle = savedStateHandle,
    signInBtnController = signInBtnController,
  )
  override fun clear() {
    signInBtnVm.clear()
  }
}