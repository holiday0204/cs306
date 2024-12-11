package com.example.assignment1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.assignment1.databinding.ActivityRandomQuestionBinding

class RandomQuestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRandomQuestionBinding
    private lateinit var quizClass: QuizClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRandomQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize QuizClass
        quizClass = QuizClass(this)

        // Set up the RecyclerView for quiz categories
        setupCategoryRecyclerView()

        // Button for random quiz
        binding.btnRandomQuiz.setOnClickListener {
            quizClass.getQuizList(10, null, null, null)
        }
    }

    private fun setupCategoryRecyclerView() {
        val rvCategoryList = binding.rvCategoryList
        rvCategoryList.layoutManager = GridLayoutManager(this, 2)
        quizClass.setRecyclerView(rvCategoryList)
    }
}
