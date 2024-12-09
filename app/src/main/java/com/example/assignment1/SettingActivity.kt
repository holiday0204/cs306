package com.example.assignment1

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment1.databinding.ActivitySettingBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize FirebaseAuth instance
        auth = Firebase.auth

        // Set up Update Password button
        binding.UpdatePasswordButton.setOnClickListener {
            val user = auth.currentUser
            val password = binding.etPassword.text.toString()
            if (checkPasswordField()) {
                user?.updatePassword(password)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("error", it.exception.toString())
                    }
                }
            }
        }

        // Set up Update Email button
        binding.UpdateEmailButton.setOnClickListener {
            val user = auth.currentUser
            val email = binding.etEmail.text.toString()

            if (checkEmailField()) {
                val credential = EmailAuthProvider.getCredential(user?.email ?: "", binding.etPassword.text.toString())
                user?.reauthenticate(credential)?.addOnCompleteListener { reAuthTask ->
                    if (reAuthTask.isSuccessful) {
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
        if (binding.etPassword.text.toString().isEmpty()) {
            binding.textInputLayoutPassword.error = "This is a required field"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        if (binding.etPassword.length() <= 6) {
            binding.textInputLayoutPassword.error = "Password should be at least 7 characters long"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        return true
    }

    private fun checkEmailField(): Boolean {
        val email = binding.etEmail.text.toString()
        if (email.isEmpty()) {
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