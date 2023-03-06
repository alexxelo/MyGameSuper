package com.first.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.first.core.*
import com.first.core.feature.mainmenu.MainMenuVM
import com.first.core.feature.mainmenu.MainMenuVMImpl
import com.first.core.feature.mainmenu.MainMenuView
import com.first.core.feature.settings.GameSettingsVM
import com.first.core.feature.settings.GameSettingsView
import com.first.core.feature.settings.GameSettingsVmImpl
import com.first.core.GameScreenView
import com.first.core.feature.game.end.GameEndView
import com.first.core.feature.game.tipshop.TipsShopView

@Composable
fun AppNavGraph(
  modifier: Modifier,
  navController: NavHostController = rememberNavController(),
  startDestination: String = Screen.MainMenu.route,
  languageChanged: () -> Unit = {},
) {
  NavHost(
    navController = navController,
    startDestination = startDestination,
    modifier = modifier
  ) {
    composable(route = Screen.MainMenu.route) {
      val vm: MainMenuVM = hiltViewModel<MainMenuVMImpl>()
      MainMenuView(
        vm = vm,
        modifier = Modifier.fillMaxSize(),
        onClickPlay = { navController.navigate(Screen.GameScreen.route) },
        onClickTutorial = {},
        onClickSettings = { navController.navigate(Screen.Settings.route) },
        onClickBackToGame = {}
      )
    }
    composable(route = Screen.GameScreen.route) {
      val vm: GameScreenVM = hiltViewModel<GameScreenVMImpl>()
      GameScreenView(
        modifier = Modifier,
        vm = vm,
        onGameEnd = { navController.navigate(Screen.GameEnd.route) },
        onClickMenu = { navController.navigate(Screen.MainMenu.route) },
        onClickPlayAgain = {
          navController.navigateUp()
          navController.navigate(Screen.GameScreen.route)
        },
        onClickShopBack = { navController.navigateUp()
          navController.navigate(Screen.GameScreen.route)
        },

        onClickNewGame = { navController.navigate(Screen.GameScreen.route)}
      )
    }
    composable(route = Screen.Store.route){
      //val vm: TipShopVM = hiltViewModel<
      TipsShopView()
    }
    composable(route = Screen.Settings.route) {
      val vm: GameSettingsVM = hiltViewModel<GameSettingsVmImpl>()
      GameSettingsView(
        vm = vm,
        modifier = Modifier.fillMaxSize(),
        languageChanged = languageChanged
      )
    }

    composable(route = Screen.GameMenu.route) {
      GameMenuView(
        onClickMenu = {
          navController.navigateUp()
          navController.navigate(Screen.MainMenu.route)
        },
        onClickPlayAgain = {
          navController.navigateUp()
          navController.navigate(Screen.GameScreen.route)
        },
        onClickBack = { navController.navigateUp() }
      )
    }

    composable(route = Screen.GameEnd.route) {
      val vm: GameScreenVM = hiltViewModel<GameScreenVMImpl>()
      GameEndView(
        vm = vm,
        onClickNewPlay = { navController.navigate(Screen.GameScreen.route) },
        onClickMenu = { navController.navigate(Screen.MainMenu.route) }
      )
    }

  }
}