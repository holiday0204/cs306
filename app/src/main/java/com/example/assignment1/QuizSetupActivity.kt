package com.example.assignment1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import com.example.assignment1.Constants
import com.example.assignment1.databinding.ActivityQuizSetupBinding
import com.example.assignment1.QuizClass

class QuizSetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizSetupBinding
    private var amount = 10
    private var category: Int? = null
    private var difficulty: String? = null
    private var type: String? = null
    private var timerValue = 20 // Default timer value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.materialToolbar
        setSupportActionBar(toolbar)

        // Enable the "up" button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        handleSpinner()

        val categoryList = Constants.getCategoryStringArray()
        binding.topicSpinner.adapter = getSpinnerAdapter(categoryList)
        binding.difficultySpinner.adapter = getSpinnerAdapter(Constants.difficultyList)
        binding.questionTypeSpinner.adapter = getSpinnerAdapter(Constants.typeList)

        handleCategorySpinner()
        handleDifficultySpinner()
        handleTypeSpinner()

        // Initialize timer value from SharedPreferences if it's set
        val sharedPreferences = getSharedPreferences("quiz_prefs", MODE_PRIVATE)
        timerValue = sharedPreferences.getInt("timerValue", 20)

        // Set SeekBar to reflect the stored timer value
        binding.timerSeekBar.progress = timerValue - 10 // SeekBar range is 0 to 20 (for 10-30s)
        binding.timerLabel.text = "Select Timer (10-30s): $timerValue"

        // Set listener for SeekBar to update the timer value
        binding.timerSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                timerValue = progress + 10 // Timer value will be between 10 and 30 seconds
                binding.timerLabel.text = "Select Timer (10-30s): $timerValue"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val quizClass = QuizClass(this)
        binding.startButton.setOnClickListener {
            // Save the timer value to SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putInt("timerValue", timerValue)
            editor.apply()

            // Start the quiz
            quizClass.getQuizList(amount, category, difficulty, type)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Navigate to the Home Page when the up button is pressed
                val intent = Intent(this, HomePageActivity::class.java)
                startActivity(intent)
                finish() // Optionally finish this activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleSpinner() {
        val amounts = (1..10).toList()

        binding.numOfQuestionSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    amount = amounts[position]
                    binding.questionCountLabel.text = "Amount: $amount"
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            amounts
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.numOfQuestionSpinner.adapter = adapter
    }

    private fun handleCategorySpinner() {
        binding.topicSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    category = if (position == 0) null else position + 8
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }

    private fun handleDifficultySpinner() {
        val difficulties = listOf(null, "easy", "medium", "hard")

        binding.difficultySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                difficulty = difficulties[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                difficulty = null
            }
        }
    }

    private fun handleTypeSpinner() {
        val typeOptions = listOf(null, "multiple", "boolean")

        binding.questionTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                type = typeOptions[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                type = null
            }
        }
    }

    private fun getSpinnerAdapter(list: List<String>): ArrayAdapter<String> {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }
}
