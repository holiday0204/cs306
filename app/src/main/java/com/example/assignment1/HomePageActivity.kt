package com.example.assignment1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.assignment1.databinding.ActivityHomePageBinding
import com.example.assignment1.FireBaseClass
import com.example.assignment1.UserModel
import com.example.assignment1.QuizClass
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

        // Fetch and set the user name and profile image from Firebase
        FireBaseClass().getUserInfo(object : FireBaseClass.UserInfoCallback {
            override fun onUserInfoFetched(userInfo: UserModel?) {
                binding.tvUserName.text = "Hi, " + userInfo?.name
                FireBaseClass().setProfileImage(userInfo?.image, binding.mainProfileImage)
            }
        })

        // Button for random quiz
        binding.btnRandomQuiz.setOnClickListener {
            startActivity(Intent(this, RandomQuestionActivity::class.java))
        }

        // Button for custom quiz
        binding.btnCustomQuiz.setOnClickListener {
            startActivity(Intent(this, QuizSetupActivity::class.java))
        }

        binding.settingsButton.setOnClickListener {

            startActivity(Intent(this, SettingActivity ::class.java))
        }

        fun fetchUserDetails() {

            FireBaseClass().getUserInfo(object : FireBaseClass.UserInfoCallback {
                override fun onUserInfoFetched(userInfo: UserModel?) {
                    if (userInfo != null) {
                        binding.tvUserName.text = "Hi, ${userInfo.name}"

                        FireBaseClass().setProfileImage(userInfo.image, binding.mainProfileImage)
                    } else {
                        binding.tvUserName.text = "Hi, Guest"
                    }
                }
            })
        }

        fetchUserDetails()
    }
}
