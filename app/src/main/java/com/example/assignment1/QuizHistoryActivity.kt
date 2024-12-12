package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.assignment1.databinding.ActivityQuizHistoryBinding

class QuizHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fetch quiz history data from Firestore
        fetchQuizHistory()

        binding.rvQuizHistory.layoutManager = LinearLayoutManager(this)
    }

    // Function to fetch quiz history from Firestore
    private fun fetchQuizHistory() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .document(userId)
            .collection("quizHistory")
            .get()
            .addOnSuccessListener { result ->
                val quizHistoryList = result.mapNotNull { document ->
                    val quizHistory = document.toObject(QuizHistory::class.java)
                    quizHistory?.apply {
                        // Optionally, format date or process data here
                    }
                }

                // Set up the adapter with the fetched quiz history data
                val adapter = QuizHistoryAdapter(quizHistoryList) { selectedHistory ->
                    // When an item is clicked, pass it to ResultActivity
                    val intent = Intent(this, ResultActivity::class.java)
                    intent.putExtra("quizHistory", selectedHistory)  // Pass selected history to ResultActivity
                    startActivity(intent)
                }
                binding.rvQuizHistory.adapter = adapter

            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting quiz history", exception)
            }
    }
}
