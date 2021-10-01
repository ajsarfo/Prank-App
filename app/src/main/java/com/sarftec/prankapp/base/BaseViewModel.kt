package com.sarftec.prankapp.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarftec.prankapp.manager.NetworkManager
import com.sarftec.prankapp.utils.Alive
import com.sarftec.prankapp.utils.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel(), Screen.ViewModel {

    final override val alive = Alive<Event<Screen.ScreenEvent>>()

    private var networkManager: NetworkManager? = null

    private var input = ""

    init {
        alive.publish(Event.Live(getScreenText()))
    }

    abstract fun getScreenText(): Screen.ScreenEvent.Text

    private suspend fun navigate() {
        alive.publish(Event.OneShot(Screen.ScreenEvent.Loading))
        delay(1000)
        alive.publish(Event.OneShot(Screen.ScreenEvent.Navigate))
    }

    fun setContext(context: Context) {
        networkManager = NetworkManager(context)
    }

    override fun setText(string: String) {
        input = string
        if (string.isNotEmpty() || string.isNotBlank()) alive.publish(Event.OneShot(Screen.ScreenEvent.Clear))
    }

    override fun onClicked() {
        if (input.isEmpty() || input.isBlank()) {
            alive.publish(Event.OneShot(Screen.ScreenEvent.Error))
            return
        }
        viewModelScope.launch {
            networkManager?.let {
                if (!it.isNetworkAvailable()) alive.publish(
                    Event.OneShot(Screen.ScreenEvent.NetworkError)
                )
                else navigate()
            } ?: navigate()
        }
    }
}