package com.sa.alarm.auth.login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import android.util.Patterns
import android.text.TextUtils
import kotlinx.android.synthetic.main.dialog_forgot_password.*
import kotlinx.android.synthetic.main.fragment_login.etEmail
import android.view.inputmethod.InputMethodManager
import android.app.Activity

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

    private val TAG : String = this.javaClass.getSimpleName()
    private val PERMISSION_LIST = listOf("email", "public_profile")

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var callbackManager: CallbackManager
    private lateinit var dialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        LogUtils.d(TAG,"onCreateView")
        return inflater.inflate(R.layout.fragment_login , container ,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        btnLogin.setOnClickListener {
            if(isValid()){
                loginViewModel.loginEmailAuth(etEmail.text.toString(),etPassword.text.toString())
            }
        }

        btnFbLogin.setOnClickListener {
            loginFbUser()
        }

        tvForgotPassword.setOnClickListener {
            openForgotPasswordDialog()
        }

        loginViewModel.getLoginStatus().observe(this, Observer { status ->
            when (status) {
                true -> {
                    SharedPrefUtils.putBoolean(Constants.IS_LOGGED_IN, true)
                    SharedPrefUtils.putString(Constants.USER_ID, FirebaseAuth.getInstance().currentUser?.uid.toString())
                    startActivity(Intent(activity!!, HomeActivity::class.java))
                    activity!!.finish()
                }
            }
        })

        loginViewModel.getProgresBar().observe(this, Observer { isLoading ->
            if(isLoading) pgbLogin.visibility = View.VISIBLE else pgbLogin.visibility = View.GONE
        })

        loginViewModel.getFailureMessage().observe(this , Observer { exceptionMessage ->
            Toast.makeText(activity, "Failed : ${exceptionMessage}", Toast.LENGTH_SHORT).show()
        })

        loginViewModel.getResetPasswordStatus().observe(this , Observer {status ->
            if(status){
                dialog.dismiss()
                Toast.makeText(activity!!, getString(R.string.password_reset_successful), Toast.LENGTH_SHORT).show();
            }
        })
    }

    private fun openForgotPasswordDialog() {
        dialog = Dialog(context!!)
        dialog.setContentView(R.layout.dialog_forgot_password);

        dialog.btnForgotPassword.setOnClickListener {
            dismissKeyboard(activity!!)

            val email = dialog.etMail?.text.toString()

            if(dialogValidation(email , dialog)){
                loginViewModel.sendEmailForgotPassword(email)
            }

        }
        val window = dialog.getWindow()
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        dialog.show()
    }

    fun dismissKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (null != activity.currentFocus)
            imm.hideSoftInputFromWindow(
                activity.currentFocus!!
                    .applicationWindowToken, 0
            )
    }

    private fun dialogValidation(email: String ,dialog:Dialog): Boolean {
        if(email.isEmpty()){
            dialog.etMail.error = resources.getString(R.string.error_email_empty)
            return false
        }
        else if(!isValidEmail(email)){
            dialog.etMail.error = resources.getString(R.string.error_email_invalid)
            return false
        }
        return true
    }

    private fun isValid(): Boolean {
        val email = etEmail.text.toString()
        val password =etPassword.text.toString()
        if(email.isEmpty()){
            etEmail.error = resources.getString(R.string.error_email_empty)

            return false
        }
        else if(!isValidEmail(email)){
            etEmail.error = resources.getString(R.string.error_email_invalid)

            return false
        }
        else if(password.isEmpty()){
            tilPassword.error=resources.getString(R.string.error_password_empty)

            return false
        }
        else if(password.length < 8){
            tilPassword.error=resources.getString(R.string.error_password_invalid)

            return false
        }
        tilPassword.isErrorEnabled=false
        return true
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
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
                    LogUtils.d(TAG,"loginFb : onSuccess : ${token}")
                    loginViewModel.loginFbAuth(token!!)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}