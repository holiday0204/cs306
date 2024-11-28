package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment1.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the view binding and set it to the activity
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Set up button click listener for signing up
        binding.SignUpButton.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (checkAllField()) {
                // Firebase sign-up logic
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Successful account creation and sign-in
                        auth.signOut()
                        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SignInActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Account creation failed
                        Log.e("SignUpError", it.exception.toString())
                    }
                }
            }
        }
    }

    // Function to validate input fields
    private fun checkAllField(): Boolean {
        val email = binding.etEmail.text.toString()

        if (email.isEmpty()) {
            binding.textInputLayoutEmail.error = "This is a required field"
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputLayoutEmail.error = "Check email format"
            return false
        }

        if (binding.etPassword.text.toString().isEmpty()) {
            binding.textInputLayoutPassword.error = "Password cannot be empty"
            return false
        }

        if (binding.etPassword.length() < 8) {
            binding.textInputLayoutPassword.error = "Password should be at least 8 characters"
            return false
        }

        if (binding.etConfirmPassword.text.toString().isEmpty()) {
            binding.textInputLayoutConfirmPassword.error = "This is a required field"
            return false
        }

        if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()) {
            binding.textInputLayoutPassword.error = "Passwords do not match"
            binding.textInputLayoutConfirmPassword.error = "Passwords do not match"
            return false
        }

        return true
    }
}
