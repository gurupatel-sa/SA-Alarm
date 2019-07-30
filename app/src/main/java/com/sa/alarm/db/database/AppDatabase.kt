package com.sa.alarm.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sa.alarm.common.Constants
import com.sa.alarm.db.dao.ReminderDao
import com.sa.alarm.db.model.Reminder

@Database(entities = [Reminder::class] ,version = 1 ,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase::class.java, Constants.DB_NAME)
                        .build()
                }
            }
            return INSTANCE
        }

    }

}