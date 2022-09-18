package com.example.advertising

import io.reactivex.rxjava3.core.Single

interface AdsInitializer {

  fun initRx(): Single<Boolean>
  fun init()

  val isInitialized: Boolean
}