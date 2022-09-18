package com.example.core.feature.mainmenu.market

import com.example.core.feature.mainmenu.market.signin.SignInBtnVM

interface MarketCardVM {

  val signInBtnVm: SignInBtnVM

  //val achievementsBtnVm: AchievementBtnVM

  fun clear()
}