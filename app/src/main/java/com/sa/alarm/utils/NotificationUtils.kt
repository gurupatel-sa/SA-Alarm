package com.sa.alarm.utils

/**
 * Created by guru on 1/8/19
 */

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sa.alarm.R
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.media.MediaPlayer
import android.media.AudioManager
import android.app.PendingIntent
import com.facebook.FacebookSdk.getApplicationContext
import com.sa.alarm.home.HomeActivity

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

                val notificationIntent = Intent(getApplicationContext(), HomeActivity::class.java)
                notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                val contentIntent = PendingIntent.getActivity(context, 15, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                mBuilder
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(title)
                    .setContentText(desc)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(contentIntent)
//                    .addAction(R.drawable.icon_lock , "SNOOZE", PendingIntent.getActivity(context , 0 ,intent,PendingIntent.FLAG_UPDATE_CURRENT ))

                val mediaPlayer = MediaPlayer()
                var ringtoneUri =Uri.parse("android.resource://" + context.packageName + "/" + R.raw.alarm)
                mediaPlayer.setDataSource(context.applicationContext, ringtoneUri)
                mediaPlayer.setAudioAttributes(AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_ALARM).build())
                mediaPlayer.isLooping = false
                mediaPlayer.setOnCompletionListener {
                    mediaPlayer.stop()
                    mediaPlayer.release()
                }
                mediaPlayer.setOnPreparedListener { mediaPlayer.start() }
                mediaPlayer.prepareAsync()

//                val contentView = RemoteViews(context.getPackageName(), R.layout.notification_layout)
//                setListeners(contentView)

                val notificationManager = NotificationManagerCompat.from(context)
                val notificationId = (System.currentTimeMillis() / 4).toInt()
                var notification= mBuilder.build()

//                notification.contentView = contentView

                notificationManager.notify(notificationId, notification)
            }
        }

    }
}