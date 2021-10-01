package com.sarftec.prankapp.base

import com.sarftec.prankapp.utils.Alive
import com.sarftec.prankapp.utils.Event

interface Screen {

    sealed class ScreenEvent {

        class Text(
            val header: String,
            val hint: String,
            val button: String,
        ) : ScreenEvent()
        object Clear : ScreenEvent()
        object Loading : ScreenEvent()
        object Error: ScreenEvent()
        object Navigate : ScreenEvent()
        object NetworkError : ScreenEvent()
    }

    interface ViewModel {
        val alive: Alive<Event<ScreenEvent>>
        fun setText(string: String)
        fun onClicked()
    }
}