package com.example.assignment1

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class QuizSetupActivity : AppCompatActivity() {
    lateinit var category: Spinner
    lateinit var difficulty: Spinner
    lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_setup)

        // Initialize the category and difficulty spinners
        category = findViewById(R.id.topicSpinner)
        difficulty = findViewById(R.id.difficultySpinner)
        text = findViewById(R.id.questionTypeLabel)

        // Fetch the string arrays from resources
        val categoriesArray = resources.getStringArray(R.array.categories_array)
        val difficultyLevelsArray = resources.getStringArray(R.array.difficulty_levels)

        // Create ArrayAdapters for category and difficulty using the string arrays from resources
        val categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categoriesArray
        )
        val difficultyAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            difficultyLevelsArray
        )

        // Set the layouts to use when the list of choices appears
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapters to the Spinners
        category.adapter = categoryAdapter
        difficulty.adapter = difficultyAdapter

        // Set a listener to handle item selection for the category Spinner
        category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                text.text = "Please Select a Category"
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Get the selected item from the adapter
                val selectedCategory = parent?.getItemAtPosition(position).toString()
                text.text = "Category Selected: $selectedCategory"
            }
        }

        // Set a listener to handle item selection for the difficulty Spinner
        difficulty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                text.text = "Please Select a Difficulty Level"
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Get the selected item from the adapter
                val selectedDifficulty = parent?.getItemAtPosition(position).toString()
                text.text = "Difficulty Selected: $selectedDifficulty"
            }
        }
    }
}

