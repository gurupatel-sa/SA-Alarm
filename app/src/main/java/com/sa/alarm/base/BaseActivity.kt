package com.sa.alarm.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.sa.alarm.utils.LogUtils
import com.sa.alarm.utils.NetworkUtils

abstract class BaseActivity : AppCompatActivity() {
    private val TAG: String = this.javaClass.getSimpleName()


    private lateinit var networkReceiver: NetworkReceiver
    private var alertDialog :AlertDialog? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.d(TAG, "onCreate : base")
    }

    fun isNetworkConnected() : Boolean{
        return NetworkUtils.isOnline
    }

    override fun onResume() {
        super.onResume()
//        registerNetworkReciver()
    }

    override fun onPause() {
        super.onPause()
//        unregisterReceiver(networkReceiver)

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun registerNetworkReciver() {
        LogUtils.d(TAG,"registerNetworkReciver :base")
        networkReceiver = NetworkReceiver()
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    fun showNetworkAlert() {
       alertDialog = AlertDialog.Builder(this)
            .setTitle("please Check Your Internet Connection")
            .show()
    }

    inner class NetworkReceiver : BroadcastReceiver() {
        private val TAG: String = this.javaClass.getSimpleName()

        override fun onReceive(context: Context?, bundle: Intent?) {
            LogUtils.d(TAG, "onReceive :")

            if (!isOnline(context!!)) {
                showNetworkAlert()
            }
            else{
                alertDialog?.cancel()
            }
        }

        private fun isOnline(context: Context): Boolean {
            try {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val netInfo = cm.activeNetworkInfo

                NetworkUtils.isOnline = netInfo != null && netInfo.isConnected

                return netInfo != null && netInfo.isConnected
            } catch (e: NullPointerException) {
                e.printStackTrace()
                NetworkUtils.isOnline=false
                return false
            }
        }
    }

}