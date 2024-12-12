package com.example.assignment1

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment1.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import org.mindrot.jbcrypt.BCrypt

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding
    private val firestore = FirebaseFirestore.getInstance()

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
            val accountName = binding.etName.text.toString()

            if (checkAllField()) {
                // Firebase sign-up logic
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = FirebaseAuth.getInstance().currentUser?.uid
                            if (userId != null) {
                                // Hash the password before storing it
                                val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
                                val dob = binding.editTextRegisterDob.text.toString()

                                val selectedGenderId = binding.radioGroupGender.checkedRadioButtonId
                                val gender = when (selectedGenderId) {
                                    R.id.radioMale -> "Male"
                                    R.id.radioFemale -> "Female"
                                    R.id.radioOther -> "Other"
                                    else -> "" // Default empty if no gender is selected
                                }

                                // Create a user HashMap with values of type Any
                                val user = hashMapOf<String, Any>(
                                    "email" to email,
                                    "password" to hashedPassword,  // Store hashed password
                                    "userId" to userId,
                                    "name" to accountName,
                                    "gender" to gender,
                                    "dob" to dob,
                                    "createdAt" to System.currentTimeMillis()
                                )
                                Log.d(TAG, "User to be saved: $user")
                                saveUserToFirestore(userId, user)
                            } else {
                                Toast.makeText(
                                    this,
                                    "Error: User ID is null",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            // Account creation failed
                            Log.e(TAG, "SignUpError: ${task.exception}")
                            Toast.makeText(
                                this,
                                "Account creation failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    // Function to save user data to Firestore (now outside onCreate)
    private fun saveUserToFirestore(userId: String, user: HashMap<String, Any>) {
        // Add the user to the `users` collection with the userId as the document ID
        firestore.collection("users")
            .document(userId) // Use the Firebase Authentication UID as the document ID
            .set(user)
            .addOnSuccessListener {
                Log.d(TAG, "User added to Firestore with ID: $userId")
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                auth.signOut()
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding user to Firestore", e)
                Toast.makeText(this, "Error saving user data: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
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
