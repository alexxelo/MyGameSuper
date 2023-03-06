package com.first.app_google_core.utils

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

class TaskOrError constructor(val task: Task<GoogleSignInAccount>? = null, val error: Exception? = null)