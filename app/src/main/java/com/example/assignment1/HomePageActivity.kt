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

        // Remove the action bar
        supportActionBar?.hide()

        // Initialize FirebaseAuth instance
        auth = Firebase.auth

        // Fetch and set the user name and profile image from Firebase
        FireBaseClass().getUserInfo(object : FireBaseClass.UserInfoCallback {
            override fun onUserInfoFetched(userInfo: UserModel?) {
                binding.tvUserName.text = "Hi, " + userInfo?.name
                FireBaseClass().setProfileImage(userInfo?.image, binding.mainProfileImage)
            }
        })

        // Set up the RecyclerView for quiz categories
        val rvCategoryList = binding.rvCategoryList
        rvCategoryList.layoutManager = GridLayoutManager(this, 2)
        val quizClass = QuizClass(this)
        quizClass.setRecyclerView(rvCategoryList)

        // Button for random quiz
        binding.btnRandomQuiz.setOnClickListener {
            quizClass.getQuizList(10, null, null, null)
        }

        // Button for custom quiz
        binding.btnCustomQuiz.setOnClickListener {
            startActivity(Intent(this, QuizSetupActivity::class.java))
        }

        binding.settingsButton.setOnClickListener {
            // Navigate to the SettingsActivity when the Settings button is clicked
            startActivity(Intent(this, SettingActivity ::class.java))
        }


//        // Profile image click to navigate to user profile
//        binding.mainProfileImage.setOnClickListener {
//            startActivity(Intent(this, UserProfileActivity::class.java))
//        }

        fun fetchUserDetails() {
            // Ensure FireBaseClass is correctly initialized and fetches user info
            FireBaseClass().getUserInfo(object : FireBaseClass.UserInfoCallback {
                override fun onUserInfoFetched(userInfo: UserModel?) {
                    // Check if the user information is retrieved successfully
                    if (userInfo != null) {
                        // Set the user's name in the TextView
                        binding.tvUserName.text = "Hi, ${userInfo.name}"

                        // Set the user's profile image if available
                        FireBaseClass().setProfileImage(userInfo.image, binding.mainProfileImage)
                    } else {
                        // If userInfo is null, show "Hi, Guest"
                        binding.tvUserName.text = "Hi, Guest"
                    }
                }

                // You can add error handling if needed, such as onFailure or onError, but that's optional here.
            })
        }

// Call the function to fetch the user details
        fetchUserDetails()

    }
}
