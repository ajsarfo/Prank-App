package com.sarftec.prankapp.screen

import androidx.activity.viewModels
import com.sarftec.prankapp.base.Screen
import com.sarftec.prankapp.base.BaseScreen
import com.sarftec.prankapp.base.BaseViewModel

class NameScreen : BaseScreen() {

    override val viewModel by viewModels<NameViewModel>()

    override fun navigate(text: String) {
        navigateTo(SocialOrBundle::class.java)
    }
}

class NameViewModel : BaseViewModel() {

    override fun getScreenText(): Screen.ScreenEvent.Text {
        return Screen.ScreenEvent.Text(
            "Enter Your Name",
            "John Doe",
            "Next"
        )
    }
}