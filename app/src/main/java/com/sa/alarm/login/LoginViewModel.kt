package com.sa.alarm.login

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.sa.alarm.utils.LogUtils

class LoginViewModel : ViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var isLoginSuccess: MutableLiveData<Boolean> = MutableLiveData()
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val TAG: String = "LoginViewModel"
    private var faliureMessage: MutableLiveData<String> = MutableLiveData()

    fun loginEmailAuth(email: String, password: String) {
        isLoading.value = true
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.user?.let {
                    LogUtils.d(TAG, " " + task.result?.user?.uid)
                    isLoginSuccess.value = true
                }
            } else {
                faliureMessage.value = task.exception?.message
                isLoginSuccess.value = false
            }
            isLoading.value = false
        }
    }

    fun loginFbAuth(token: AccessToken) {
        isLoading.value = true

        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    isLoginSuccess.value = true

                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    isLoginSuccess.value = false
                    faliureMessage.value = task.exception?.message
                }
                isLoading.value = false
            }
    }


    fun getLoginStatus() : LiveData<Boolean>{
        return isLoginSuccess
    }

    fun getProgresBar(): LiveData<Boolean>{
        return isLoading
    }

    fun getFailureMessage(): LiveData<String> {
        return faliureMessage
    }

}