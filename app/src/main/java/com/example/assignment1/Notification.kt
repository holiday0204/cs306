package com.example.assignment1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

// Worker subclass for background notifications
class Notification(context: Context, params: WorkerParameters) : Worker(context, params) {

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "quiz_notification_channel"
    private val appContext = context  // The context is passed through the constructor

    init {
        // Create notification channel for Android Oreo (API 26) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Quiz Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Called when the Worker runs in the background (scheduled notifications)
    override fun doWork(): Result {
        // Retrieve custom time data passed from inputData
        val hour = inputData.getInt("hour", 0)  // Default to 0 if not set
        val minute = inputData.getInt("minute", 0)  // Default to 0 if not set
        val title = inputData.getString("title") ?: "Quiz Time!"
        val message = inputData.getString("message") ?: "Here's your daily trivia question!"

        // Pass context to NotificationManagerHelper
        NotificationManagerHelper.sendNotification(appContext, "$title at $hour:$minute", message)

        // Save notification details to Firestore
        NotificationManagerHelper.saveNotificationToFirestore(title, message)

        return Result.success()
    }
}
