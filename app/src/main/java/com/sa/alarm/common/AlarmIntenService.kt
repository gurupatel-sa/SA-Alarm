package com.sa.alarm.common

import android.app.IntentService
import android.app.Service
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.sa.alarm.utils.LogUtils


class AlarmIntenService : IntentService("Alarm") {
    private val TAG: String = this.javaClass.getSimpleName()
    override fun onHandleIntent(p0: Intent?) {
        LogUtils.d(TAG, "onHandleIntent :")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.d(TAG,"onDestroy :")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d(TAG ,"onStartCommand :")
        return Service.START_STICKY
    }

}