package com.sarftec.prankapp.screen

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.view.inputmethod.EditorInfoCompat
import com.sarftec.prankapp.base.BaseScreen
import com.sarftec.prankapp.base.BaseViewModel
import com.sarftec.prankapp.base.Screen

class NumberScreen : BaseScreen() {

    override val viewModel by viewModels<NumberViewModel>()

    override fun navigate(text: String) {
        navigateTo(CountryScreen::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutBinding.textField.imeOptions = EditorInfo.TYPE_CLASS_PHONE
    }
}

class NumberViewModel : BaseViewModel() {

    override fun getScreenText(): Screen.ScreenEvent.Text {
        return Screen.ScreenEvent.Text(
            "Enter Your Mobile Number",
            "+233545267118",
            "Next"
        )
    }
}