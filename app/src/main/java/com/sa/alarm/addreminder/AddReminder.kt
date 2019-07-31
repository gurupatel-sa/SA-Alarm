package com.sa.alarm.addreminder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.sa.alarm.R
import com.sa.alarm.base.BaseFragment
import com.sa.alarm.common.DateFragment
import com.sa.alarm.common.TimeFragment
import com.sa.alarm.reminder.ReminderViewModel
import com.sa.alarm.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_reminder.*

class AddReminder : BaseFragment() {
    private val TAG : String = this.javaClass.getSimpleName()
    private lateinit var addReminderViewModel : AddReminderViewModel
    private val REQUEST_CODE_DATE =12
    private val REQUEST_CODE_TIME =14
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reminder , container,false)
    }
    companion object{
        private var addReminderInstance: AddReminder? = null

        fun getInstance(): AddReminder {
            if (addReminderInstance == null) {
                addReminderInstance = AddReminder()
            }
            return addReminderInstance as AddReminder
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        btnSetDate.setOnClickListener {
            val newFragment = DateFragment()
            newFragment.setTargetFragment(this, REQUEST_CODE_DATE);
            newFragment.show(fragmentManager!!, "DatePicker")
        }

        btnSetTime.setOnClickListener {
            val newFragment = TimeFragment()
            newFragment.setTargetFragment(this, REQUEST_CODE_TIME);
            newFragment.show(fragmentManager!!, "TimePicker")

        }

        btnSubmit.setOnClickListener {
            val note = etNote.text.toString()
            val type = etType.text.toString()

            addReminderViewModel.addReminder(note , type )
        }

        addReminderViewModel.getReminder().observe(this , Observer {reminder ->
            LogUtils.d(TAG,"getReminder observe :")
            var reminderViewModel = ViewModelProviders.of(activity!!).get(ReminderViewModel::class.java)
            reminderViewModel.insertReminder(reminder)
        })
    }

    private fun init() {
        addReminderViewModel=ViewModelProviders.of(this).get(AddReminderViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE_DATE && resultCode == Activity.RESULT_OK){
            LogUtils.d(TAG,"onActivityResult :success")
            addReminderViewModel.setReminderDate(data?.extras?.getLong("selectedDate")!!)
        }

        if(requestCode == REQUEST_CODE_TIME && resultCode == Activity.RESULT_OK){
            LogUtils.d(TAG,"onActivityResult :success")
            addReminderViewModel.setReminderTime(data?.extras?.getLong("selectedTime")!!)
        }
    }
}