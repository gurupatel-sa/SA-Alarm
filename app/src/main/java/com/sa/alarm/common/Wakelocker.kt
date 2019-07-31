package com.sa.alarm.common

import android.annotation.SuppressLint
import android.content.Context
import android.os.PowerManager

object WakeLocker {
    private var wakeLock: PowerManager.WakeLock? = null

    @SuppressLint("WakelockTimeout")
    fun acquire(context: Context) {
        if (wakeLock != null) wakeLock!!.release()

        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE, "tag:WakeLock"
        )
        wakeLock!!.acquire(1000)
    }

    fun release() {
        if (wakeLock != null) wakeLock!!.release()
        wakeLock = null
    }
}
