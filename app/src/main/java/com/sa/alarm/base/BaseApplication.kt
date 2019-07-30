package com.sa.alarm.base

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.room.Room
import com.sa.alarm.common.NetworkReceiver
import com.sa.alarm.db.database.AppDatabase
import com.sa.alarm.utils.SharedPrefUtils

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        registerNetworkReciver()
        SharedPrefUtils.getInstance(this,"PREF")
    }

    fun registerNetworkReciver() {
        registerReceiver(NetworkReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

}