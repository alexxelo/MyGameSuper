package com.example.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.core.*
import com.example.core.feature.game.GameView2
import com.example.engine2.game.state.GameState
import com.example.core.navigation.Screens
import com.ilyinp.core.feature.settings.GameSettingsVM
import com.ilyinp.core.feature.settings.GameSettingsView
import com.ilyinp.core.feature.settings.GameSettingsVmImpl

@Composable
fun AppNavGraph(
  modifier: Modifier = Modifier,
  navController: NavHostController = rememberNavController(),
  startDestination: String = Screens.SCREEN_MENU

) {
  NavHost(
    navController = navController, startDestination = startDestination, modifier = modifier

  ) {
    composable(Screens.SCREEN_MENU) {
      MenuView(
        onClickPlay = { navController.navigate(Screens.SCREEN_GAME) },
        onClickTutorial = { navController.navigate(Screens.SCREEN_END) },
        onClickSettings = {navController.navigate(Screens.SCREEN_SETTINGS)},
        onClickStore = {}
      )
    }
    composable(Screens.SCREEN_GAME) {
      val vm: GameScreenVM = hiltViewModel<GameScreenVMImpl>()
      GameScreenView(
        vm = vm,
        onGameEnd = { navController.navigate(Screens.SCREEN_END) },
        onClickMenu = { navController.navigate(Screens.SCREEN_GAME_MENU) }
      )
    }
    composable(Screens.SCREEN_END) {
      MenuEnd(
        onClickNewPlay = { navController.navigate(Screens.SCREEN_GAME) }
      )
    }
    composable(Screens.SCREEN_GAME_MENU) {
      GameMenuView(
        onClickMenu = { navController.navigate(Screens.SCREEN_MENU) },
        onClickPlayAgain = { navController.navigate(Screens.SCREEN_GAME) }
      )
    }
    composable(Screens.SCREEN_SETTINGS) {
      val vm: GameSettingsVM = hiltViewModel<GameSettingsVmImpl>()
        GameSettingsView(vm = vm)
    }
  }
}