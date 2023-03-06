package com.first.core.feature.mainmenu.market

import com.first.core.feature.mainmenu.market.signin.SignInBtnVM

interface MarketCardVM {

  val signInBtnVm: SignInBtnVM

  //val achievementsBtnVm: AchievementBtnVM

  fun clear()
}