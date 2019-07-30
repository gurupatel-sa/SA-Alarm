package com.sa.alarm.common

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.app.PendingIntent
import android.content.Intent
import com.sa.alarm.utils.LogUtils

class AlarmSchedular {
    private val TAG : String = this.javaClass.getSimpleName()
    fun setAlarm(context:Context , time : Long){
        
        LogUtils.d(TAG,"setAlarm :")
        val manager = AlarmManagerProvider.getAlarmManager(context)

        var intent = Intent(context, AlarmIntenService::class.java)
        var pendingIntent = PendingIntent.getService(
            context, 280192, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= 23) {

            manager?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)

        } else if (Build.VERSION.SDK_INT >= 19) {

            manager?.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)

        } else {
            manager?.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
        }
    }
}