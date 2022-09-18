package com.example.core.feature.sounds

import android.content.Context
import android.media.SoundPool
import androidx.core.R

class AtomSoundPoolLoaderImpl constructor(private val ctx: Context, private val soundPool: SoundPool):AtomSoundPoolLoader {
  override fun tryToLoad(): AtomSoundPool {
    return AtomSoundPool(
      newRecord = soundPool.load(ctx, com.example.core.R.raw.new_rec, 0),
      takeElementWithMinus = soundPool.load(ctx, com.example.core.R.raw.take_element, 0),
      turnElementToPlus = soundPool.load(ctx, com.example.core.R.raw.turn_to_plus, 0),
      gameEnd = soundPool.load(ctx, com.example.core.R.raw.general_click, 0),
      generalClick = soundPool.load(ctx, com.example.core.R.raw.game_end, 0)
    )
  }
}