package com.sa.alarm.base

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.sa.alarm.common.NetworkReceiver

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
//        registerNetworkReciver()
    }

    fun registerNetworkReciver() {
        registerReceiver(NetworkReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

}