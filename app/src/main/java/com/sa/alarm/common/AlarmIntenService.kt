package com.sa.alarm.common

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.sa.alarm.R
import com.sa.alarm.utils.LogUtils

class AlarmIntenService : IntentService("Alarm") {
    private val TAG: String = this.javaClass.getSimpleName()

    override fun onHandleIntent(p0: Intent?) {
        LogUtils.d(TAG, "onHandleIntent :")

        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel()
            } else {
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId )
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(101, notification)

    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.d(TAG,"onDestroy :")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d(TAG ,"onStartCommand :")
        return Service.START_STICKY
    }

    private fun createNotificationChannel(): String{
        val channelId = "my_service"
        val channelName = "My Background Service"
        val chan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                channelId,
                channelName, NotificationManager.IMPORTANCE_HIGH
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan.importance = NotificationManager.IMPORTANCE_HIGH
        }
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            service.createNotificationChannel(chan)
        }
        return channelId
    }
}