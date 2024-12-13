package com.example.assignment1
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.app.TimePickerDialog
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.util.*

class NotificationActivity : AppCompatActivity() {

    private lateinit var setTimeButton: MaterialButton
    private lateinit var selectedTimeText: TextView
    private lateinit var btnHomePage: MaterialButton  // Add reference for the Home Page button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        setTimeButton = findViewById(R.id.setTimeButton)
        selectedTimeText = findViewById(R.id.selectedTimeText)
        btnHomePage = findViewById(R.id.btnHomePage)  // Initialize the button

        // Navigate back to HomePageActivity when the button is clicked
        btnHomePage.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }

        // Existing TimePickerDialog setup
        setTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                selectedTimeText.text = "Notification set for: $selectedHour:$selectedMinute"

                val timeInMillis = calculateTimeInMillis(selectedHour, selectedMinute)
                setNotificationAlarm(timeInMillis)
            }, hour, minute, true)

            timePickerDialog.show()
        }
    }

    private fun calculateTimeInMillis(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }

    private fun setNotificationAlarm(timeInMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }
}
