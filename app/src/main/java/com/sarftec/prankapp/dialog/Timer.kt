package com.sarftec.prankapp.dialog

import android.content.Context
import android.util.Log
import com.sarftec.prankapp.computeSecondsLeft
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Timer(
    private val context: Context,
    private val coroutineScope: CoroutineScope
    ) {

    private val timeChannel: Channel<Long> = Channel()

    private var timerJob: Job? = null

    fun start() : ReceiveChannel<Long> {
         timerJob = coroutineScope.launch {
             var secondsLeft = computeSecondsLeft(context)
             Log.v("TAG", "Time left : ${secondsLeft}")
             while (true) {
                 timeChannel.send(if(secondsLeft <= 0) 0 else secondsLeft)
                 delay(1000)
                 secondsLeft-= 1
             }
         }
        return timeChannel
    }

    fun cancel() = timerJob?.cancel()
}