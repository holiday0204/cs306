package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assignment1.databinding.ActivityForgotPasswordBinding
import com.example.assignment1.databinding.ActivityHomeBinding
import com.example.assignment1.databinding.ActivitySignInBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.ForgotPasswordButton.setOnClickListener {
            val email = binding.etEmail.text.toString()
            auth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful){
                    //email is sent
                    Toast.makeText(this, "Email sent!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun checkEmail():Boolean {
        val email = binding.etEmail.text.toString()
        if (binding.etEmail.text.toString() == "") {
            binding.textInputLayoutEmail.error = "This is required field"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputLayoutEmail.error = "Check email format"
            return false
        }
        return true
    }
}
