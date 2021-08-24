package com.example.custom_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val CHANNEL = "com.timus/notification"
    private val ChannelId = "PersonalNotification"
    private val NotificationId = 1

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger,CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "callNotification"){
                    displayNotitivations();
            }
        }
    }

    class ActionReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("--------------", "---------------------------------------------------")
            Log.d("Hello", "World")
        }

        companion object {
            private const val TAG = "ActionReceiver"
        }
    }


    fun displayNotitivations() {
        val snoozeIntent = Intent(this, ActionReceiver::class.java)
        val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(applicationContext, 0, snoozeIntent, 0)
        createNotificationChannel()
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, ChannelId)
        builder.setSmallIcon(R.drawable.res_app_icon)
        builder.setContentText("This is Simple Notification")
        builder.setContentTitle("Simple Notification")
        builder.setPriority(NotificationCompat.PRIORITY_HIGH)
        builder.addAction(R.drawable.res_app_icon, "Confirm", snoozePendingIntent)
        builder.addAction(R.drawable.res_app_icon, "Dismissed", snoozePendingIntent)
        val notificationManagerCompat: NotificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(NotificationId, builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Personal Notification"
            val description = "This is Personal Notification"
            val importance: Int = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(ChannelId, name, importance)
            channel.setDescription(description)
            val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
