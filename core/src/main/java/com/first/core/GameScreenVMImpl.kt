package com.first.core

import androidx.lifecycle.*
import com.first.advertising.watchvideo.WatchVideoDelegate
import com.first.core.feature.game.end.GameEndVM
import com.first.core.feature.game.end.GameEndVMImpl
import com.first.core.feature.game.gameinterstitial.GameInterstitialController
import com.first.core.feature.memory.GameStateMemory
import com.first.core.feature.sounds.GameSounds
import com.first.core.feature.game.tip.TipMemory
import com.first.core.feature.game.tip.TipVM
import com.first.core.feature.game.tip.TipVMImpl
import com.first.core.feature.game.tipshop.TipShopVM
import com.first.core.feature.game.tipshop.TipShopVMImpl
import com.first.core.feature.game.tipshop.free.pereodical.FreePeriodicalMemory
import com.first.engine2.game.state.GameState
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

  private val _useTip: MutableLiveData<Boolean> = savedStateHandle.getLiveData<Boolean>("ut", false)
  override val useTip: LiveData<Boolean> = _useTip

  override fun toggleMenu() {
    gameSounds.playGeneralClick()
    _showMenu.value = showMenu.value == false
  }

  override fun useTip() {
    gameSounds.playGeneralClick()
    _useTip.value = useTip.value == true

    //gameState.value?()
  }
  override fun stopTip() {
    gameSounds.playGeneralClick()
    _useTip.value = useTip.value == false
  }

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

    //gameState.value?.useTip()

  }

  init {

    val game = gameStateMemory.gameState ?: GameState.createGame()
    _gameState = savedStateHandle.getLiveData<GameState>("gs", game)
    gameState = _gameState

    //game.activeNode
    //gameState.value.activeNode

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
