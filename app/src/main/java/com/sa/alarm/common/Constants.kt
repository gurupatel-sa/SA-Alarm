package com.sa.alarm.common

class Constants {
    companion object{
        //SharedPref Keys
        val USER_ID :String ="uid"
        val IS_LOGGED_IN :String = "is_logged_in"

        //Auth Messages
        var SUCCESS ="Success"
        var FAILED ="Failed"

        //FIRESTORE FIELDS
        val USER_LIST="user_list"

        //ROOM TABLE
        const val DB_NAME="event"
        const val TABLENAME ="reminder"
        const val _ID ="_ID"
        const val EVENT_TITLE ="event_title"
        const val TIME="time"
        const val DATE="date"
        const val TYPE="type"
        const val TIMESTAMP="timestamp"

    }
}