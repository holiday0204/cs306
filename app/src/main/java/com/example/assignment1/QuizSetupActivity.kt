package com.example.assignment1

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuizSetupActivity : AppCompatActivity() {
    lateinit var category: Spinner
    lateinit var difficulty: Spinner
    lateinit var numQuestions: Spinner  // Spinner for number of questions
    lateinit var text: TextView
    lateinit var startButton: Button  // Declare the start button
    lateinit var categoryAdapter: ArrayAdapter<String>  // Adapter for categories
    lateinit var categories: List<CategoryModel>  // List to store categories

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_setup)

        // Initialize the category, difficulty, and number of questions spinners
        category = findViewById(R.id.topicSpinner)
        difficulty = findViewById(R.id.difficultySpinner)
        numQuestions = findViewById(R.id.numOfQuestionSpinner)  // Initialize the new Spinner
        text = findViewById(R.id.questionTypeLabel)
        startButton = findViewById(R.id.startButton)  // Initialize the start button

        // Set up Retrofit for the API call
        val retrofit = Retrofit.Builder()
            .baseUrl("https://opentdb.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: QuizService = retrofit.create(QuizService::class.java)

        // Fetch categories from the API
        service.getCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                if (response.isSuccessful) {
                    val categories = response.body()?.triviaCategories ?: emptyList()
                    val categoryNames = categories.map { it.name }

                    // Log the category names
                    Log.d("QuizSetupActivity", "Category Names: $categoryNames")

                    // Set up the adapter
                    categoryAdapter = ArrayAdapter(
                        this@QuizSetupActivity,
                        android.R.layout.simple_spinner_item,
                        categoryNames
                    )
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    // Set the adapter to the Spinner
                    category.adapter = categoryAdapter
                } else {
                    Toast.makeText(this@QuizSetupActivity, "Failed to load categories", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Toast.makeText(this@QuizSetupActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Fetch the string arrays from resources
        val difficultyLevelsArray = resources.getStringArray(R.array.difficulty_levels)

        // Create an ArrayAdapter for the difficulty spinner
        val difficultyAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            difficultyLevelsArray
        )

        // Create an ArrayAdapter for the number of questions (1 to 10)
        val numQuestionsArray = (1..10).map { it.toString() }.toTypedArray()
        val numQuestionsAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            numQuestionsArray
        )

        // Set the layouts to use when the list of choices appears
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        numQuestionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapters to the Spinners
        difficulty.adapter = difficultyAdapter
        numQuestions.adapter = numQuestionsAdapter  // Set the adapter to the numQuestions spinner

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
                val selectedDifficulty = parent?.getItemAtPosition(position).toString()
                text.text = "Difficulty Selected: $selectedDifficulty"
            }
        }

        // Set the OnClickListener for the Start button
        startButton.setOnClickListener {
            // Get the selected category, difficulty, and number of questions values
            val selectedCategory = category.selectedItem.toString()
            val selectedDifficulty = difficulty.selectedItem.toString()
            val selectedNumQuestions = numQuestions.selectedItem.toString()

            // Validate inputs
            if (selectedCategory.isEmpty() || selectedDifficulty.isEmpty() || selectedNumQuestions.isEmpty()) {
                Toast.makeText(this, "Please select all required fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Pass the selected data to the QuizActivity
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("CATEGORY", selectedCategory)
            intent.putExtra("DIFFICULTY", selectedDifficulty)
            intent.putExtra("NUM_QUESTIONS", selectedNumQuestions.toInt())  // Pass number of questions

            // Start the QuizActivity
            startActivity(intent)
        }
    }
}
