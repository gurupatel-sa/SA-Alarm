package com.sa.alarm.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sa.alarm.common.Constants

@Entity(tableName = Constants.TABLENAME )
data class Reminder(

    @ColumnInfo(name = Constants.EVENT_TITLE)
    var eventTitle:String ="",

    @ColumnInfo(name = Constants.TIME)
    var time:Long=0 ,

    @ColumnInfo(name = Constants.TYPE)
    var type:String=""

){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants._ID)
    var _ID :Int =0
}