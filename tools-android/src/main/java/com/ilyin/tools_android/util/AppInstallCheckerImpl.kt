package com.ilyin.tools_android.util

import android.content.Context

class AppInstallCheckerImpl constructor(private val ctx: Context) : AppInstallChecker {

  override fun isAppInstalled(packageName: String): Boolean {
    return isAppInstalled(ctx, packageName)
  }
}