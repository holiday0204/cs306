package com.example.assignment1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment1.databinding.ItemNotificationBinding
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(private val notifications: List<NotificationScoreModel>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.bind(notification)
    }

    override fun getItemCount(): Int = notifications.size

    class NotificationViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: NotificationScoreModel) {
            binding.notificationTitle.text = "Quiz Score"
            binding.notificationMessage.text = notification.result
            binding.notificationTimestamp.text = "Score: ${notification.score}%"
        }
    }
}
