package com.example.app_google_core.auth

import android.content.Intent
import com.example.app_google_core.utils.GmsUtils
import com.example.app_google_core.utils.TaskOrError
import com.example.auth.ForActivityResult
import com.example.auth.SignInController
import com.example.auth_google.GoogleSignInFlow
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.Module
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber


class GoogleSignInControllerImpl constructor(
  private val signInFlow: GoogleSignInFlow,
) : SignInController {

  override val intent: Intent
    get() = signInFlow.signInIntent

  override fun onSignInResult(result: ForActivityResult) {
    // TODO очень подозрительно. Авторизованный аккаунт уже может появиться, хотя мы не делали GoogleSignIn.getSignedInAccountFromIntent
    val account = signInFlow.getLastSignedInAccount()
    if (account == null) {
      val taskOrError = try {
        TaskOrError(task = GoogleSignIn.getSignedInAccountFromIntent(intent))
      } catch (e: Exception) {
        TaskOrError(error = e)
      }
      GmsUtils.handleTask(
        taskOrError = taskOrError,
        onSuccess = signInFlow::signIn,
        onError = Timber::e,
      )
    } else {
      signInFlow.signIn(account)
    }
  }

  override fun signOut() {
    signInFlow.signOut()
      .ignoreElement()
      .subscribe({}, Timber::e)
  }

  override fun onSignInStatusChanged(): Observable<Boolean> {
    return signInFlow.onSignInStateChanged().map { it is GoogleSignInFlow.SignInState.SignedIn }
  }
}