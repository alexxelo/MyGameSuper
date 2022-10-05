package com.example.core

import androidx.lifecycle.*
import com.example.core.feature.memory.GameStateMemory
import com.example.engine2.game.state.GameState
import com.example.engine2.node.NodeElement
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameScreenVMImpl @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val gameStateMemory: GameStateMemory
) : GameScreenVM, ViewModel() {


  private val _gameState: MutableLiveData<GameState>
  override val gameState: LiveData<GameState>

  private val _gameStateMaxNode: MutableLiveData<Int>
  override val gameStateMaxNode: LiveData<Int>

  private val _showMenu: MutableLiveData<Boolean> = savedStateHandle.getLiveData<Boolean>("sm", false)
  override val showMenu: LiveData<Boolean> = _showMenu

  override fun toggleMenu() {
    _showMenu.value = showMenu.value == false
  }

  init {

    val game = gameStateMemory.gameState ?: GameState.createGame()
    _gameState = savedStateHandle.getLiveData<GameState>("gs", game)
    gameState = _gameState


      _gameStateMaxNode = savedStateHandle.getLiveData("mn", game.recordAtomicMass)


    //getLiveData("gsmn", game.findMaxNode())
    gameStateMaxNode = _gameStateMaxNode

    _gameState.observeForever {
      val oldMaxNode = _gameStateMaxNode.value
      val newMaxNode = it.recordAtomicMass
      if (oldMaxNode != null) {

        if (oldMaxNode < newMaxNode) {
          _gameStateMaxNode.value = newMaxNode

        }
      }
    }
  }

  override fun setGameState(gameState: GameState) {
    _gameState.value = gameState
    gameStateMemory.gameState = _gameState.value
  }

  override fun onCleared() {
    super.onCleared()
    gameStateMemory.gameState = _gameState.value
  }
}