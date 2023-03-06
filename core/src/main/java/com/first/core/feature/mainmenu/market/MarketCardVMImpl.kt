package com.first.core.feature.mainmenu.market

import androidx.lifecycle.SavedStateHandle
import com.first.auth.SignInController
import com.first.core.feature.mainmenu.market.signin.SignInBtnVM
import com.first.core.feature.mainmenu.market.signin.SignInBtnVmImpl
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