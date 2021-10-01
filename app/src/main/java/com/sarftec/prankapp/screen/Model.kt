package com.sarftec.prankapp.screen

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.sarftec.prankapp.*
import com.sarftec.prankapp.base.BaseScreen
import com.sarftec.prankapp.base.BaseViewModel
import com.sarftec.prankapp.base.Screen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ModelScreen : BaseScreen() {

    override val viewModel by viewModels<ModelViewModel>()

    override fun showInterstitial(): Boolean = true

    override fun navigate(text: String) {
        navigateTo(NumberScreen::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            if(readSettings(IS_APP_STARTED, true).first()) {
                Toast.makeText(
                    this@ModelScreen,
                    getString(R.string.intro_message),
                    Toast.LENGTH_LONG
                ).show()
            }
            editSettings(IS_APP_STARTED, false)
        }
    }
}

class ModelViewModel : BaseViewModel(), Screen.ViewModel {

    override fun getScreenText(): Screen.ScreenEvent.Text {
        return  Screen.ScreenEvent.Text(
            "Enter Your Mobile Model",
            "Samsung",
            "Next"
        )
    }
}