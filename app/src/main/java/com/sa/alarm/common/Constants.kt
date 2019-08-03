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
        val USER_LIST="user_list" //collection
        val FOLLOW_LIST: String="follow_list" //collection

        val IMAGEURL ="https://firebasestorage.googleapis.com/v0/b/sa-alarm.appspot.com/o/profile.png?alt=media&token=9005444c-3d2d-461f-b29e-ec4994213e77"
        val IS_FOLLOWING:String = "is_following"


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