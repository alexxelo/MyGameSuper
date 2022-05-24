package com.example.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.engine2.game.state.GameState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameScreenVMImpl @Inject constructor(savedStateHandle: SavedStateHandle) : GameScreenVM, ViewModel() {
  override val gameState: LiveData<GameState>
    get() = TODO("Not yet implemented")
}