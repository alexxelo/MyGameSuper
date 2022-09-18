package com.example.auth_google


import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface GoogleSignInFlow {

  val signInIntent: Intent

  fun onSignInStateChanged(): Observable<SignInState>

  fun silentSignIn(ctx: Context): Single<GoogleSignInAccount>

  /**
   * @return true, если юзер после выхода все еще залогинен (аномальное поведение) или false, если юзер после выхода не залогинен (ожидаемое поведение)
   */
  fun signOut(): Single<Boolean>

  fun signIn(account: GoogleSignInAccount)

  fun getLastSignedInAccount(): GoogleSignInAccount?

  sealed class SignInState {

    class SignedIn constructor(val account: GoogleSignInAccount) : SignInState()

    object SignedOut : SignInState()
  }
}