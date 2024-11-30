package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment1.databinding.ActivityHomePageBinding
import com.google.firebase.auth.EmailAuthProvider
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
        binding.UpdatePasswordButton.setOnClickListener {
            val user = auth.currentUser
            val password = binding.etPassword.text.toString()
            if (checkPasswordField()) {
                user?.updatePassword(password)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Update successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("error", it.exception.toString())
                    }
                }

            }
        }
        binding.UpdateEmailButton.setOnClickListener {
            val user = auth.currentUser
            val email = binding.etEmail.text.toString()

            if (checkEmailField()) {
                // Re-authenticate the user
                val credential = EmailAuthProvider.getCredential(user?.email ?: "", binding.etPassword.text.toString()) // Use password to re-authenticate
                user?.reauthenticate(credential)?.addOnCompleteListener { reAuthTask ->
                    if (reAuthTask.isSuccessful) {
                        // Once re-authentication is successful, update the email
                        user.updateEmail(email).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Email updated successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Log.e("error", task.exception.toString())
                            }
                        }
                    } else {
                        Log.e("error", "Re-authentication failed: ${reAuthTask.exception?.message}")
                    }
                }
            }
        }

    }


    private fun checkPasswordField(): Boolean {
        if (binding.etPassword.text.toString() == "") {
            binding.textInputLayoutPassword.error = "This is required field"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        if (binding.etPassword.length() <= 6) {
            binding.textInputLayoutPassword.error = "Password should at least 7 characters long"
            binding.textInputLayoutPassword.errorIconDrawable = null
        }
        return true
    }

    private fun checkEmailField(): Boolean {
        val email = binding.etEmail.text.toString() // Assign email

        if (binding.etEmail.text.toString() == "") {
            binding.textInputLayoutEmail.error = "This is a required field"
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputLayoutEmail.error = "Check email format"
            return false
        }
        return true

    }
}