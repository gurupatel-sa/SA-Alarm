package com.sa.alarm.reminder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sa.alarm.R
import com.sa.alarm.addreminder.AddReminder
import com.sa.alarm.common.AlarmSchedular
import com.sa.alarm.db.model.Reminder
import com.sa.alarm.utils.LogUtils
import kotlinx.android.synthetic.main.activity_reminder.*
import java.util.*

class ReminderActivity : AppCompatActivity() {
    private val TAG: String = this.javaClass.getSimpleName()
    private val FRAGMENT_TAG ="ReminderFragment"
    private lateinit var fragmentManager: FragmentManager
    lateinit var reminderViewModel: ReminderViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        init()

        addReminder.setOnClickListener {
            fragmentManager.beginTransaction().add(R.id.fmAddReminder, AddReminder.getInstance(),FRAGMENT_TAG).commit()
        }

        reminderViewModel.newData.observe(this , Observer {dataInserted ->
            LogUtils.d(TAG,"new alarm inserted :"+dataInserted.time)
            AlarmSchedular().setUpAlarm(applicationContext ,dataInserted.time)
        })
    }

    private fun init() {
        reminderViewModel = ViewModelProviders.of(this).get(ReminderViewModel::class.java)
        fragmentManager = supportFragmentManager

//        reminderViewModel.insertReminder()
        reminderViewModel.getReminder()

        var date = Calendar.getInstance().time

        val startMillis: Long = Calendar.getInstance().run {
            set(
                2019, 6, 31, date.hours
                , date.minutes+1
            )
            timeInMillis
        }
//        reminderViewModel.insertReminder(Reminder("a",startMillis," b"))

        reminderViewModel.reminderList.observe(this, Observer {
            LogUtils.d(TAG, "observe  :" + it?.size)
            LogUtils.d(TAG, "observe  :" + startMillis)

//            NotificationUtils.createNotification(this)
//            NotificationUtils.createNotification("a","aa",applicationContext)
        })
    }
}
