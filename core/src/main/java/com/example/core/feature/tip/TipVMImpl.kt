package com.example.core.feature.tip

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.ilyin.tools_android.VMPart
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import timber.log.Timber

class TipVMImpl constructor(
  savedStateHandle: SavedStateHandle,
  private val tipMemory: TipMemory,
  override val enableToUse: Boolean
) : TipVM, VMPart by VMPart.create(
  savedStateHandle = savedStateHandle,
  name = "TipVMImpl"
){
  private val _tips = getLiveData("hints", tipMemory.tips)

  override val tips: LiveData<Int> = _tips


  private val tipsWatcher = tipMemory.onTipsChanged
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe({
      _tips.value = it
    }, Timber::e)

  override fun addTips(tips: Int) {
    tipMemory.addTip(tips)
  }

  override fun onCleared() {
    tipsWatcher.dispose()
  }
}