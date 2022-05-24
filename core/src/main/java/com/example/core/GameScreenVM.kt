package com.example.core

import androidx.lifecycle.LiveData
import com.example.engine2.game.state.GameState

interface GameScreenVM {
  val gameState: LiveData<GameState>
}