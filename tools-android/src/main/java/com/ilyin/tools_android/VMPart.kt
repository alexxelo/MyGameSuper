package com.ilyin.tools_android

import androidx.lifecycle.MutableLiveData

interface VMPart {
  fun <T> getLiveData(key: String, initialValue: T? = null): MutableLiveData<T>

  fun <T> get(key: String, defaultValue: T? = null): T?

  fun <T> set(key: String, value: T? = null)
}