package com.sa.alarm.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.sa.alarm.utils.SharedPrefUtils
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    companion object{
        private var loginInstance: LoginFragment? = null

        fun getInstance(): LoginFragment {
            if (loginInstance == null) {
                loginInstance = LoginFragment()
            }
            return loginInstance as LoginFragment
        }
    }
    lateinit var loginViewModel: LoginViewModel
    lateinit var callbackManager: CallbackManager
    var TAG="LoginFragment"
    private val PERMISSION_LIST = listOf("email", "public_profile")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        LogUtils.d("LognFragment","oncreatvew")
        return inflater.inflate(R.layout.fragment_login , container ,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        btnLogin.setOnClickListener {
            loginViewModel.loginEmailAuth("ab@gmail.com","12345679")
        }

        btnFbLogin.setOnClickListener {
            loginFbUser()
        }

        loginViewModel.getLoginStatus().observe(this, Observer { status ->
            when (status) {
                true -> {
                    Toast.makeText(activity, "registered", Toast.LENGTH_SHORT).show()
                    SharedPrefUtils.putBoolean(Constants.IS_LOGGED_IN, true)
                    SharedPrefUtils.putString(Constants.USER_ID, FirebaseAuth.getInstance().currentUser?.uid.toString())
                    startActivity(Intent(activity!!, HomeActivity::class.java))

                    activity!!.finish()
                }
                false -> Toast.makeText(activity, "failed", Toast.LENGTH_SHORT).show()
            }
        })

        loginViewModel.getProgresBar().observe(this, Observer { isLoading ->
            if(isLoading) pgbLogin.visibility = View.VISIBLE else pgbLogin.visibility = View.GONE
        })

        loginViewModel.getFailureMessage().observe(this , Observer { exceptionMessage ->
            Toast.makeText(activity, "Failed : ${exceptionMessage}", Toast.LENGTH_SHORT).show()
        })
    }

    private fun init() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        callbackManager = CallbackManager.Factory.create();
    }

    private fun loginFbUser() {
        LoginManager.getInstance().loginBehavior = LoginBehavior.WEB_VIEW_ONLY;
        LoginManager.getInstance().registerCallback(
            callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    val token = result?.accessToken
                    Log.d(TAG, "onSuccess" + result?.accessToken)
                    loginViewModel.loginFbAuth(token!!)
                }

                override fun onCancel() {
                    Log.d(TAG, "onCancel")
                }
                override fun onError(error: FacebookException?) {
                    Log.d(TAG, "onError" + error?.message)
                    error?.printStackTrace()
                }
            })
        LoginManager.getInstance().logInWithReadPermissions(this, PERMISSION_LIST);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        LogUtils.d(TAG , "ONACTVTYRESULT")
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}