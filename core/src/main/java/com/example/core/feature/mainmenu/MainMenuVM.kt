package com.example.core.feature.mainmenu

import com.example.core.feature.mainmenu.market.MarketCardVM

interface MainMenuVM {

  val marketCardVM: MarketCardVM

  fun playClickSound()
}