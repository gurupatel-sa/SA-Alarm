package com.sa.alarm.register.model

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class User(
    var uid:String,
    var email:String,
    var photoUrl:String?,
    var token:String,
    var displayName: String,
    @ServerTimestamp
    var timestamp: Date
)