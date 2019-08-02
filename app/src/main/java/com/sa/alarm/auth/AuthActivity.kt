package com.sa.alarm.auth

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.sa.alarm.R
import com.sa.alarm.base.BaseActivity
import com.sa.alarm.login.LoginFragment
import com.sa.alarm.register.RegisterFragment
import kotlinx.android.synthetic.main.activity_auth.*
import com.google.firebase.auth.FirebaseAuth
import com.sa.alarm.utils.LogUtils

class AuthActivity : BaseActivity() {

    private val TAG : String = this.javaClass.getSimpleName()
    private lateinit var fragmentManager : FragmentManager
    private val LOGIN_FRAGMENT_TAG="Login"
    private val REGISTER_FRAGMENT_TAG="Register"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        init()
        tvRegister.setOnClickListener {
            tvRegister.isSelected = true
            tvLogin.isSelected = false
            fragmentManager.beginTransaction().replace(R.id.fmAuth,RegisterFragment.getInstance() ,REGISTER_FRAGMENT_TAG).commit()
        }

        tvLogin.setOnClickListener {
            tvLogin.isSelected = true
            tvRegister.isSelected = false
            fragmentManager.beginTransaction().replace(R.id.fmAuth,LoginFragment.getInstance() ,LOGIN_FRAGMENT_TAG).commit()
        }
    }

    private fun init() {
        tvLogin.isSelected = true
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fmAuth,LoginFragment() ,LOGIN_FRAGMENT_TAG).commit()
    }


    override fun onStart() {
        super.onStart()
        LogUtils.d(TAG,"onStart : Auth :${FirebaseAuth.getInstance().currentUser?.uid }")
    }
}
