package com.example.core.feature.sounds

import android.media.SoundPool
import com.ilyin.settings.feature.sound.SoundEnableMemory
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

class GameSoundsImpl constructor(
  private val soundMemory: SoundEnableMemory,
  private val soundPoolLoader: AtomSoundPoolLoader,
  private val soundPool: SoundPool,
) : GameSounds {
  private val volume = 0.5f

  private var atomSoundPool: AtomSoundPool = AtomSoundPool.fake()


  private val enableWatcher = soundMemory.isEnabledObservable
    .observeOn(Schedulers.io())
    .subscribe({ isEnabled ->
      if (isEnabled && atomSoundPool.isFake) {
        atomSoundPool = soundPoolLoader.tryToLoad()
      }
    }, Timber::e)

  private val enabled: Boolean
    get() = soundMemory.enabled


  override fun playNewRecord() {
    TODO("Not yet implemented")
  }

  override fun playTakeElementWithMinus() {
    TODO("Not yet implemented")
  }

  override fun playGameEnd() {
    TODO("Not yet implemented")
  }

  override fun playGeneralClick() {
    TODO("Not yet implemented")
  }

  override fun playTurnElementToPlus() {
    TODO("Not yet implemented")
  }

  override fun dispose() {
    TODO("Not yet implemented")
  }


}