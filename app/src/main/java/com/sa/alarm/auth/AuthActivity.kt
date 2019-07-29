package com.sa.alarm.auth

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.sa.alarm.R
import com.sa.alarm.base.BaseActivity
import com.sa.alarm.login.LoginFragment
import com.sa.alarm.register.RegisterFragment
import kotlinx.android.synthetic.main.activity_auth.*
import android.content.Intent
import android.util.Log
import com.facebook.FacebookSdk
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : BaseActivity() {

    private lateinit var mFragmentManager : FragmentManager
    private val LOGIN_FRAGMENT_TAG="Login"
    private val REGISTER_FRAGMENT_TAG="Register"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        init()
        tvRegister.setOnClickListener {
            mFragmentManager.beginTransaction().replace(R.id.fmAuth,RegisterFragment.getInstance() ,REGISTER_FRAGMENT_TAG).commit()
        }

        tvLogin.setOnClickListener {
            mFragmentManager.beginTransaction().replace(R.id.fmAuth,LoginFragment.getInstance() ,LOGIN_FRAGMENT_TAG).commit()
        }

    }

    private fun init() {
        tvLogin.isSelected = true
        mFragmentManager = supportFragmentManager
        mFragmentManager.beginTransaction().replace(R.id.fmAuth,LoginFragment() ,LOGIN_FRAGMENT_TAG).commit()
    }


    override fun onStart() {
        super.onStart()
        Log.d("aUTHaVTIVITY " ," auth" + FirebaseAuth.getInstance().currentUser?.uid )
    }
}
