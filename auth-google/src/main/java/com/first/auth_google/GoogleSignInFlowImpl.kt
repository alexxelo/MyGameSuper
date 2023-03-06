package com.first.auth_google

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
class GoogleSignInFlowImpl constructor(
  private val appCtx: Context,
  private val signInOptions: GoogleSignInOptions,
) : GoogleSignInFlow {

  private val signInState: BehaviorSubject<GoogleSignInFlow.SignInState> = BehaviorSubject.createDefault(computeInitialState(appCtx))

  private fun getLoggedAccount(ctx: Context, signInOptions: GoogleSignInOptions = this.signInOptions): GoogleSignInAccount? {
    val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(ctx)
    return account?.takeIf { GoogleSignIn.hasPermissions(it, *signInOptions.scopeArray) }
  }

  private fun computeInitialState(ctx: Context): GoogleSignInFlow.SignInState {
    val loggedInAccount = getLoggedAccount(ctx)
    return if (loggedInAccount == null) {
      GoogleSignInFlow.SignInState.SignedOut
    } else {
      GoogleSignInFlow.SignInState.SignedIn(loggedInAccount)
    }
  }

  override val signInIntent: Intent
    get() = GoogleSignIn.getClient(appCtx, signInOptions).signInIntent

  override fun onSignInStateChanged(): Observable<GoogleSignInFlow.SignInState> {
    return signInState
  }

  override fun silentSignIn(ctx: Context): Single<GoogleSignInAccount> {
    val loggedInAccount = getLoggedAccount(appCtx)
    return if (loggedInAccount != null) {
      Single.just(loggedInAccount)
    } else {
      Single.create { source ->
        val signInClient = GoogleSignIn.getClient(appCtx, signInOptions)
        signInClient.silentSignIn()
          .addOnSuccessListener(source::onSuccess)
          .addOnFailureListener(source::onError)
      }
    }.doOnSuccess {
      signInState.onNext(GoogleSignInFlow.SignInState.SignedIn(it))
    }
  }

  override fun signOut(): Single<Boolean> {
    val signInClient = GoogleSignIn.getClient(appCtx, signInOptions)
    return Single.create<GoogleSignInFlow.SignInState> { source ->
      signInClient.signOut().addOnCompleteListener {
        val loggedAccount: GoogleSignInAccount? = getLoggedAccount(appCtx)
        val state = if (loggedAccount == null) {
          GoogleSignInFlow.SignInState.SignedOut
        } else {
          GoogleSignInFlow.SignInState.SignedIn(loggedAccount)
        }
        source.onSuccess(state)
      }
    }.doOnSuccess { state ->
      signInState.onNext(state)
    }.map { it is GoogleSignInFlow.SignInState.SignedIn }
  }

  override fun signIn(account: GoogleSignInAccount) {
    signInState.onNext(GoogleSignInFlow.SignInState.SignedIn(account))
  }

  override fun getLastSignedInAccount(): GoogleSignInAccount? {
    return getLoggedAccount(appCtx)
  }
}