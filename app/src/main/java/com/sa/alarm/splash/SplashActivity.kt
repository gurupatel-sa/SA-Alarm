package com.sa.alarm.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.sa.alarm.R
import com.sa.alarm.auth.AuthActivity
import com.sa.alarm.common.Constants
import com.sa.alarm.home.HomeActivity
import com.sa.alarm.utils.SharedPrefUtils

class SplashActivity : AppCompatActivity() {
    private val TAG : String = this.javaClass.getSimpleName()
    private val DELAY : Long=3000

    private lateinit var runnable : Runnable
    private var handler: Handler? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        init()

        runnable= Runnable {
            if(!isFinishing){
                val isLoggedIn = SharedPrefUtils.getBoolean(Constants.IS_LOGGED_IN)
                if(isLoggedIn){
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                else{
                    val intent = Intent(this, AuthActivity::class.java)
                    startActivity(intent)
                }
                finish()
            }
        }
        handler = Handler()

        handler?.postDelayed(runnable ,DELAY)

    }

    private fun init() {

    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacks(runnable)
        handler = null
    }
}
