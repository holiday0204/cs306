package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.assignment1.databinding.ActivitySettingBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize FirebaseAuth instance
        auth = Firebase.auth
        // Setup the toolbar as the action bar
        val toolbar: Toolbar = findViewById(R.id.materialToolbar)
        setSupportActionBar(toolbar)

        // Enable the "up" button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        binding.SignOutButton.setOnClickListener {
            // Sign out the user from Firebase
            auth.signOut()

            // Navigate back to the Sign-In screen
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

            // Finish the current activity
            finish()
        }

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Go back to the previous activity when the up button (back button) is pressed
                onBackPressed() // This is the default action to go back
                return true
            }
            else -> return super.onOptionsItemSelected(item)
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
