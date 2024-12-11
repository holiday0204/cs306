package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment1.databinding.ActivityQuizHistoryBinding

class QuizHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val quizHistoryList: List<QuizHistory> = fetchQuizHistory() // Retrieve your data here

        binding.rvQuizHistory.layoutManager = LinearLayoutManager(this)
        val adapter = QuizHistoryAdapter(quizHistoryList) { selectedHistory ->
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("quizHistory", selectedHistory)
            startActivity(intent)
        }
        binding.rvQuizHistory.adapter = adapter
    }

    // Dummy function to fetch quiz history data
    private fun fetchQuizHistory(): List<QuizHistory> {
        // Replace this with your actual data retrieval logic
        return listOf()
    }
}
