package com.sa.alarm.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.sa.alarm.R
import com.sa.alarm.base.BaseActivity
import com.sa.alarm.home.reminder.ReminderFragment

class HomeActivity : BaseActivity() {

    companion object{
        private val TAG: String = this.javaClass.getSimpleName()
    }
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var fragmentManager: FragmentManager
    private val DASHBOARD_FRAGMENT_TAG: String = "users_fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        init()

    }

    private fun init() {
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fmHome, ReminderFragment.getInstance(),
            ReminderFragment.TAG ).commit()

    }

    //replaces fragment from other fragment
    fun replaceFragments(TAG: String, f: Fragment) {
        fragmentManager.beginTransaction().replace(R.id.fmHome, f, TAG).addToBackStack(TAG).commit()
    }

    fun removeFragment(TAG: String, f: Fragment) {
        fragmentManager.beginTransaction().remove(f).commit()
    }


    override fun onStart() {
        super.onStart()
    }
}
