package com.sa.alarm.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.sa.alarm.common.Constants
import com.sa.alarm.register.model.User
import com.sa.alarm.utils.LogUtils
import java.util.*

class RegisterViewModel : ViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var isRegistrationSuccess: MutableLiveData<Boolean> = MutableLiveData()
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var faliureMessage: MutableLiveData<String> = MutableLiveData()
    private var rootRef : FirebaseFirestore =FirebaseFirestore.getInstance();

    var TAG = "RegisterViewmodel"

    fun registerEmailUser(mail: String, password: String, name: String) {
        isLoading.value = true
        firebaseAuth.createUserWithEmailAndPassword(mail, password)
            .addOnCompleteListener { task ->
                LogUtils.d(TAG, "createUserWithEmailAndPassword")
                if (task.isSuccessful) {
                    task.result?.user?.let {
                        val user= User(
                            uid = it.uid ,
                            email = it.email.toString() ,
                            photoUrl = null ,
                            token = FirebaseInstanceId.getInstance().token.toString() ,
                            displayName = name ,
                            timestamp =  Calendar.getInstance().time
                        )
                        addUserInDb(user)
                    }
                }
                else {
                    LogUtils.d(TAG, "onFaliure " +  task.exception?.message)
                    faliureMessage.value = task.exception?.message
//                    try {
//                        throw task.exception!!
//                    } catch (e: FirebaseAuthWeakPasswordException) {
//                    } catch (e: FirebaseAuthInvalidCredentialsException) {
//                    } catch (e: FirebaseAuthUserCollisionException) {
//                    } catch (e: Exception) {
//                    }
                    isRegistrationSuccess.value = false
                    isLoading.value = false
                }
            }
    }

     fun addUserInDb(user: User) {
         rootRef.collection(Constants.USER_LIST)
             .document(user.uid)
             .set(user)
             .addOnCompleteListener { task ->
                 if(task.isSuccessful){
                     isRegistrationSuccess.value = true
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
                    LogUtils.d(TAG,"registerFbAuth : signIn success")
                    task.result?.user?.let {
                        val user= User(
                            uid = it.uid ,
                            email = it.email.toString() ,
                            photoUrl = it.photoUrl.toString() ,
                            token = FirebaseInstanceId.getInstance().token.toString() ,
                            displayName = it.displayName.toString() ,
                            timestamp =  Calendar.getInstance().time

                        )
                        addUserInDb(user)
                    }

                } else {
                    LogUtils.d(TAG,"registerFbAuth :signIn faliure ${task.exception}")
                    faliureMessage.value = task.exception?.message
                    isRegistrationSuccess.value = false
                    isLoading.value = false
                }
            }
    }

    fun getRegistrationStatus(): LiveData<Boolean> {
        return isRegistrationSuccess
    }


    fun getProgresBar(): LiveData<Boolean> {
        return isLoading
    }

    fun getFailureMessage(): LiveData<String> {
        return faliureMessage
    }

}