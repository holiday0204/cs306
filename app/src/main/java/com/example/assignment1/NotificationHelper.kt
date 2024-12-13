package com.example.assignment1

import android.content.Context

class NotificationHelper(private val context: Context) {

    init {
        // Create notification channel when the class is initialized
        NotificationManagerHelper.createNotificationChannel(context)
    }

    // Function to send the notification with quiz result (immediate notification)
    fun sendQuizNotification(score: Double, userEmail: String) {
        val message = "You got ${score.toInt()} marks. Well done! Your score has been updated."
        // Pass the context to NotificationManagerHelper
        NotificationManagerHelper.sendNotification(context, "Quiz Completed", message)

        // Save the notification details to Firestore
        NotificationManagerHelper.saveNotificationToFirestore("Quiz Completed", message)
    }
}
