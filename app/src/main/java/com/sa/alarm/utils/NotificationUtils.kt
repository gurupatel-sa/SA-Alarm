package com.sa.alarm.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sa.alarm.R
import android.media.MediaPlayer


class NotificationUtils {

    companion object {
        var CHANNEL_ID: String = "SA_ALARM"
        var CHANNEL_NAME = "reminder"

        private fun createNotificationChannel(context: Context) {
            val channelName = CHANNEL_ID
            val channelDesc = "channelDesc"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
                channel.description = channelDesc

                val notificationManager = context.getSystemService(NotificationManager::class.java)!!
                val currChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
                if (currChannel == null)
                    notificationManager.createNotificationChannel(channel)
            }
        }

        fun createNotification(title:String? ,desc:String? ,context: Context) {

            if (title != null) {
                createNotificationChannel(context)

                val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(title)
                    .setContentText(desc)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

                mBuilder.setSound(uri)

                val notificationManager = NotificationManagerCompat.from(context)
                val notificationId = (System.currentTimeMillis() / 4).toInt()
                notificationManager.notify(notificationId, mBuilder.build())
            }
        }
    }
}