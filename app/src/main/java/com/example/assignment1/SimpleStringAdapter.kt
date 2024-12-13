package com.example.assignment1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SimpleStringAdapter(private val items: List<String>) : RecyclerView.Adapter<SimpleStringAdapter.ViewHolder>() {

    // ViewHolder for each item
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(android.R.id.text1) // or your custom TextView ID
    }

    // Called to create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    // Called to bind data to the view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item
    }

    // Returns the total number of items
    override fun getItemCount(): Int {
        return items.size
    }
}
