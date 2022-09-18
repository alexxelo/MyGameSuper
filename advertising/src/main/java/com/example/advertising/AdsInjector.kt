package com.example.advertising

import com.example.advertising.interstitial.InterstitialDelegate
import com.example.advertising.watchvideo.WatchVideoDelegate

interface AdsInjector {

  val interstitialDelegate: InterstitialDelegate

  val watchVideoDelegate: WatchVideoDelegate
}