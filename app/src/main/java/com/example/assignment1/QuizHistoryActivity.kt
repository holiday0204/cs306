package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment1.databinding.ActivityQuizHistoryBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class QuizHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizHistoryBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val historyList = mutableListOf<QuizHistoryModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the toolbar as the action bar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Enable the "Up" button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.rvQuizHistory.layoutManager = LinearLayoutManager(this)
        val adapter = QuizHistoryAdapter(historyList) { history ->
            val intent = Intent(this, ResultActivity::class.java)
            intent.putParcelableArrayListExtra("resultList", ArrayList(history.quizResults))
            startActivity(intent)
        }

        binding.rvQuizHistory.adapter = adapter

        fetchQuizHistories(adapter)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Navigate back to the homepage or desired activity
                val intent = Intent(this, HomePageActivity::class.java)  // Adjust MainActivity if needed
                startActivity(intent)
                finish()  // Finish current activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fetchQuizHistories(adapter: QuizHistoryAdapter) {
        val userEmail = auth.currentUser?.email ?: return

        db.collection("quizHistories")
            .whereEqualTo("userEmail", userEmail)
            .get()
            .addOnSuccessListener { documents ->
                historyList.clear()  // Clear any existing data in the list

                for (document in documents) {
                    val date = document.getLong("date") ?: 0L
                    val score = document.getDouble("score") ?: 0.0  // Correctly fetch score as Double
                    val questionsAnswered = (document.getLong("questionsAnswered") ?: 0L).toInt()
                    val totalQuestions = (document.getLong("totalQuestions") ?: 0L).toInt()
                    val result = document.getString("result") ?: ""  // Get the result as a string
                    val quizResultsData = document.get("quizResults") as? List<Map<String, Any>> ?: listOf()

                    val quizResults = quizResultsData.map { resultMap ->
                        ResultModel(
                            question = resultMap["question"] as? String ?: "",
                            selectedAnswer = resultMap["selectedAnswer"] as? String ?: "",
                            correctAnswer = resultMap["correctAnswer"] as? String ?: "",
                            correct = resultMap["correct"] as? Boolean ?: false,
                            score = (resultMap["score"] as? Long)?.toDouble() ?: 0.0,  // Cast Long to Double
                            time = (resultMap["time"] as? Long)?.toInt() ?: 0,
                            stability = (resultMap["stability"] as? Long)?.toInt() ?: 0,
                            timeBonus = (resultMap["timeBonus"] as? Long)?.toInt() ?: 0
                        )
                    }

                    val history = QuizHistoryModel(
                        date = date,
                        score = score,
                        questionsAnswered = questionsAnswered,
                        totalQuestions = totalQuestions,
                        quizResults = quizResults
                    )
                    historyList.add(history)
                }

                adapter.notifyDataSetChanged()  // Notify adapter of the data change
                toggleEmptyState()
            }
            .addOnFailureListener { e ->
                Log.e("QuizHistoryActivity", "Error fetching histories: ${e.message}", e)
            }
    }

    private fun toggleEmptyState() {
        if (historyList.isEmpty()) {
            binding.rvQuizHistory.visibility = View.GONE
            binding.tvEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvQuizHistory.visibility = View.VISIBLE
            binding.tvEmptyState.visibility = View.GONE
        }
    }
}
