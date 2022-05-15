package com.example.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.core.GameScreenView
import com.example.core.MenuEnd
import com.example.core.MenuView
import com.example.core.feature.game.GameView2
import com.example.engine2.game.state.GameState
import com.example.core.navigation.Screens

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
                onClickPlay = {navController.navigate(Screens.SCREEN_GAME)},
                onClickTutorial = {},
                onClickStore = {}
            )
        }
        composable(Screens.SCREEN_GAME) {
            GameScreenView()
            //GameView2(gameState = GameState.createInitial())
        }
        composable (Screens.SCREEN_END){
            MenuEnd()
        }
    }
}