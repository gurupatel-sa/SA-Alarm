package com.sa.alarm.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sa.alarm.utils.LogUtils

class BootReceiver : BroadcastReceiver() {

    private val TAG : String = this.javaClass.getSimpleName()
    override fun onReceive(context: Context?, intent: Intent?) {
        LogUtils.d(TAG,"onReceive Boot Receiver")
        if (context != null && intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            LogUtils.d(TAG,"Calling alarm schedular")
            /*
                *TODO
                * update all reminders again
                * call AlarmSchedular from it
             */

        }
    }
}