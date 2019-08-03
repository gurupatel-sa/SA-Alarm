package com.sa.alarm.home.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sa.alarm.common.Constants
import com.sa.alarm.auth.register.model.User

class ProfileViewmodel : ViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var rootRef: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var user: MutableLiveData<User> = MutableLiveData()
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        getProfileData(firebaseAuth.currentUser!!.uid)
    }

    private fun getProfileData(uid: String) {
        isLoading.value = true
        rootRef.collection(Constants.USER_LIST)
            .document(uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.value = task.result?.toObject(User::class.java)
                    isLoading.value = false
                }
                else{
                    isLoading.value = false
                }
            }
    }

    fun submitData(name: String) {
        isLoading.value = true
        var uid: String? = FirebaseAuth.getInstance().currentUser?.uid
        rootRef.collection(Constants.USER_LIST)
            .document(uid!!)
            .update("displayName", name)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isLoading.value = false
                } else {
                    isLoading.value = false
                }
            }
    }

    fun getUser(): LiveData<User> {
        return user
    }

    fun getProgresBar(): LiveData<Boolean>{
        return isLoading
    }
}