package com.first.core

import androidx.lifecycle.LiveData
import com.first.core.feature.game.end.GameEndVM
import com.first.core.feature.game.tip.TipVM
import com.first.core.feature.game.tipshop.TipShopVM
import com.first.engine2.game.state.GameState

interface GameScreenVM {

  val tipShopVm : TipShopVM
  val tipVm : TipVM

  fun setGameState(gameState: GameState)
  fun setGameStateEnd()
  fun onGameEnd()
  fun playClickSound()

  val gameState: LiveData<GameState>
  val gameStateMaxNode: LiveData<Int>

  val gameEndVm: GameEndVM


  val showMenu: LiveData<Boolean>
  val useTip: LiveData<Boolean>

  fun toggleMenu()
  fun useTip()
  fun stopTip ()

  fun dispatchTip()

}