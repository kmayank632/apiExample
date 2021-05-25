package com.example.apipractice.utills

import android.util.Log
import com.example.apipractice.BuildConfig

/**
 * Print LOG Extension Functions
 * */

enum class LogType {
    VERBOSE, WARNING, DEBUG, INFO, ERROR
}

fun logger(tag: String = "", message: String?, type: LogType = LogType.VERBOSE) {
    if (BuildConfig.DEBUG && message != null) {
        when (type) {
            LogType.VERBOSE -> Log.v(tag, message)
            LogType.DEBUG -> Log.d(tag, message)
            LogType.WARNING -> Log.w(tag, message)
            LogType.INFO -> Log.i(tag, message)
            LogType.ERROR -> Log.e(tag, message)
        }
    }
}