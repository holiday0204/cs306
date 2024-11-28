package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment1.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
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

        // Remove action bar
        supportActionBar?.hide()

        auth = Firebase.auth

        // Handle sign-in button click
        binding.SignInButton.setOnClickListener {
            if (checkAllFields()) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successfully signed in", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, QuizSetupActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("Error", task.exception.toString())
                        Toast.makeText(this, "Sign-in failed. Check your credentials.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.tvCreateAccount.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.tvForgotPassword.setOnClickListener{
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun checkAllFields(): Boolean {
        email = binding.etEmail.text.toString() // Assign email

        if (binding.etEmail.text.toString() == "") {
            binding.textInputLayoutEmail.error = "This is a required field"
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputLayoutEmail.error = "Check email format"
            return false
        }

        if (binding.etPassword.text.toString() == "") {
            binding.textInputLayoutPassword.error = "Password cannot be empty"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }

        if (binding.etPassword.length() <= 6) {
            binding.textInputLayoutPassword.error = "Password should be at least 8 characters"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        return true
    }
}

