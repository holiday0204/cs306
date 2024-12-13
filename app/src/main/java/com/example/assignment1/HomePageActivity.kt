package com.example.assignment1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment1.databinding.ActivityHomePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomePageActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomePageBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = Firebase.auth

        // Get the current FirebaseUser
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // Fetch user info from Firestore using email or UID
            FireBaseClass().getUserInfo(user.email ?: "guest", object : FireBaseClass.UserInfoCallback {
                override fun onUserInfoFetched(userInfo: UserModel?) {
                    if (userInfo != null) {
                        // Set username in the TextView
                        binding.tvUserName.text = "Hi, ${userInfo.name}"
                    } else {
                        // If no user info found, display "Guest"
                        binding.tvUserName.text = "Hi, Guest"
                    }
                }
            })
        } else {
            // If the user is not logged in, display "Guest"
            binding.tvUserName.text = "Hi, Guest"
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
    }
}