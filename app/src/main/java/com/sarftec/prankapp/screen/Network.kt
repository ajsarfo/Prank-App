package com.sarftec.prankapp.screen

import androidx.activity.viewModels
import com.sarftec.prankapp.base.Screen
import com.sarftec.prankapp.base.BaseScreen
import com.sarftec.prankapp.base.BaseViewModel

class NetworkScreen : BaseScreen() {

    override val viewModel by viewModels<NetworkViewModel>()

    override fun showInterstitial(): Boolean = true

    override fun navigate(text: String) {
        navigateTo(NameScreen::class.java)
    }
}

class NetworkViewModel : BaseViewModel() {

    override fun getScreenText(): Screen.ScreenEvent.Text {
        return Screen.ScreenEvent.Text(
            "Enter Your Network/SIM",
            "Vodafone",
            "Next"
        )
    }
}