package com.sa.alarm.common

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.app.PendingIntent
import android.content.Intent
import com.sa.alarm.R

class AlarmSchedular {
    private val TAG: String = this.javaClass.getSimpleName()

    fun setUpAlarm(context: Context, time: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmpendingIntent = createPendingIntent(context)

        if (Build.VERSION.SDK_INT >= 23) {

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, alarmpendingIntent)

        } else if (Build.VERSION.SDK_INT >= 19) {

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, alarmpendingIntent)

        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, alarmpendingIntent)
        }

    }

    fun createPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
        intent.action = context.getString(R.string.action_notify_alarm)
        val pendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return pendingIntent
    }
}