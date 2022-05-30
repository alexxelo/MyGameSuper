package com.ilyinp.core.sounds

import android.media.SoundPool
import com.ilyin.settings.feature.sound.SoundEnableMemory
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

class GameSoundsImpl constructor(
  private val soundMemory: SoundEnableMemory,
  private val soundPool: SoundPool,
) : GameSounds {

  private var geoSoundPool: GeoSoundPool = GeoSoundPool.fake()


  private val enabled: Boolean
    get() = soundMemory.enabled

  private val enableWatcher = soundMemory.isEnabledObservable
    .observeOn(Schedulers.io())
    .subscribe({ isEnabled ->
      if (isEnabled && geoSoundPool.isFake) {
        geoSoundPool = GeoSoundPool()
      }
    }, Timber::e)


  override fun playGeneralInterfaceClick() {
    if (enabled) {
      soundPool.play(geoSoundPool.uiGeneralClick, 1f, 1f, 1, 0, 1f)
    }
  }


  override fun dispose() {
    enableWatcher.dispose()
  }
}