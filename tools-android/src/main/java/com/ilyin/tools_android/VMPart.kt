package com.ilyin.tools_android

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle

interface VMPart {
  fun <T> getLiveData(key: String): MutableLiveData<T?>

  fun <T> getLiveData(key: String, initialValue: T): MutableLiveData<T>

  fun <T> get(key: String, defaultValue: T? = null): T?

  fun <T> set(key: String, value: T? = null)

  companion object {

    fun create(
      savedStateHandle: SavedStateHandle,
      name: String,
    ): VMPart {
      return VMPartImpl(
        savedStateHandle = savedStateHandle,
        name = name,
      )
    }
  }
}