package com.first.core.navigation

import com.first.core.navigation.Screen.Keys.GAME_PARAM_MODE
import com.first.core.navigation.Screen.Keys.GAME_PARAM_MODIFICATOR
import com.first.core.navigation.Screen.Keys.GAME_PARAM_PROPERTIES
import com.first.core.navigation.Screen.Keys.GAME_PARAM_REGIONS
import com.first.core.navigation.Screen.Keys.ROUTE_GAME_END
import com.first.core.navigation.Screen.Keys.ROUTE_GAME_MENU
import com.first.core.navigation.Screen.Keys.ROUTE_GAME_OOF
import com.first.core.navigation.Screen.Keys.ROUTE_INFO
import com.first.core.navigation.Screen.Keys.ROUTE_MAIN_MENU
import com.first.core.navigation.Screen.Keys.ROUTE_OTHER_GAMES
import com.first.core.navigation.Screen.Keys.ROUTE_PRE_GAME
import com.first.core.navigation.Screen.Keys.ROUTE_SETTINGS
import com.first.core.navigation.Screen.Keys.ROUTE_STORE

sealed class Screen constructor(val route: String) {
  object PreGame : Screen(ROUTE_PRE_GAME)
  object GameScreen : Screen(
    ROUTE_GAME_OOF +
        "/{$GAME_PARAM_MODE}" +
        "?modificator={$GAME_PARAM_MODIFICATOR}" +
        "&regions={$GAME_PARAM_REGIONS}" +
        "&properties={$GAME_PARAM_PROPERTIES}"
  )

  object MainMenu : Screen(ROUTE_MAIN_MENU)
  object GameMenu : Screen(ROUTE_GAME_MENU)
  object GameEnd : Screen(ROUTE_GAME_END)
  object Settings : Screen(ROUTE_SETTINGS)
  object Store : Screen(ROUTE_STORE)
  object HowToPlay : Screen(ROUTE_INFO)
  object OtherGames : Screen(ROUTE_OTHER_GAMES)


  object Keys {

    const val ROUTE_PRE_GAME = "route_pre_game"

    const val ROUTE_GAME_OOF = "game"
    const val ROUTE_GAME_END = "game_end"
    const val GAME_PARAM_MODE = "mode"
    const val GAME_PARAM_MODIFICATOR = "modificator"
    const val GAME_PARAM_REGIONS = "regions"
    const val GAME_PARAM_PROPERTIES = "properties"

    const val ROUTE_MAIN_MENU = "route_main_menu"
    const val ROUTE_GAME_MENU = "route_game_menu"
    const val ROUTE_SETTINGS = "route_settings"
    const val ROUTE_STORE = "route_store"
    const val ROUTE_INFO = "route_info"
    const val ROUTE_OTHER_GAMES = "route_other_games"
  }
}
