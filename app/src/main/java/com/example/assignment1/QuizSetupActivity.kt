package com.example.assignment1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.SpinnerAdapter
import com.example.assignment1.Constants
import com.example.assignment1.databinding.ActivityQuizSetupBinding
import com.example.assignment1.QuizClass

class QuizSetupActivity : AppCompatActivity() {


    private var binding: ActivityQuizSetupBinding? = null
    private var amount = 10
    private var category: Int? = null
    private var difficulty: String? = null
    private var type: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizSetupBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        handleSpinner()

        val categoryList = Constants.getCategoryStringArray()
        binding?.topicSpinner?.adapter = getSpinnerAdapter(categoryList)
        binding?.difficultySpinner?.adapter = getSpinnerAdapter(Constants.difficultyList)
        binding?.questionTypeSpinner?.adapter = getSpinnerAdapter(Constants.typeList)

        handleCategorySpinner()
        handleDifficultySpinner()
        handleTypeSpinner()

        val quizClass = QuizClass(this)
        binding?.startButton?.setOnClickListener {
            quizClass.getQuizList(amount, category, difficulty, type)
        }
    }

    private fun handleSpinner() {
        // Create a list of integers from 1 to 10
        val amounts = (1..10).toList()

        // Set the listener for the spinner
        binding?.numOfQuestionSpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Get the selected amount from the list and update the 'amount' variable
                    amount = amounts[position]

                    // Update the TextView with the selected amount
                    binding?.questionCountLabel?.text = "Amount: $amount"
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // No action needed if nothing is selected
                }
            }

        // Create an ArrayAdapter for the spinner with the list of amounts (1-10)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            amounts
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Set the adapter to the spinner
        binding?.numOfQuestionSpinner?.adapter = adapter
    }



    private fun handleCategorySpinner() {
        binding?.topicSpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    category = if (position == 0)
                        null
                    else
                        position + 8
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // Nothing to do here
                }

            }
    }

    private fun handleDifficultySpinner() {
        val difficulties = listOf(null, "easy", "medium", "hard")

        binding?.difficultySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        binding?.questionTypeSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                type = typeOptions[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                type = null
            }
        }
    }


    private fun getSpinnerAdapter(list: List<String>): SpinnerAdapter {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }
}