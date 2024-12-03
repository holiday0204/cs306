package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment1.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater) // Use correct binding
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Remove action bar
        supportActionBar?.hide()

        // Handle sign-in button click
        binding.SignInButton.setOnClickListener {
            if (checkAllFields()) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successfully signed in", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomePageActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Log detailed Firebase error message
                        Log.e("SignInError", "Sign-in failed: ${task.exception?.message}")
                        Toast.makeText(this, "Sign-in failed. Check your credentials.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.tvCreateAccount.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        // Check if the user is signed in
        val currentUser = auth.currentUser

        // If a user is signed in, navigate to the next activity
        if (currentUser != null) {
            updateUI(currentUser)  // Optionally update UI for the signed-in user
            val intent = Intent(this, QuizSetupActivity::class.java)
            startActivity(intent)
            finish()  // Close this SignInActivity
        }
    }
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // Optional: You can navigate to the next screen or show something based on the user.
            val intent = Intent(this, QuizSetupActivity::class.java)
            startActivity(intent)
            finish()  // Close the current SignInActivity
        } else {
            // Handle the case where there is no signed-in user (e.g., show sign-in screen)
            Toast.makeText(this, "No user signed in", Toast.LENGTH_SHORT).show()
        }
    }



    // Function to validate input fields
    private fun checkAllFields(): Boolean {
        email = binding.etEmail.text.toString()
        password = binding.etPassword.text.toString()

        if (email.isEmpty()) {
            binding.textInputLayoutEmail.error = "This is a required field"
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputLayoutEmail.error = "Check email format"
            return false
        }

        if (password.isEmpty()) {
            binding.textInputLayoutPassword.error = "Password cannot be empty"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }

        if (password.length <= 6) {
            binding.textInputLayoutPassword.error = "Password should be at least 8 characters"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }

        return true
    }
}
