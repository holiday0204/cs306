package com.example.assignment1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment1.databinding.ActivityResultBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the ResultModel list passed from the previous activity
        val resultList: ArrayList<ResultModel>? =
            intent.getParcelableArrayListExtra("resultList")

        // Set up the RecyclerView for showing the result summary with a click listener
        binding.rvSummary.layoutManager = LinearLayoutManager(this)
        val adapter = QuizSummaryAdapter(resultList ?: arrayListOf()) { result ->
            // Handle item click, show a detailed message with the question and result status
            val resultMessage = if (result.score > 0) {
                "Correct! Your answer: ${result.selectedAnswer}"
            } else {
                "Incorrect! Correct answer: ${result.correctAnswer}"
            }
            Toast.makeText(this, "${result.question}\n$resultMessage", Toast.LENGTH_LONG).show()
        }
        binding.rvSummary.adapter = adapter

        // Show or hide the RecyclerView based on whether the list is empty
        if (adapter.itemCount == 0) {
            binding.rvSummary.visibility = View.GONE
            binding.tvEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvSummary.visibility = View.VISIBLE
            binding.tvEmptyState.visibility = View.GONE
        }

        // Calculate the final score using getFinalScore()
        val finalScore = getFinalScore(resultList ?: arrayListOf())

        // Calculate the total number of questions and the number of correct answers
        val correctAnswers = resultList?.count { it.score > 0 } ?: 0
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

        // Create and save quiz history after displaying results
        saveQuizHistory(finalScore, totalQuestions, resultList ?: arrayListOf())

        // Home Page button click listener
        binding.homePageBtn.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            finish() // Close the result activity
        }
    }

    // Function to calculate the final score
    private fun getFinalScore(list: ArrayList<ResultModel>): Double {
        var totalScore = 0.0
        for (item in list) {
            totalScore += if (item.score > 1.0) 1.0 else item.score
        }
        return totalScore
    }

    private fun saveQuizHistory(
        finalScore: Double,
        totalQuestions: Int,
        resultList: ArrayList<ResultModel>
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Log.e("Firestore", "User is not authenticated.")
            return
        }

        Log.d("User", "Current User ID: $userId")

        // Proceed with saving quiz history if the user is authenticated
        val timestamp: Long = System.currentTimeMillis()  // Current time for storing the date

        // Convert resultList into a list of Maps
        val quizResults = resultList.map { result ->
            result.toMap()  // Convert each ResultModel to a Map<String, Any>
        }

        // Create the QuizHistory object
        val quizHistory = QuizHistoryModel(
            date = timestamp,
            score = finalScore,
            totalQuestions = totalQuestions,
            questionsAnswered = resultList.size,
            quizResults = resultList // Pass the list of Maps to Firestore
        )

        // Save the quiz history to Firestore
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(userId) // Use userId to identify the correct user document
            .collection("quizHistory")
            .document()  // Automatically generates a new document ID
            .set(quizHistory) // Save the quiz history object
            .addOnSuccessListener {
                Log.d("Firestore", "Quiz history saved successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error saving quiz history: ${e.message}")
                e.printStackTrace()
            }
    }
}