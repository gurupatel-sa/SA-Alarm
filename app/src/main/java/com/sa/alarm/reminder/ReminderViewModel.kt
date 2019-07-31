package com.sa.alarm.reminder

import android.app.Application
import androidx.lifecycle.*
import com.sa.alarm.db.database.AppDatabase
import com.sa.alarm.db.model.Reminder
import com.sa.alarm.utils.LogUtils
import kotlinx.coroutines.*

class ReminderViewModel( application : Application) : AndroidViewModel(application) {

    private val TAG : String = this.javaClass.getSimpleName()
    var database :AppDatabase
    private var job: Job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)
     var newData : MutableLiveData<Reminder> = MutableLiveData()

     var reminderList: MutableLiveData<List<Reminder>> = MutableLiveData()

    init {
        database = AppDatabase.getInstance(application.applicationContext)!!
    }

    fun insertReminder(newReminder :Reminder) {
        LogUtils.d(TAG,"insertReminder :")
        scope.launch {
            database.reminderDao().insert(newReminder)
//            dataInserted.postValue(true)
            newData.postValue(newReminder)
        }
    }

    fun getReminder(){
        scope.launch {
            reminderList.postValue(database.reminderDao().getUsers())
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.coroutineContext.cancelChildren()
    }
}