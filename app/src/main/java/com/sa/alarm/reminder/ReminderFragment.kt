package com.sa.alarm.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sa.alarm.R
import com.sa.alarm.base.BaseFragment
import com.sa.alarm.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_reminder.*
import android.graphics.Rect
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sa.alarm.addreminder.AddReminder
import com.sa.alarm.common.AlarmSchedular
import com.sa.alarm.home.HomeActivity

class ReminderFragment :BaseFragment() {

    private lateinit var reminderViewModel: ReminderViewModel

    companion object{
        private var reminderInstance: ReminderFragment? = null
         val TAG : String = "ReminderFragment"

        fun getInstance(): ReminderFragment {
            if (reminderInstance == null) {
                reminderInstance =
                    ReminderFragment()
            }
            return reminderInstance as ReminderFragment
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        LogUtils.d(TAG,"onCreateView")
        return inflater.inflate(R.layout.fragment_reminder , container ,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        fabAddReminder.setOnClickListener {
            (activity as HomeActivity).replaceFragments(AddReminder.TAG , AddReminder.getInstance())
        }

        appbar.setOnClickListener {
        }

        reminderViewModel.newData.observe(activity!! , Observer {dataInserted ->
            LogUtils.d(TAG,"new alarm inserted :"+dataInserted.timestamp)
//            AlarmSchedular().setUpAlarm(activity!!.applicationContext ,dataInserted)
        })

        reminderViewModel.reminderList.observe(this, Observer {
            LogUtils.d(TAG, "observe  :" + it?.size)

//            NotificationUtils.createNotification(this)
//            NotificationUtils.createNotification("a","aa",applicationContext)
        })
    }

    private fun init() {
        reminderViewModel = ViewModelProviders.of(this).get(ReminderViewModel::class.java)

        reminderViewModel.getReminder()
    }
}