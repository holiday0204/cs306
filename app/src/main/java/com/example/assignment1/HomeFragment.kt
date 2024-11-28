package com.example.assignment1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.activity_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find views
        val startQuizButton = view.findViewById<Button>(R.id.startQuizButton)
        val recyclerView = view.findViewById<RecyclerView>(R.id.quizHistoryRecyclerView)

        // Set up RecyclerView with the QuizHistoryAdapter
        val quizHistoryAdapter = QuizHistoryAdapter(getDummyHistory())
        recyclerView.adapter = quizHistoryAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set up button click listener
        startQuizButton.setOnClickListener {
            Snackbar.make(view, "Starting Quiz...", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun getDummyHistory(): List<QuizHistoryItem> {
        // Simulating data for the RecyclerView
        return listOf(
            QuizHistoryItem(R.mipmap.ic_launcher, "Quiz 1", "85%", "2024-11-01"),
            QuizHistoryItem(R.mipmap.ic_launcher, "Quiz 2", "90%", "2024-11-02"),
            QuizHistoryItem(R.mipmap.ic_launcher, "Quiz 3", "75%", "2024-11-03")
        )
    }
}
