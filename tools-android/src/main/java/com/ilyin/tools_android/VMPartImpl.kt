package com.ilyin.tools_android

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle

class VMPartImpl constructor(
  private val savedStateHandle: SavedStateHandle,
  private val name: String,
) : VMPart {

  private fun getInnerKey(key: String): String {
    return name + "_" + key
  }

  override fun <T> getLiveData(key: String, initialValue: T?): MutableLiveData<T> {
    return savedStateHandle.getLiveData(getInnerKey(key), initialValue)
  }

  override fun <T> get(key: String, defaultValue: T?): T? {
    return savedStateHandle[getInnerKey(key)] ?: defaultValue
  }

  override fun <T> set(key: String, value: T?) {
    savedStateHandle[getInnerKey(key)] = value
  }
}