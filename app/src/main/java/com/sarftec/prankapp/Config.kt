package com.sarftec.prankapp

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.first
import java.util.*
import java.util.concurrent.TimeUnit

val STOP_TIME = longPreferencesKey("Stop_Time")
val IS_TIME_COMPLETE = booleanPreferencesKey("Is_Time_Complete")
val IS_TIME_STARTED = booleanPreferencesKey("Is_Time_Started")
val IS_APP_STARTED = booleanPreferencesKey("Is_App_Started")

fun getStopTime(): Long {
    return Calendar.getInstance().timeInMillis + TimeUnit.HOURS.toMillis(3)
}

suspend fun computeSecondsLeft(context: Context): Long {
    if(context.readSettings(IS_TIME_COMPLETE, false).first()) return 0
    val stopTime = context.readSettings(STOP_TIME, getStopTime()).first()
    val currentTime = Calendar.getInstance().timeInMillis
    return TimeUnit.MILLISECONDS.toSeconds(stopTime - currentTime)
}