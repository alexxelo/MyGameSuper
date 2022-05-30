package com.example.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.engine2.game.state.GameState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameScreenVMImpl @Inject constructor(savedStateHandle: SavedStateHandle) : GameScreenVM, ViewModel() {

  private val _gameState = savedStateHandle.getLiveData<GameState>("gs", GameState.createInitial())

  override val gameState: LiveData<GameState> = _gameState

  override fun setGameState(gameState: GameState) {
    _gameState.value = gameState
  }

}