package com.sa.alarm.home

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.sa.alarm.R
import com.sa.alarm.base.BaseActivity
import com.sa.alarm.dashboard.DashboardFragment

class HomeActivity : BaseActivity() {

    var TAG="HomeActivity"
     var auth :FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var fragmentManager : FragmentManager
    private val DASHBOARD_FRAGMENT_TAG :String ="users_fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        init()

//        button3.setOnClickListener {
//            SharedPrefUtils.remove(Constants.IS_LOGGED_IN)
//            SharedPrefUtils.remove(Constants.USER_ID)
//
//            FirebaseAuth.getInstance().signOut()
//            startActivity(Intent(this , AuthActivity::class.java))
//            finish()
//        }
//
//        loadUsersFragment.setOnClickListener {
//            fragmentManager.beginTransaction().replace(R.id.fmHome, DashboardFragment() ,USERS_FRAGMENT_TAG).commit()
//        }
    }

    private fun init() {
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fmHome, DashboardFragment() ,DASHBOARD_FRAGMENT_TAG).commit()

    }

    override fun onStart() {
        super.onStart()
    }
}
