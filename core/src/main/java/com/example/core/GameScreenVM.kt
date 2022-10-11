package com.example.core

import androidx.lifecycle.LiveData
import com.example.core.feature.game.end.GameEndVM
import com.example.engine2.game.state.GameState
import com.example.engine2.node.NodeElement

interface GameScreenVM {
  fun setGameState(gameState: GameState)
  fun setGameStateEnd()
  fun onGameEnd()
  fun playClickSound()

  val gameState: LiveData<GameState>
  val gameStateMaxNode: LiveData<Int>

  val gameEndVm: GameEndVM


  val showMenu: LiveData<Boolean>
  fun toggleMenu()


}