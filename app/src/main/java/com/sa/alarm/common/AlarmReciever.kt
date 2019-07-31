package com.sa.alarm.common

import android.annotation.SuppressLint
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.os.PowerManager
import com.sa.alarm.R
import com.sa.alarm.utils.LogUtils
import com.sa.alarm.utils.NotificationUtils


class AlarmReceiver : BroadcastReceiver() {
    private val TAG: String = this.javaClass.getSimpleName()
    private var wakeLock: PowerManager.WakeLock? = null

    @SuppressLint("InvalidWakeLockTag")
    override fun onReceive(context: Context?, intent: Intent?) {
        LogUtils.d(TAG, "onReceive :")

        if (intent != null && intent.action != null) {
            if (intent?.action.equals(context?.getString(R.string.action_notify_alarm))) {

                WakeLocker.acquire(context!!)

                LogUtils.d(TAG, "onReceive : notificatio")
                NotificationUtils.createNotification("Name", "Title", context!!)
            }


        }
    }
}
