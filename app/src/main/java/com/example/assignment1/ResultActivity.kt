package com.example.assignment1

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    private lateinit var scoreProgressBar: ProgressBar
    private lateinit var scoreTextView: TextView
    private var score: Int = 0
    private var totalQuestions: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Initialize views
        scoreProgressBar = findViewById(R.id.score_progress)
        scoreTextView = findViewById(R.id.score_summary)

        // Retrieve score and totalQuestions from Intent extras
        score = intent.getIntExtra("SCORE", 0)
        totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 0)

        // Calculate and display the percentage
        val percentage = if (totalQuestions > 0) {
            (score.toFloat() / totalQuestions * 100).toInt()
        } else {
            0
        }
        scoreProgressBar.progress = percentage
        scoreTextView.text = "Score: $percentage%"
    }
}
