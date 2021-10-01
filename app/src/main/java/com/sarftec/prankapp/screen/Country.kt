package com.sarftec.prankapp.screen

import androidx.activity.viewModels
import com.sarftec.prankapp.base.Screen
import com.sarftec.prankapp.base.BaseScreen
import com.sarftec.prankapp.base.BaseViewModel

class CountryScreen : BaseScreen() {

    override val viewModel by viewModels<CountryViewModel>()

    override fun navigate(text: String) {
        navigateTo(NetworkScreen::class.java)
    }
}

class CountryViewModel : BaseViewModel() {

    override fun getScreenText(): Screen.ScreenEvent.Text {
        return Screen.ScreenEvent.Text(
            "Enter Your Country",
            "USA",
            "Next"
        )
    }
}