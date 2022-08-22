package com.ilyin.tools_android.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.StringRes
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random


fun osIs(vararg versions: Int): Boolean {
  val version = Build.VERSION.SDK_INT
  return version in versions
}

fun isAppInstalled(ctx: Context, uri: String): Boolean {
  val pm = ctx.packageManager
  return try {
    pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
    true
  } catch (e: PackageManager.NameNotFoundException) {
    false
  } catch (e: Exception) {
    false
  }
}

fun goToGooglePlayIntent(packageName: String): Intent {
  return try {
    Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
  } catch (e: ActivityNotFoundException) {
    Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
  }
}

fun goToGooglePlay(ctx: Context, packageName: String) {
  try {
    val toMarketAppIntent = Uri.parse("market://details?id=$packageName")
    ctx.startActivity(Intent(Intent.ACTION_VIEW, toMarketAppIntent))
  } catch (e: ActivityNotFoundException) {
    val toMarketWebsiteIntent = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
    ctx.startActivity(Intent(Intent.ACTION_VIEW, toMarketWebsiteIntent))
  }
}

fun goToWebSite(ctx: Context, address: String) {
  try {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(address))
    ctx.startActivity(intent)
  } catch (e: ActivityNotFoundException) {
    toast(ctx, "Install any web-browser to open this link")
  }
}

fun Context.prefs(name: String): SharedPreferences {
  return getSharedPreferences(name, Context.MODE_PRIVATE)
}

fun <T : Activity> T.takeIfRunning(): T? {
  return takeIf { it.isActive() }
}

fun Activity.isActive(): Boolean {
  return !isDestroyed && !isFinishing
}

fun toastL(ctx: Context, text: CharSequence?) {
  if (ctx is Activity && !ctx.isActive()) {
    return
  }
  if (text.isNullOrBlank()) {
    return
  }
  Toast.makeText(ctx, text, Toast.LENGTH_LONG).show()
}

fun toast(ctx: Context, text: CharSequence?) {
  if (ctx is Activity && !ctx.isActive()) {
    return
  }
  if (text.isNullOrBlank()) {
    return
  }
  Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show()
}

fun toast(ctx: Context, @StringRes id: Int) {
  toast(ctx, ctx.getString(id))
}

fun toastL(ctx: Context, @StringRes id: Int) {
  toastL(ctx, ctx.getString(id))
}

fun <T> MutableList<T>.removeRandomOrNull(random: Random = Random): T? {
  return if (isEmpty()) {
    null
  } else {
    val randomElement = random(random)
    remove(randomElement)
    randomElement
  }
}

fun <T> MutableList<T>.removeRandom(random: Random = Random): T {
  return removeRandomOrNull(random)!!
}

fun <T>JSONArray.map(func: (Int, JSONArray) -> T): List<T> {
  return (0 until this.length()).map {
    func(it, this)
  }
}