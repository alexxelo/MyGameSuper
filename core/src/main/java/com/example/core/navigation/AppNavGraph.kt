package com.example.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.core.*
import com.example.core.feature.mainmenu.MainMenuVM
import com.example.core.feature.mainmenu.MainMenuVMImpl
import com.example.core.feature.settings.GameSettingsVM
import com.example.core.feature.settings.GameSettingsView
import com.example.core.feature.settings.GameSettingsVmImpl

@Composable
fun AppNavGraph(
  modifier: Modifier = Modifier,
  navController: NavHostController = rememberNavController(),
  startDestination: String = Screens.SCREEN_MENU,
  languageChanged: () -> Unit = {},
) {
  NavHost(
    navController = navController, startDestination = startDestination, modifier = modifier

  ) {
    composable(route = Screen.MainMenu.route) {
      val vm: MainMenuVM = hiltViewModel<MainMenuVMImpl>()

      MainMenuView(
        vm = vm,
        modifier = Modifier.fillMaxSize(),
        onClickPlay = { navController.navigate(Screen.PreGame.route) },
        onClickTutorial = {},
        onClickSettings = { navController.navigate(Screen.Settings.route) },
        onClickStore = {}


      )
    }
    composable(route = Screen.GameScreen.route) {
      val vm: GameScreenVM = hiltViewModel<GameScreenVMImpl>()
      GameScreenView(
        vm = vm,
        onGameEnd = { navController.navigate(Screens.SCREEN_END) },
        onClickMenu = { navController.navigate(Screens.SCREEN_MENU) },
        onClickPlayAgain = {
          navController.navigateUp()
          navController.navigate(Screens.SCREEN_GAME)
        }
      )
    }
    composable(route = Screen.Settings.route) {
      val vm: GameSettingsVM = hiltViewModel<GameSettingsVmImpl>()
      GameSettingsView(
        vm = vm,
        modifier = Modifier.fillMaxSize(),
        languageChanged = languageChanged
      )
    }
    /*
    composable(Screens.SCREEN_MENU) {
      MainMenuView(
        onClickPlay = { navController.navigate(Screens.SCREEN_GAME) },
        onClickTutorial = { navController.navigate(Screens.SCREEN_END) },
        onClickSettings = { navController.navigate(Screens.SCREEN_SETTINGS) },
        onClickStore = {}
      )
    }*/
    composable(Screens.SCREEN_GAME) {
      val vm: GameScreenVM = hiltViewModel<GameScreenVMImpl>()
      GameScreenView(
        vm = vm,
        onGameEnd = { navController.navigate(Screens.SCREEN_END) },
        onClickMenu = { navController.navigate(Screens.SCREEN_MENU) },
        onClickPlayAgain = {
          navController.navigateUp()
          navController.navigate(Screens.SCREEN_GAME)
        }
      )
    }

  }
}