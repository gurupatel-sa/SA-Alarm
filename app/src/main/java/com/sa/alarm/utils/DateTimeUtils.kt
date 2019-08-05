package com.sa.alarm.utils

/**
 * Created by guru on 3/8/19
 */

import android.annotation.SuppressLint
import java.text.DateFormatSymbols
import java.util.*

class DateTimeUtils {
    companion object{
        @SuppressLint("SimpleDateFormat")
        //convert timeinmillis to date string
         fun getDate(time: Long): String {
            val cal = Calendar.getInstance()
            cal.timeInMillis = time
            return cal.get(Calendar.DAY_OF_MONTH).toString() + " " + DateFormatSymbols().getMonths()[cal.get(Calendar.MONTH)]
        }

        //convert timeinmillis to time string
        fun getTime(time: Long): String {
            val cal = Calendar.getInstance()
            cal.timeInMillis = time

            var min =""
            if(cal.get(Calendar.MINUTE) <10)
                min="0"+cal.get(Calendar.MINUTE).toString()
            else
                min=cal.get(Calendar.MINUTE).toString()

            val type = if (cal.get(Calendar.AM_PM) == 1) {
                "PM"
            } else {
                "AM"
            }
            return cal.get(Calendar.HOUR).toString() + ":" + min + " " + type
        }
    }
}