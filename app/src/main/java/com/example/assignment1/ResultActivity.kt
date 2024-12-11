package com.example.assignment1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment1.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the ResultModel list passed from the previous activity
        val resultList: ArrayList<ResultModel>? =
            intent.getParcelableArrayListExtra("resultList") // This retrieves the ResultModel list

        // Set up the RecyclerView for showing the result summary with a click listener
        binding.rvSummary.layoutManager = LinearLayoutManager(this)
        val adapter = QuizSummaryAdapter(resultList ?: arrayListOf()) { result ->
            // Handle item click, e.g., show a detailed view or a toast
            Toast.makeText(this, "Clicked on ${result.type}", Toast.LENGTH_SHORT).show()
        }
        binding.rvSummary.adapter = adapter

        // Show or hide the RecyclerView based on whether the list is empty
        if (adapter.itemCount == 0) {
            binding.rvSummary.visibility = View.GONE
            binding.tvEmptyState.visibility = View.VISIBLE // Ensure you have a TextView for empty state in your layout
        } else {
            binding.rvSummary.visibility = View.VISIBLE
            binding.tvEmptyState.visibility = View.GONE
        }

        // Calculate the final score using getFinalScore()
        val finalScore = getFinalScore(resultList ?: arrayListOf())

        // Calculate the total number of questions and the number of correct answers
        val correctAnswers = resultList?.count { it.score > 0 } ?: 0 // Count answers where score > 0 (indicating correct answers)
        val totalQuestions = resultList?.size ?: 0

        // Calculate percentage based on the final score (total score / number of questions)
        val percentage = if (totalQuestions > 0) (finalScore / totalQuestions) * 100 else 0.0

        // Update the UI with the final score percentage
        binding.scoreProgressText.text = "${String.format("%.0f", percentage)}%"

        // Display the score breakdown (correct answers out of total)
        binding.scoreSubtitle.text = "$correctAnswers out of $totalQuestions questions are correct"
        binding.scoreBreakdown.text = "${String.format("%.0f", percentage)}%"

        // Show bonus message based on percentage
        val bonus = if (percentage >= 50) 5 else 0
        if (bonus > 0) {
            binding.speedBonusMessage.visibility = View.VISIBLE
            binding.scoreBreakdown.text = "${String.format("%.0f", percentage)}% + $bonus% Bonus"
        } else {
            binding.timeUpMessage.visibility = View.VISIBLE
        }

        // Update the progress bar
        binding.scoreProgressIndicator.max = 100
        binding.scoreProgressIndicator.progress = percentage.toInt()

        // Home Page button click listener
        binding.homePageBtn.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            finish() // Close the result activity
        }
    }

    private fun getFinalScore(list: ArrayList<ResultModel>): Double {
        var totalScore = 0.0
        for (item in list) {
            totalScore += if (item.score > 1.0) 1.0 else item.score // Ensure score doesn't exceed 1.0
        }
        return totalScore
    }

}
