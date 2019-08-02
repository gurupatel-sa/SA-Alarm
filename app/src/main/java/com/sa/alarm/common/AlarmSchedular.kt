package com.sa.alarm.common

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.app.PendingIntent
import android.content.Intent
import com.sa.alarm.R
import com.sa.alarm.db.model.Reminder
import com.sa.alarm.utils.LogUtils

class AlarmSchedular {
    private val TAG: String = this.javaClass.getSimpleName()

    fun setUpAlarm(context: Context, reminder: Reminder) {
        LogUtils.d(TAG,"setUpAlarm :" +reminder.timestamp)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmpendingIntent = createPendingIntent(context ,reminder)

        if (Build.VERSION.SDK_INT >= 23) {

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminder.timestamp, alarmpendingIntent)

        } else if (Build.VERSION.SDK_INT >= 19) {

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminder.timestamp, alarmpendingIntent)

        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.timestamp, alarmpendingIntent)
        }
    }

    fun createPendingIntent(context: Context ,reminder: Reminder): PendingIntent {
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
        intent.action = context.getString(R.string.action_notify_alarm)
        intent.putExtra("title", reminder.eventTitle);

        val pendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return pendingIntent
    }
}