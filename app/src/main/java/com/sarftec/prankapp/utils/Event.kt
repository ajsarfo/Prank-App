package com.sarftec.prankapp.utils

sealed class Event<T>(private val data: T) {

    protected fun handleData(callback: (T) -> Unit) = callback(data)
    abstract fun onHandle(callback: (T) -> Unit)

    class OneShot<T>(data: T) : Event<T>(data) {
        private var isHandled = false
        override fun onHandle(callback: (T) -> Unit) {
           if(isHandled) return
            isHandled = true
            handleData(callback)
        }
    }

    class Live<T>(data: T) : Event<T>(data) {
        override fun onHandle(callback: (T) -> Unit) {
          handleData(callback)
        }
    }
}