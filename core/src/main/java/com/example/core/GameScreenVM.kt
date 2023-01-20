package com.example.core

import androidx.lifecycle.LiveData
import com.example.core.feature.game.end.GameEndVM
import com.example.core.feature.tip.TipVM
import com.example.core.feature.tipshop.TipShopVM
import com.example.engine2.game.state.GameState
import com.example.engine2.node.NodeElement

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