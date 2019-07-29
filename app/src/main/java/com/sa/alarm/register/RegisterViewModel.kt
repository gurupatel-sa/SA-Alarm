package com.sa.alarm.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.sa.alarm.register.model.User
import com.sa.alarm.utils.LogUtils
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class RegisterViewModel : ViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var user: MutableLiveData<User> = MutableLiveData()
    private var isRegistrationSuccess: MutableLiveData<Boolean> = MutableLiveData()
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var faliureMessage: MutableLiveData<String> = MutableLiveData()

    var TAG = "RegisterViewmodel"

    fun registerEmailUser(mail: String, password: String, name: String) {
        isLoading.value = true
        firebaseAuth.createUserWithEmailAndPassword(mail, password)
            .addOnCompleteListener { task ->
                LogUtils.d(TAG, "cOMPLETElISTNER")
                if (task.isSuccessful) {
                    task.result?.user?.let {
                        user.value = User(it.uid, it.email.toString(), null, name)
                        isRegistrationSuccess.value = true
                    }
                }
                else {
                    LogUtils.d(TAG, "onFaliure " +  task.exception?.message)
                    faliureMessage.value = task.exception?.message
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                    } catch (e: FirebaseAuthUserCollisionException) {
                    } catch (e: Exception) {
                        Log.e(TAG, e.message)
                    }
                    isRegistrationSuccess.value = false
                }
                isLoading.value = false
            }
    }

    fun registerFbUser(token: AccessToken) {
        isLoading.value = true
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    task.result?.user?.let {
                        user.value =
                            User(it.uid, it.email.toString(), it.photoUrl.toString(), it.displayName.toString())
                        isRegistrationSuccess.value = true
                    }

                } else {
                    faliureMessage.value = task.exception?.message
                    isRegistrationSuccess.value = false
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
                isLoading.value = false
            }
    }

    fun getRegistrationStatus(): LiveData<Boolean> {
        return isRegistrationSuccess
    }

    fun getUser(): LiveData<User> {
        return user
    }

    fun getProgresBar(): LiveData<Boolean> {
        return isLoading
    }

    fun getFailureMessage(): LiveData<String> {
        return faliureMessage
    }

}