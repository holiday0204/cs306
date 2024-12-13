package com.example.assignment1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

object NotificationManagerHelper {

    private const val CHANNEL_ID = "quiz_notification_channel"

    // Function to create the notification channel (if needed)
    fun createNotificationChannel(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Quiz Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Function to send a notification
    fun sendNotification(context: Context, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(0, notification)
    }

    // Function to save notification details to Firestore
    fun saveNotificationToFirestore(title: String, message: String) {
        val timestamp = System.currentTimeMillis()
        val notification = NotificationModel(title, message, timestamp)

        val user = FirebaseAuth.getInstance().currentUser
        val normalizedEmail = user?.email?.replace(".", "")?.replace("@", "_") ?: ""
        val db = FirebaseFirestore.getInstance()
        val userNotificationsRef = db.collection("user_notifications").document(normalizedEmail)

        userNotificationsRef.collection("notifications")
            .add(notification)
            .addOnSuccessListener {
                Log.d("Notification", "Notification saved successfully!")
            }
            .addOnFailureListener { e ->
                Log.w("Notification", "Error saving notification", e)
            }
    }
}
