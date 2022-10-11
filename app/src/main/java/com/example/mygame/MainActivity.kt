package com.example.mygame
// can be deleted
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.*
import com.example.core.GameAppView
import com.example.mygame.ui.theme.MyGameTheme
import com.ilyin.localization.LocaleControllerImpl
import com.ilyin.localization.LocalePref
import com.ilyin.tools_android.util.ContextUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val vm: AppViewModel = viewModel<AppViewModelImpl>()
      GameAppView(modifier = Modifier, vm = vm, languageChanged = { requestRestartByLangChanged() })
    }
  }


  override fun attachBaseContext(newBase: Context) {
    val localeController = LocaleControllerImpl(LocalePref(newBase))
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
      val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, locale)
      super.attachBaseContext(localeUpdatedContext)
    }
  }

  private fun requestRestartByLangChanged() {
    val intent = Intent(this, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    startActivity(intent)
  }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  MyGameTheme {
  }
}