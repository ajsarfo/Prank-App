package com.sarftec.prankapp.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarftec.prankapp.manager.NetworkManager
import com.sarftec.prankapp.utils.Alive
import com.sarftec.prankapp.utils.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class OneShotViewModel : ViewModel() {

    val alive = Alive<Event<Screen.ScreenEvent>>()

    private var networkManager: NetworkManager? = null

    protected open fun shouldLoad() : Boolean = false

    private suspend fun navigate() {
        alive.publish(Event.OneShot(Screen.ScreenEvent.Loading))
        delay(1000)
        alive.publish(Event.OneShot(Screen.ScreenEvent.Navigate))
    }

    fun setContext(context: Context) {
        networkManager = NetworkManager(context)
    }

    fun onClicked() {
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