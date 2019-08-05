package com.sa.alarm.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sa.alarm.db.model.Reminder

@Dao
interface ReminderDao {
    @Insert
    fun insertAll(vararg listItems: Reminder)

    @Insert
    fun insert(data: Reminder)

    @Query("SELECT * FROM reminder WHERE type =1 order by timestamp DESC ")
    fun getOtherReminders(): List<Reminder>

    @Query("SELECT * FROM reminder where type=0 order by timestamp DESC")
    fun getMyReminders(): List<Reminder>
}