package com.ilyin.settings.feature.hidesystemui

import io.reactivex.rxjava3.core.Observable

interface SystemUiHideMemory {

  var isHidden: Boolean

  val isHiddenObservable: Observable<Boolean>
}