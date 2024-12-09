package com.example.assignment1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class QuizScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Retrieve the passed data from the Intent
        val selectedCategory = intent.getStringExtra("CATEGORY")
        val selectedDifficulty = intent.getStringExtra("DIFFICULTY")

        // Use the retrieved data (e.g., display it or use it to fetch questions)
        Toast.makeText(this, "Category: $selectedCategory, Difficulty: $selectedDifficulty", Toast.LENGTH_SHORT).show()
    }
}