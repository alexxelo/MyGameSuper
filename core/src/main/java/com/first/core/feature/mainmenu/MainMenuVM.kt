package com.first.core.feature.mainmenu

import com.first.core.feature.mainmenu.market.MarketCardVM

interface MainMenuVM {

  val marketCardVM: MarketCardVM

  fun playClickSound()
}