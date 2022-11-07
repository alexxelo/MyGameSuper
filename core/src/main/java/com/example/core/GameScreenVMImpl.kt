package com.example.core

import androidx.lifecycle.*
import com.example.advertising.watchvideo.WatchVideoDelegate
import com.example.core.feature.game.end.GameEndVM
import com.example.core.feature.game.end.GameEndVMImpl
import com.example.core.feature.game.interstitial.GameInterstitialController
import com.example.core.feature.memory.GameStateMemory
import com.example.core.feature.sounds.GameSounds
import com.example.core.feature.tip.TipMemory
import com.example.core.feature.tip.TipVM
import com.example.core.feature.tip.TipVMImpl
import com.example.core.feature.tipshop.TipShopVM
import com.example.core.feature.tipshop.TipShopVMImpl
import com.example.core.feature.tipshop.free.pereodical.FreePeriodicalMemory
import com.example.engine2.game.state.GameState
import com.example.engine2.node.NodeElement
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameScreenVMImpl @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val gameSounds: GameSounds,
  private val gameStateMemory: GameStateMemory,
  tipMemory: TipMemory,
  tipPeriodicalMemory: FreePeriodicalMemory,
  watchVideoDelegate: WatchVideoDelegate,
  gameInterstitialController: GameInterstitialController,

) : GameScreenVM, ViewModel() {


  private val _gameState: MutableLiveData<GameState>
  override val gameState: LiveData<GameState>

  private val _gameStateMaxNode: MutableLiveData<Int>
  override val gameStateMaxNode: LiveData<Int>

  private val _showMenu: MutableLiveData<Boolean> = savedStateHandle.getLiveData<Boolean>("sm", false)
  override val showMenu: LiveData<Boolean> = _showMenu

  override fun toggleMenu() {
    gameSounds.playGeneralClick()
    _showMenu.value = showMenu.value == false
  }

  //private val enableToUseMediator: MediatorLiveData<Boolean> = MediatorLiveData()

  override val tipShopVm: TipShopVM = TipShopVMImpl(
    savedStateHandle = savedStateHandle,
    tipMemory = tipMemory,
    tipPeriodicalMemory = tipPeriodicalMemory,
    gameSounds = gameSounds,
    watchVideoDelegate = watchVideoDelegate,
    gameInterstitialController = gameInterstitialController
  )

  override val tipVm: TipVM = TipVMImpl(
    savedStateHandle = savedStateHandle,
    tipMemory = tipMemory,
    enableToUse = true
  )


  override fun dispatchTip() {
    /*
    val gameState: GameState = _gameStates.value?.last() ?: return
    val newGameState = gameState.deepCopy().apply {
      this.dispatchTip()
    }*/

   // setGameState(newGameState)
  }

  init {

    val game = gameStateMemory.gameState ?: GameState.createGame()
    _gameState = savedStateHandle.getLiveData<GameState>("gs", game)
    gameState = _gameState


    _gameStateMaxNode = savedStateHandle.getLiveData("mn", game.recordAtomicMass)


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
    gameStateMemory.gameState = null

    _gameState.value = gameState
    gameStateMemory.gameState = _gameState.value
  }

  override fun setGameStateEnd() {
    gameStateMemory.gameState = null
  }

  override fun onGameEnd() {
    gameSounds.playGameEnd()
  }

  override val gameEndVm: GameEndVM = GameEndVMImpl(
    savedStateHandle = savedStateHandle,
    gameSounds = gameSounds
  )

  override fun playClickSound() {
    gameSounds.playGeneralClick()
  }

  override fun onCleared() {
    super.onCleared()
    //gameStateMemory.gameState = _gameState.value
  }
}
