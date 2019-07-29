package com.sa.alarm.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FacebookAuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.sa.alarm.R
import com.sa.alarm.auth.AuthActivity
import com.sa.alarm.base.BaseActivity
import com.sa.alarm.common.Constants
import com.sa.alarm.login.LoginFragment
import com.sa.alarm.users.UsersFragment
import com.sa.alarm.utils.SharedPrefUtils
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    var TAG="HomeActivity"
     var auth :FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var fragmentManager : FragmentManager
    private val USERS_FRAGMENT_TAG :String ="users_fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        init()

        button3.setOnClickListener {
            SharedPrefUtils.remove(Constants.IS_LOGGED_IN)
            SharedPrefUtils.remove(Constants.USER_ID)

            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this , AuthActivity::class.java))
            finish()
        }

        loadUsersFragment.setOnClickListener {
            fragmentManager.beginTransaction().replace(R.id.fmHome, UsersFragment() ,USERS_FRAGMENT_TAG).commit()
        }
    }

    private fun init() {
        fragmentManager = supportFragmentManager
    }

    override fun onStart() {
        super.onStart()
    }
}
