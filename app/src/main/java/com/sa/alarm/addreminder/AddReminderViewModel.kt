package com.sa.alarm.addreminder

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sa.alarm.db.model.Reminder
import com.sa.alarm.utils.LogUtils
import java.util.*

class AddReminderViewModel : ViewModel() {
    private val TAG : String = this.javaClass.getSimpleName()
    private var reminder: MutableLiveData<Reminder> = MutableLiveData()
    private var date : Long =0
    private var time : Long =0

    init {

    }

    fun setReminderDate(date :Long){
        this.date =date
    }

    fun setReminderTime(time :Long){
        this.time =time
    }

    fun addReminder(event:String , type:Int ,date_ : String ,time_ :String) {

        var time = getTimeInMillies(date , time)
        var reminder :Reminder=Reminder(event , time , type ,date_ ,time_ )

        this.reminder.value = reminder
        LogUtils.d(TAG ,"addReminder : time " +reminder.time)
    }

    private fun getTimeInMillies(date: Long, time: Long): Long {
        LogUtils.d(TAG,"getTimeInMillies :"+date)
        LogUtils.d(TAG,"getTimeInMillies :"+time)
        var day =0
        var year =0
        var month = 0
        var hour =0
        var min = 0
        var calendar =Calendar.getInstance()
        calendar.timeInMillis = date

        year = calendar.get(Calendar.YEAR)
        month= calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.timeInMillis = time
        hour= calendar.get(Calendar.HOUR_OF_DAY)
        min= calendar.get(Calendar.MINUTE)

        Log.d(TAG ,"getTimeInMillies :"+date +" "+year+" ")

        return Calendar.getInstance().run{
            set(year,month,day,hour,min)
            timeInMillis
        }
    }

    fun getReminder() :LiveData<Reminder> {
        return reminder
    }
}