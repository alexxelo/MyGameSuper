package com.first.advertising_google

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

object AdManager {

  private val TEST_DEVICES_IDS = listOf(
//    "89E1A7F627CF00718166815323289096",
//    "35C630D1329351995443CF738FD7F646",
//    "9D178FE5F87DA91493E0FE3D2750C258",
//    "02DB3BB25E7679E3792AF9D78EBD53FC",
    "17F210B06B3CB56365CE9EA20EA7BE72", // Redmi Note 8T
    //"E886ADC26C1DF8073E96E0FD77AB89B1", // Samsung N-900
    //"5CE7DECFF047C92897BED6DEA858A345", // Samsung Galaxy 21+
  )

  fun testBuilder(): AdRequest.Builder {
    val requestConfig = RequestConfiguration.Builder()
      .setTestDeviceIds(TEST_DEVICES_IDS)
      .build()
    MobileAds.setRequestConfiguration(requestConfig)
    return AdRequest.Builder()
  }

  fun adRequest(): AdRequest {
    return testBuilder().build()
  }
}
