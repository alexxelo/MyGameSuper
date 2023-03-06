package com.first.core.feature.mainmenu.market.signin

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.first.auth.ForActivityResult
import com.first.auth.SignInController
import com.ilyin.tools_android.VMPart
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import timber.log.Timber

class SignInBtnVmImpl constructor(
  savedStateHandle: SavedStateHandle,
  private val signInBtnController: SignInController,
) : SignInBtnVM, VMPart by VMPart.create(
  savedStateHandle = savedStateHandle,
  name = "SignInBtnVmImpl",
) {

  private val _isSignedIn = getLiveData<Boolean>("isSignedIn")
  override val isSignedIn: LiveData<Boolean?> = _isSignedIn

  private val signInWatcher = signInBtnController.onSignInStatusChanged()
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe({
      _isSignedIn.value = it
    }, Timber::e)

  override val signInIntent: Intent
    get() = signInBtnController.intent

  override fun onSignOutClick() {
    signInBtnController.signOut()
  }

  override fun onSignInResult(result: ForActivityResult) {
    signInBtnController.onSignInResult(result)
  }

  override fun clear() {
    signInWatcher.dispose()
  }
}