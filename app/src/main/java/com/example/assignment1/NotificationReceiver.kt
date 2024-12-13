package com.example.assignment1

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Build the notification
        val notification = NotificationCompat.Builder(context, "channel_id")
            .setContentTitle("Quiz Time!")
            .setContentText("Time to try the quiz!")
            .setSmallIcon(R.drawable.ic_notification) // Replace with your icon
            .build()

        // Show the notification
        notificationManager.notify(0, notification)
    }
}
