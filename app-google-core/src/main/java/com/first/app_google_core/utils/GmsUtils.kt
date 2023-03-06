package com.first.app_google_core.utils

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException

object GmsUtils {

  fun handleTask(taskOrError: TaskOrError, onSuccess: (GoogleSignInAccount) -> Unit, onError: (Exception) -> Unit) {
    val task = taskOrError.task
    val error = taskOrError.error
    if (task != null) {
      try {
        val account = task.getResult(ApiException::class.java)
        onSuccess(account)
      } catch (e: Exception) {
        onError(e)
      }
    } else if (error != null) {
      onError(error)
    } else {
      onError(RuntimeException("GoogleSignIn UnknownError"))
    }
  }
}