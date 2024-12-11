package com.example.assignment1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment1.databinding.ItemResultBinding
import com.example.assignment1.ResultModel
class QuizSummaryAdapter(
    private val resultList: ArrayList<ResultModel>,
    private val onItemClick: (ResultModel) -> Unit  // Add click listener parameter
) : RecyclerView.Adapter<QuizSummaryAdapter.ResultViewHolder>() {

    inner class ResultViewHolder(private val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ResultModel) {
            binding.tvQuestionType.text = "Type: ${result.type}"
            binding.tvQuestionDifficulty.text = "Difficulty: ${result.difficulty}"
            binding.tvScore.text = "Score: ${String.format("%.2f", result.score)}"
            binding.tvTime.text = "Time Taken: ${result.time}s"

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
