package com.sa.alarm.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sa.alarm.R
import android.widget.RemoteViews
import android.content.Intent

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

        fun createNotification(title: String?, desc: String?, context: Context) {

            if (title != null) {
                createNotificationChannel(context)
                var mBuilder: NotificationCompat.Builder
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                } else {
                    mBuilder = NotificationCompat.Builder(context)
                }

                val intent = Intent("Accept")

                mBuilder
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(title)
                    .setContentText(desc)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .addAction(R.drawable.icon_lock , "SNOOZE", PendingIntent.getActivity(context , 0 ,intent,PendingIntent.FLAG_UPDATE_CURRENT ))
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

                mBuilder.setSound(uri)

//                val contentView = RemoteViews(context.getPackageName(), R.layout.notification_layout)
//
//                setListeners(contentView)//look at step 3


                val notificationManager = NotificationManagerCompat.from(context)
                val notificationId = (System.currentTimeMillis() / 4).toInt()
                var notification= mBuilder.build()

//                notification.contentView = contentView

                notificationManager.notify(notificationId, notification)
            }
        }

        private fun setListeners(contentView: RemoteViews) {

        }
    }
}