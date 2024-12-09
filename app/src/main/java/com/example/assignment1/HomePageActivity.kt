package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment1.databinding.ActivityHomePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomePageActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Remove the action bar
        supportActionBar?.hide()

        // Initialize FirebaseAuth instance
        auth = Firebase.auth

        binding.startQuizButton.setOnClickListener {
            val intent = Intent(this, QuizSetupActivity::class.java)
            startActivity(intent)
        }

        // Set up the Sign-Out button
        binding.SignOutButton.setOnClickListener {
            // Sign out the user from Firebase
            auth.signOut()

            // Navigate back to the Sign-In screen
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

            // Finish the current activity
            finish()
        }
    }
}