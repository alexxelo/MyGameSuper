package com.first.core.feature.sounds

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
    if (enabled) {
      soundPool.play(atomSoundPool.newRecord, volume, volume * 2, 1, 0, 1f)
    }
  }

  override fun playTakeElementWithMinus() {
    if (enabled) {
      soundPool.play(atomSoundPool.takeElementWithMinus, volume, volume * 2, 1, 0, 1f)
    }
  }

  override fun playGameEnd() {
    if (enabled) {
      soundPool.play(atomSoundPool.gameEnd, volume, volume * 2, 1, 0, 1f)
    }
  }

  override fun playGeneralClick() {
    if (enabled) {
      soundPool.play(atomSoundPool.generalClick, volume, volume * 2, 1, 0, 1f)
    }
  }

  override fun playTurnElementToPlus() {
    if (enabled) {
      soundPool.play(atomSoundPool.turnElementToPlus, volume, volume * 2, 1, 0, 1f)
    }
  }

  override fun dispose() {
    enableWatcher.dispose()
  }


}