package com.sa.alarm.utils

import android.util.Log
import com.sa.alarm.BuildConfig

class LogUtils {
    companion object {

        //Debug
        fun d(tag: String, message: String) {
            if (BuildConfig.DEBUG) {
                Log.d(tag, message)
            }
        }

        //error
        fun e(tag: String, message: String) {
            if (BuildConfig.DEBUG) {
                Log.e(tag, message)
            }
        }


    }

}