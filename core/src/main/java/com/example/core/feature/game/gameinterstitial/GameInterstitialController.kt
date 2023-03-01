package com.example.core.feature.game.gameinterstitial

import android.telephony.TelephonyManager

class GameInterstitialController constructor(
  private val interstitialMemory: GameInterstitialMemory,
  private val telephonyManager: TelephonyManager,

  ) : GameInterstitialMemory by interstitialMemory {

  fun interstitialIsEnabled(): Boolean {
    val countryIso = telephonyManager.networkCountryIso
    return !COUNTRIES_WHERE_INTERSTITIAL_IS_DISABLED.contains(countryIso.uppercase())
  }

  fun needToShow(): Boolean {
    if (!interstitialIsEnabled()) return false
    val lastAdShow = interstitialMemory.lastAdShowMillis ?: return true
    if (System.currentTimeMillis() - lastAdShow > 2 * 60 * 1000) return true
    return false
  }

  fun markAdShown() {
    lastAdShowMillis = System.currentTimeMillis()
  }

  companion object {
    //private val DISABLED_BECAUSE_HIGH_INCOME = listOf("US", "JP", "KR", "FR", "GB")
    //private val DISABLED_BECAUSE_WANT_BE_FRIENDS = listOf("CA", "CZ", "BE", "IN", "FI", "DE", "DK", "SE", "CH", "AT", "NO", "IT", "NL")
    private val DISABLED_BECAUSE_HIGH_INCOME: List<String> = listOf()
    private val DISABLED_BECAUSE_WANT_BE_FRIENDS: List<String> = listOf()
    private val COUNTRIES_WHERE_INTERSTITIAL_IS_DISABLED: List<String> = DISABLED_BECAUSE_HIGH_INCOME + DISABLED_BECAUSE_WANT_BE_FRIENDS
  }
}