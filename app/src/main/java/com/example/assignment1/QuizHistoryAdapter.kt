
package com.example.assignment1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuizHistoryAdapter(
    private val historyList: List<QuizHistoryModel>,
    private val onItemClick: (QuizHistoryModel) -> Unit
) : RecyclerView.Adapter<QuizHistoryAdapter.QuizHistoryViewHolder>() {

    class QuizHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.findViewById(R.id.tvDate)
        val scoreTextView: TextView = view.findViewById(R.id.tvScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quiz_history, parent, false)
        return QuizHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizHistoryViewHolder, position: Int) {
        val history = historyList[position]
        holder.dateTextView.text = java.text.DateFormat.getDateTimeInstance().format(history.date)
        holder.scoreTextView.text = "Score: ${history.score}/${history.totalQuestions}"

        holder.itemView.setOnClickListener {
            onItemClick(history)
        }
    }


    override fun getItemCount(): Int = historyList.size
}
