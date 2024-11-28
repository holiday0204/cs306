package com.example.assignment1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView


// Adapter for the RecyclerView in HomeFragment
class QuizHistoryAdapter(private val quizHistory: List<QuizHistoryItem>) :
    RecyclerView.Adapter<QuizHistoryAdapter.QuizHistoryViewHolder>() {

    // Create ViewHolder for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_quiz_history, parent, false)
        return QuizHistoryViewHolder(view)
    }

    // Bind data to the view
    override fun onBindViewHolder(holder: QuizHistoryViewHolder, position: Int) {
        val quizItem = quizHistory[position]
        holder.bind(quizItem)
    }

    // Return the number of items in the dataset
    override fun getItemCount(): Int {
        return quizHistory.size
    }

    // ViewHolder class that holds the item view
    class QuizHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconImageView: AppCompatImageView = itemView.findViewById(R.id.icon)
        private val quizTitleTextView: TextView = itemView.findViewById(R.id.quizTitleTextView)
        private val quizScoreTextView: TextView = itemView.findViewById(R.id.quizScoreTextView)
        private val quizDateTextView: TextView = itemView.findViewById(R.id.quizDateTextView)

        // Bind data to the view
        fun bind(quizItem: QuizHistoryItem) {
            // Set the icon, title, score, and date
            iconImageView.setImageResource(quizItem.iconResId)
            quizTitleTextView.text = quizItem.title
            quizScoreTextView.text = quizItem.score
            quizDateTextView.text = quizItem.date
        }
    }
}

// Data model for a quiz history item
data class QuizHistoryItem(
    val iconResId: Int,
    val title: String,
    val score: String,
    val date: String
)

