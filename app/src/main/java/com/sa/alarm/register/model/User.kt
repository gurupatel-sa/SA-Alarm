package com.sa.alarm.register.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class User constructor (
    var uid:String ="-1",
    var email:String="",
    var photoUrl:String? = null,
    var token:String ="-1",
    var displayName: String="",
    @ServerTimestamp
    var timestamp: Date?=null
)