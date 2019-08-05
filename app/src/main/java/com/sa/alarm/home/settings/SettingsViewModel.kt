package com.sa.alarm.home.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sa.alarm.common.Constants

class SettingsViewModel : ViewModel() {
    private var rootRef: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    var notficationStatus: MutableLiveData<Boolean> = MutableLiveData()
    var availabilityStatus: MutableLiveData<Boolean> = MutableLiveData()
    var email: String = ""
    var isResetMailSent :MutableLiveData<Boolean> = MutableLiveData()

    init {
        getUserData()
    }

    private fun getUserData() {
        rootRef.collection(Constants.USER_LIST)
            .document(uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    availabilityStatus.value = task.result?.getBoolean(Constants.AVAILABILITY_STATUS)
                    notficationStatus.value = task.result?.getBoolean(Constants.NOTIFICATION_STATUS)
                    email = task.result?.getString("email").toString()
                } else {
                }
            }
    }

    fun updateAvailability(status: Boolean) {
        rootRef.collection(Constants.USER_LIST)
            .document(uid)
            .update(Constants.AVAILABILITY_STATUS, status)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    availabilityStatus.value = status
                } else {
                }
            }
    }

    fun updateNotification(status: Boolean) {
        rootRef.collection(Constants.USER_LIST)
            .document(uid)
            .update(Constants.NOTIFICATION_STATUS, status)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    notficationStatus.value = status
                } else {
                }
            }
    }

    fun changePassword() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnSuccessListener {
                isResetMailSent.value = true
            }
            .addOnFailureListener {
                it.printStackTrace()
                isResetMailSent.value = false
            }
    }
}