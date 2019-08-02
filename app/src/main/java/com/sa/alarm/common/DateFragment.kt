package com.sa.alarm.common

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*
import android.content.Intent
import android.app.Activity
import com.sa.alarm.utils.LogUtils

class DateFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private val TAG : String = this.javaClass.getSimpleName()
    override fun onDateSet(view: DatePicker?, yy: Int, mm: Int, dd: Int) {
        var hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY )
        var min = Calendar.getInstance().get(Calendar.MINUTE )
        val timeInMillis: Long = Calendar.getInstance().run {
            set(yy, mm, dd , hour , min)
            timeInMillis
        }

        LogUtils.d(TAG,"onDateSet :time" + timeInMillis +" " +yy+ " "+mm+" "+dd)
        targetFragment!!.onActivityResult(
            targetRequestCode,
            Activity.RESULT_OK,
            Intent().putExtra("selectedDate", timeInMillis)
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val yy = calendar.get(Calendar.YEAR)
        val mm = calendar.get(Calendar.MONTH)
        val dd = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity!!, this, yy, mm, dd)
    }
}