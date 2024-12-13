package com.example.assignment1

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.util.*
import java.util.concurrent.TimeUnit
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        // Find views
        val setTimeButton: MaterialButton = findViewById(R.id.setTimeButton)
        val selectedTimeText: TextView = findViewById(R.id.selectedTimeText)

        // Handle click on Set Time button
        setTimeButton.setOnClickListener {
            showTimePicker(selectedTimeText)
        }
    }

    // Show the TimePickerDialog and update the TextView with the selected time
    private fun showTimePicker(selectedTimeText: TextView) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            // When time is selected, update the TextView and schedule the notification
            val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            selectedTimeText.text = "Notification set for: $selectedTime"

            // Schedule the notification with the selected time
            scheduleNotification(selectedHour, selectedMinute)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    // Schedule the notification based on the selected time
    private fun scheduleNotification(hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        val currentTime = Calendar.getInstance()
        if (calendar.before(currentTime)) {
            // If the selected time has already passed today, schedule it for the next day
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val delayInMillis = calendar.timeInMillis - currentTime.timeInMillis

        // Create input data for WorkManager
        val inputData = workDataOf(
            "hour" to hour,
            "minute" to minute,
            "title" to "Trivia Time!", // Set the title for your notification
            "message" to "Here is your daily trivia question!" // Set the message
        )

        // Create a OneTimeWorkRequest to schedule the notification
        val workRequest = OneTimeWorkRequestBuilder<Notification>()
            .setInputData(inputData)  // Pass the custom data (hour, minute, title, message)
            .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)  // Set the delay
            .build()

        // Enqueue the work request to trigger the notification at the scheduled time
        WorkManager.getInstance(this).enqueue(workRequest)

        // Show a Toast message to confirm the notification is scheduled
        Toast.makeText(this, "Notification scheduled!", Toast.LENGTH_SHORT).show()
    }
}
