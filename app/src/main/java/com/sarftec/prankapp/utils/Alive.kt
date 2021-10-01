package com.sarftec.prankapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class Alive<T> {

    private val _data = MutableLiveData<T>()

    fun publish(data: T) {
        _data.value = data
    }

    fun liveData() : LiveData<T> = _data
}