package com.sa.alarm.home.addreminder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sa.alarm.base.BaseFragment
import com.sa.alarm.common.DateFragment
import com.sa.alarm.common.TimeFragment
import com.sa.alarm.home.HomeActivity
import com.sa.alarm.home.reminder.ReminderViewModel
import com.sa.alarm.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_addreminder.*
import android.widget.ArrayAdapter
import android.widget.Toast
import com.sa.alarm.R
import com.sa.alarm.common.AlarmSchedular
import com.sa.alarm.utils.DateTimeUtils
import kotlinx.android.synthetic.main.toolbar_main.view.*
import java.util.*

class AddReminder : BaseFragment() {
    private lateinit var addReminderViewModel: AddReminderViewModel
    private val REQUEST_CODE_DATE = 12
    private val REQUEST_CODE_TIME = 14

    private val items = arrayOf("My Reminder", "Other Reminder")
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_addreminder, container, false)
    }

    companion object {
        private var addReminderInstance: AddReminder? = null
        val TAG: String = "AddReminder"

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
        etDate.setOnClickListener {
            val newFragment = DateFragment()
            newFragment.setTargetFragment(this, REQUEST_CODE_DATE);
            newFragment.show(fragmentManager!!, "DatePicker")
        }

        toolbar.ivBack.setOnClickListener {
            (activity as HomeActivity).supportFragmentManager.popBackStackImmediate();
        }

        etTime.setOnClickListener() {
            val newFragment = TimeFragment()
            newFragment.setTargetFragment(this, REQUEST_CODE_TIME);
            newFragment.show(fragmentManager!!, "TimePicker")
        }
//        DateUtils.getRelativeTimeSpanString(timeInMillis).toString()

        fabSetReminder.setOnClickListener {
            val reminder = etReminer.text.toString()
            val type: Int
            if (spType.selectedItem.equals("My Reminder")) type = 0 else type = 1

            val time = etTime.text.toString()
            val date = etDate.text.toString()

            if (isValid()) {
                addReminderViewModel.addReminder(reminder, type, date, time)
            }
        }

        addReminderViewModel.getReminder().observe(activity!!, Observer { reminder ->
            LogUtils.d(TAG, "getReminder observe :")
            var reminderViewModel = ViewModelProviders.of(activity!!).get(ReminderViewModel::class.java)
            reminderViewModel.insertReminder(reminder)
            AlarmSchedular().setUpAlarm(activity!!.applicationContext ,reminder)

            Toast.makeText(activity!!, resources.getText(R.string.reminder_added), Toast.LENGTH_SHORT).show();
        })
    }

    private fun init() {
        toolbar.tvTitle.setText(resources.getText(R.string.title_add_reminder))

        addReminderViewModel = ViewModelProviders.of(this).get(AddReminderViewModel::class.java)
        spinnerAdapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_spinner_dropdown_item, items)
        spType.adapter = spinnerAdapter
        etTime.setText(DateTimeUtils.getTime(Calendar.getInstance().timeInMillis))
        etDate.setText(DateTimeUtils.getDate(Calendar.getInstance().timeInMillis))

        addReminderViewModel.setReminderDate(Calendar.getInstance().timeInMillis)
        addReminderViewModel.setReminderTime(Calendar.getInstance().timeInMillis)
    }

    fun isValid(): Boolean {
        var reminder = etReminer.text.toString()
        var time = etTime.text.toString()
        if (reminder.isEmpty()) {
            etReminer.error = resources.getString(R.string.erro_reminder_empty)
        } else if (time.isEmpty()) {
            etTime.error = resources.getString(R.string.error_time_empty)
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_DATE && resultCode == Activity.RESULT_OK) {
            LogUtils.d(TAG, "onActivityResult :success")
            etDate.setText(DateTimeUtils.getDate(data?.extras?.getLong("selectedDate")!!))
            addReminderViewModel.setReminderDate(data?.extras?.getLong("selectedDate")!!)
        }

        if (requestCode == REQUEST_CODE_TIME && resultCode == Activity.RESULT_OK) {
            LogUtils.d(TAG, "onActivityResult :success")
            etTime.setText(DateTimeUtils.getTime(data?.extras?.getLong("selectedTime")!!))
            addReminderViewModel.setReminderTime(data?.extras?.getLong("selectedTime")!!)
        }
    }
}