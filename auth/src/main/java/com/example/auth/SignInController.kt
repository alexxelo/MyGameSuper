package com.example.auth

import android.content.Intent
import io.reactivex.rxjava3.core.Observable


interface SignInController {

  val intent: Intent

  fun signOut()

  fun onSignInResult(result: ForActivityResult)

  fun onSignInStatusChanged(): Observable<Boolean>
}