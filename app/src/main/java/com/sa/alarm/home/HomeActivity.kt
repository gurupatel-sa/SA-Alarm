package com.sa.alarm.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FacebookAuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.sa.alarm.R
import com.sa.alarm.auth.AuthActivity
import com.sa.alarm.base.BaseActivity
import com.sa.alarm.common.Constants
import com.sa.alarm.utils.SharedPrefUtils
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    var TAG="HomeActivity"
     var auth :FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        button3.setOnClickListener {
            SharedPrefUtils.remove(Constants.IS_LOGGED_IN)
            SharedPrefUtils.remove(Constants.USER_ID)

            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this , AuthActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG , "onStart : auth.currentUser?.uid" + auth.currentUser?.uid)


    }
}
