package com.example.core

import androidx.lifecycle.LiveData
import com.example.engine2.game.state.GameState
import com.example.engine2.node.NodeElement

interface GameScreenVM {
  fun setGameState(gameState: GameState)

  val gameState: LiveData<GameState>
  val gameStateMaxNode: LiveData<Int>

  val showMenu: LiveData<Boolean>
  fun toggleMenu()


}