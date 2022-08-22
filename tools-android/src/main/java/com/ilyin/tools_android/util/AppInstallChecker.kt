package com.ilyin.tools_android.util

interface AppInstallChecker {

  fun isAppInstalled(packageName: String): Boolean
}