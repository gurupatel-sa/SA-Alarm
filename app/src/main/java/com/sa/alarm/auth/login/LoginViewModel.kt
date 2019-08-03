package com.sa.alarm.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.sa.alarm.common.Constants
import com.sa.alarm.utils.LogUtils

class LoginViewModel : ViewModel() {
    private val TAG : String = this.javaClass.getSimpleName()

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var isLoginSuccess: MutableLiveData<Boolean> = MutableLiveData()
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var faliureMessage: MutableLiveData<String> = MutableLiveData()
    private var passwordReset: MutableLiveData<Boolean> = MutableLiveData()
    private var rootRef : FirebaseFirestore = FirebaseFirestore.getInstance();

    fun loginEmailAuth(email: String, password: String) {
        isLoading.value = true
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.user?.let {
                    LogUtils.d(TAG, " " + task.result?.user?.uid)
                    updateToken(it.uid)
                }
            } else {
                faliureMessage.value = task.exception?.message
                isLoginSuccess.value = false
                isLoading.value = false
            }
        }
    }

    fun updateToken(uid:String){
        rootRef.collection(Constants.USER_LIST)
            .document(uid)
            .update("token",FirebaseInstanceId.getInstance().token.toString())
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    isLoginSuccess.value = true
                }
                else{
                    faliureMessage.value = task.exception?.message
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
                    LogUtils.d(TAG,"loginFbAuth : signIn success")
                    isLoginSuccess.value = true

                } else {
                    LogUtils.d(TAG,"loginFbAuth :signIn faliure ${task.exception}")
                    isLoginSuccess.value = false
                    faliureMessage.value = task.exception?.message
                }
                isLoading.value = false
            }
    }

    fun sendEmailForgotPassword(email: String){
        isLoading.value = true
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                passwordReset.value =true
                isLoading.value = false
            }
            .addOnFailureListener {exception ->
                try {
                    throw exception
                }
                catch (invalidEmail : FirebaseAuthInvalidUserException)
                {
                    LogUtils.d(TAG,"sendEmailForgotPassword :invalidEmail exception" +invalidEmail?.message )
                }
                catch (e: Exception) {
                    LogUtils.d(TAG,"sendEmailForgotPassword :exception" +e?.message )

                }
                faliureMessage.value =exception.message
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

    fun getResetPasswordStatus(): LiveData<Boolean> {
        return passwordReset
    }

    fun checkUserEmailExist(email: String) {

    }
}