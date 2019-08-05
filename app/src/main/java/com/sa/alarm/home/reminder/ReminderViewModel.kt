package com.sa.alarm.home.reminder

import android.app.Application
import androidx.lifecycle.*
import com.sa.alarm.db.database.AppDatabase
import com.sa.alarm.db.model.Reminder
import com.sa.alarm.utils.LogUtils
import kotlinx.coroutines.*

class ReminderViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG: String = this.javaClass.getSimpleName()
    var database: AppDatabase
    private var job: Job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)
    var newData: MutableLiveData<Reminder> = MutableLiveData()

    var myReminderList: MutableLiveData<List<Reminder>> = MutableLiveData()
    var otherReminderList: MutableLiveData<List<Reminder>> = MutableLiveData()

    init {
        database = AppDatabase.getInstance(application.applicationContext)!!
        getMyReminders()
        getOtherReminders()
    }

    fun insertReminder(newReminder: Reminder) {
        LogUtils.d(TAG, "insertReminder :")
        scope.launch {
            database.reminderDao().insert(newReminder)
            newData.postValue(newReminder)
//            dataInserted.postValue(true)
        }
    }

    fun getMyReminders() {
        scope.launch {
            myReminderList.postValue(database.reminderDao().getMyReminders())
        }
    }

    fun getOtherReminders() {
        scope.launch {
            otherReminderList.postValue(database.reminderDao().getOtherReminders())
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.coroutineContext.cancelChildren()
    }
}