package com.sa.alarm.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sa.alarm.utils.LogUtils
import android.net.ConnectivityManager
import com.sa.alarm.utils.NetworkUtils

class NetworkReceiver : BroadcastReceiver() {
    private val TAG: String = this.javaClass.getSimpleName()

    override fun onReceive(context: Context?, bundle: Intent?) {
        LogUtils.d(TAG, "onReceive :")

        NetworkUtils.isOnline = isConneted(context!!)
        LogUtils.d(TAG, "onReceive : networkInfo " + NetworkUtils.isOnline)
    }

    fun isConneted(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}