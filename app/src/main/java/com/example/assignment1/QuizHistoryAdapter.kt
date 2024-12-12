package com.example.assignment1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment1.databinding.ItemQuizHistoryBinding
import com.example.assignment1.utils.formatDate

class QuizHistoryAdapter(
    private val historyList: List<QuizHistory>,
    private val onItemClick: (QuizHistory) -> Unit
) : RecyclerView.Adapter<QuizHistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(private val binding: ItemQuizHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(history: QuizHistory) {
            // Format and display the date
            binding.tvDate.text = "Date: ${formatDate(history.date)}"

            // Display the score
            binding.tvScore.text = "Score: ${history.score} / ${history.totalQuestions}"

            // Click listener to open detailed quiz results
            binding.root.setOnClickListener {
                onItemClick(history)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemQuizHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int = historyList.size
}
