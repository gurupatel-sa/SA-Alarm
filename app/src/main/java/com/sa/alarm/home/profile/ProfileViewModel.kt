package com.sa.alarm.home.profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.sa.alarm.common.Constants
import com.sa.alarm.auth.register.model.User
import java.util.*

class ProfileViewmodel : ViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var rootRef: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var user: MutableLiveData<User> = MutableLiveData()
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var storageRef = FirebaseStorage.getInstance(Constants.STORAGE_URL).reference
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
        val uid: String? = FirebaseAuth.getInstance().currentUser?.uid
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

    fun updateProfile(uri: Uri) {
        isLoading.value=true
        val ref = storageRef.child("profile_images/" + UUID.randomUUID().toString())

        val uploadTask = ref.putFile(uri)

        val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation ref.downloadUrl
        })
            .addOnCompleteListener {
                task ->
                if(task.isSuccessful){
                    val downloadUri = task.result
                    addUploadRecordToDb(downloadUri.toString())
                }
            }
    }

    private fun addUploadRecordToDb(url: String) {
        val uid: String? = FirebaseAuth.getInstance().currentUser?.uid
        rootRef.collection(Constants.USER_LIST)
            .document(uid!!)
            .update("photoUrl", url)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isLoading.value = false
                } else {
                    isLoading.value = false
                }
            }
    }
}