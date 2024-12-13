package com.example.assignment1

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.assignment1.databinding.ActivityHomePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.material.imageview.ShapeableImageView

class HomePageActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomePageBinding
    private val firebase = FireBaseClass()
    private var imageUri: Uri? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        auth = Firebase.auth

        // Load existing profile image
        val user = auth.currentUser
        user?.email?.let { email ->
            val userImageRef = "profile_pictures/$email"
            firebase.setProfileImage(userImageRef, binding.profileImageView)
        }

        // Set click listener to change the profile image
        binding.profileImageView.setOnClickListener {
            openImagePicker()
        }

        // Button actions
        binding.btnRandomQuiz.setOnClickListener {
            startActivity(Intent(this, RandomQuestionActivity::class.java))
        }

        binding.btnCustomQuiz.setOnClickListener {
            startActivity(Intent(this, QuizSetupActivity::class.java))
        }

        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        binding.btnViewQuizHistory.setOnClickListener {
            val intent = Intent(this, QuizHistoryActivity::class.java)
            startActivity(intent)
        }

        binding.notificationButton.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        // Create Notification Channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "channel_id"
            val channelName = "Default Channel"
            val channelDescription = "Channel for notifications"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = channelDescription
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        if (user != null) {
            sendWelcomeNotification(user.displayName ?: "User")
            firebase.getUserInfo(user.email ?: "guest", object : FireBaseClass.UserInfoCallback {
                override fun onUserInfoFetched(userInfo: UserModel?) {
                    binding.tvUserName.text = if (userInfo != null) {
                        "Hi, ${userInfo.name}"
                    } else {
                        "Hi, Guest"
                    }
                }
            })
        } else {
            binding.tvUserName.text = "Hi, Guest"
        }
    }

    // Open image picker to select a new image
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            binding.profileImageView.setImageURI(uri) // Display selected image temporarily
            uploadProfileImage(uri)
        }
    }

    private fun openImagePicker() {
        imagePickerLauncher.launch("image/*")
    }

    private fun uploadProfileImage(uri: Uri) {
        val userEmail = auth.currentUser?.email
        if (userEmail != null) {
            firebase.uploadImage(uri, userEmail)
        } else {
            Log.e("HomePage", "User email is null")
        }
    }

    // Function to send the "Welcome Back" notification
    private fun sendWelcomeNotification(userName: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle("Welcome Back, $userName!")
            .setContentText("It's time for the quiz!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        notificationManager.notify(0, notification)
    }
}