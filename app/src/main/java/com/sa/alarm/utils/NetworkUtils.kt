package com.sa.alarm.utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import android.net.NetworkInfo
import android.net.NetworkCapabilities
import android.net.Network


class NetworkUtils {

    companion object {
        var isOnline: Boolean = true

        fun getNetworkInfo(context: Context): NetworkInfo? {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo
        }
    }
}