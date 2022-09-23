package com.example.app_google_core

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.auth_google.GoogleSignInFlow
import com.example.core.AppViewModel
import com.example.core.AppViewModelImpl
import com.example.core.views.GameAppView
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.games.Games
import com.ilyin.localization.AppLanguage
import com.ilyin.localization.LocaleControllerImpl
import com.ilyin.localization.LocalePref
import com.ilyin.tools_android.util.ContextUtils
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class GoogleAppActivity : ComponentActivity() {

  @Inject
  lateinit var signInFlow: GoogleSignInFlow

  private var signInWatcherDisposable: Disposable? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val vm: AppViewModel = hiltViewModel<AppViewModelImpl>()
      GameAppView(
        vm = vm,
        languageChanged = ::requestRestartByLangChanged,
      )
    }
    signInWatcherDisposable = signInFlow.onSignInStateChanged()
      .subscribe(::onSignInStateChanged, Timber::e)
  }

  private fun onSignInStateChanged(signInState: GoogleSignInFlow.SignInState) {
    if (signInState is GoogleSignInFlow.SignInState.SignedIn) {
      onAccountDefined(signInState.account)
    }
  }

  private fun onAccountDefined(account: GoogleSignInAccount) {
    val client = Games.getGamesClient(this, account)
    client.setViewForPopups(findViewById(android.R.id.content))
    client.setGravityForPopups(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
  }

  private fun requestRestartByLangChanged() {
    val intent = Intent(this, GoogleAppActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    startActivity(intent)
  }

  override fun attachBaseContext(newBase: Context) {
    val localeController = LocaleControllerImpl(LocalePref(newBase))
    val defaultLocale = Locale.getDefault()
    localeController.defaultLanguage = AppLanguage.from(defaultLocale)//.from(defaultLocale)
    val currentLang = localeController.currentLanguage
    val languageCode = currentLang.language
    val countryCode = currentLang.countryCode
    if (languageCode.isBlank()) {
      super.attachBaseContext(newBase)
    } else {
      val locale = if (countryCode.isNullOrBlank()) {
        Locale(languageCode)
      } else {
        Locale(languageCode, countryCode)
      }
      val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase,locale)
      super.attachBaseContext(localeUpdatedContext)
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    signInWatcherDisposable?.dispose()
    signInWatcherDisposable = null
  }
}
