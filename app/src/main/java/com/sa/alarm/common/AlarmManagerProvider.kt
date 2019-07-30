package com.sa.alarm.common

import android.app.AlarmManager
import android.content.Context

class AlarmManagerProvider {
    private val TAG : String = this.javaClass.getSimpleName()

    companion object{
        private var ALARM_INSTANCE :AlarmManager? =null
        fun getAlarmManager(context : Context) : AlarmManager?{
            if(ALARM_INSTANCE == null){
                ALARM_INSTANCE= context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            }
            return ALARM_INSTANCE
        }
    }
}