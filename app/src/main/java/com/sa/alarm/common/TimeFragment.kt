package com.sa.alarm.common

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*
import android.content.Intent
import android.app.Activity
import android.app.TimePickerDialog
import android.widget.TimePicker
import com.sa.alarm.utils.LogUtils

class TimeFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener{
    private val TAG : String = this.javaClass.getSimpleName()
    override fun onTimeSet(p0: TimePicker?, hour: Int, min: Int) {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        LogUtils.d(TAG ,"onTimeSet :"+ hour + min)


        val timeInMillis: Long = Calendar.getInstance().run { set(year , month , day ,hour ,min)
            timeInMillis }

        targetFragment!!.onActivityResult(
            targetRequestCode,
            Activity.RESULT_OK,
            Intent().putExtra("selectedTime", timeInMillis)
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val h = calendar.get(Calendar.HOUR_OF_DAY)
        val m = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(activity!!, this, h , m , false)
    }
}