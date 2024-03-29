package com.first.core.feature.mainmenu.market.signin

import android.content.Intent
import androidx.lifecycle.LiveData
import com.first.auth.ForActivityResult

interface SignInBtnVM {

  val isSignedIn: LiveData<Boolean?>
  val signInIntent: Intent

  fun onSignOutClick()
  fun onSignInResult(result: ForActivityResult)

  fun clear()
}