package com.sa.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sa.alarm.common.AlarmSchedular
import com.sa.alarm.home.ReminderViewModel
import com.sa.alarm.utils.LogUtils
import java.util.*

class ReminderActivity : AppCompatActivity() {
    private val TAG : String = this.javaClass.getSimpleName()
    lateinit var reminderViewModel :ReminderViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        init()
    }

    private fun init() {
        reminderViewModel=ViewModelProviders.of(this).get(ReminderViewModel::class.java)

//        reminderViewModel.insertReminder()
        reminderViewModel.getReminder()

        val startMillis: Long = Calendar.getInstance().run {
            set(2019, 6, 30, 19,52 )
            timeInMillis
        }

        reminderViewModel.reminderList.observe(this , Observer {
            LogUtils.d(TAG,"observe  :" +it?.size)
            LogUtils.d(TAG,"observe  :" +startMillis)

            AlarmSchedular().setAlarm(applicationContext, startMillis)
        })
    }
}
