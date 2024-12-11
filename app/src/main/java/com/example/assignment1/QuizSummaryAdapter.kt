package com.example.assignment1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment1.databinding.ItemResultBinding

class QuizSummaryAdapter(
    private val resultList: ArrayList<ResultModel>,
    private val onItemClick: (ResultModel) -> Unit  // Add click listener parameter
) : RecyclerView.Adapter<QuizSummaryAdapter.ResultViewHolder>() {

    inner class ResultViewHolder(private val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: ResultModel) {
            // Display the question
            binding.tvQuestion.text = "Question: ${result.question}"

            // Display the user's selected answer
            binding.tvSelectedAnswer.text = "Your Answer: ${result.selectedAnswer}"

            // Display the correct answer
            binding.tvCorrectAnswer.text = "Correct Answer: ${result.correctAnswer}"

            // Indicate if the answer was correct or incorrect
            binding.tvResultStatus.text = if (result.isCorrect) {
                "Result: Correct"
            } else {
                "Result: Incorrect"
            }

            // Handle item click
            binding.root.setOnClickListener {
                onItemClick(result)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(resultList[position])
    }

    override fun getItemCount(): Int = resultList.size
    fun isEmpty(): Boolean = resultList.isEmpty()
}
