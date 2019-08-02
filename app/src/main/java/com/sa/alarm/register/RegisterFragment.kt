package com.sa.alarm.register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseAuth
import com.sa.alarm.R
import com.sa.alarm.base.BaseFragment
import com.sa.alarm.common.Constants
import com.sa.alarm.home.HomeActivity
import com.sa.alarm.utils.LogUtils
import com.sa.alarm.utils.NetworkUtils
import com.sa.alarm.utils.SharedPrefUtils
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.*

class RegisterFragment : BaseFragment() {
    companion object {
        private var registerInstance: RegisterFragment? = null

        fun getInstance(): RegisterFragment {
            if (registerInstance == null) {
                registerInstance = RegisterFragment()
            }
            return registerInstance as RegisterFragment
        }
    }

    private val TAG : String = this.javaClass.getSimpleName()
    private val PERMISSION_LIST = Arrays.asList("email", "public_profile")

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var callbackManager: CallbackManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        LogUtils.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        btnRegister.setOnClickListener {
            if (NetworkUtils.isOnline) {
                if(isValid()){
                    registerViewModel.registerEmailUser(etEmail.text.toString(), etPassword.text.toString(), etName.text.toString())
                }
            }
        }
        btnFbRegister.setOnClickListener {
            if (NetworkUtils.isOnline) {
                registerFbUser()
            }
        }

        registerViewModel.getRegistrationStatus().observe(this, Observer { status ->
            when (status) {
                true -> {
                    SharedPrefUtils.putString(Constants.USER_ID, FirebaseAuth.getInstance().currentUser?.uid.toString())
                    SharedPrefUtils.putBoolean(Constants.IS_LOGGED_IN, true)

                    startActivity(Intent(activity!!, HomeActivity::class.java))
                    activity!!.finish()
                }
            }
        })

        registerViewModel.getFailureMessage().observe(this , Observer { exceptionMessage ->
            Toast.makeText(activity, "Failed : ${exceptionMessage}", Toast.LENGTH_SHORT).show()
        })

        registerViewModel.getProgresBar().observe(this, Observer { isLoading ->
            if(isLoading) pgbRegister.visibility = View.VISIBLE else pgbRegister.visibility = View.GONE
        })
    }

    private fun isValid(): Boolean {
        val email = etEmail.text.toString()
        val password =etPassword.text.toString()
        val name =etName.text.toString()

        if(email.isEmpty()){
            etEmail.error = resources.getString(R.string.error_email_empty)
            return false
        }
        else if(!isValidEmail(email)){
            etEmail.error = resources.getString(R.string.error_email_invalid)
            return false
        }

        else if(name.isEmpty()){
            etName.error =resources.getString(R.string.error_name_empty)
            return false
        }
        else if(password.isEmpty()){
            tilPassword.error=resources.getString(R.string.error_password_empty)
            return false
        }
        else if(password.length <= 8){
            tilPassword.error=resources.getString(R.string.error_password_invalid)

            return false
        }
        return true
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun registerFbUser() {
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
        LoginManager.getInstance().registerCallback(
            callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    val token = result?.accessToken
                    LogUtils.d(TAG,"loginFb : onSuccess : ${token}")
                    registerViewModel.registerFbUser(token!!)
                }

                override fun onCancel() {
                    LogUtils.d(TAG,"onCancel :")
                }

                override fun onError(error: FacebookException?) {
                    LogUtils.d(TAG,"onError : ${error?.message}")
                    error?.printStackTrace()
                }
            })
        LoginManager.getInstance().logInWithReadPermissions(this, PERMISSION_LIST);
    }

    private fun init() {
        firebaseAuth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create();
        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}