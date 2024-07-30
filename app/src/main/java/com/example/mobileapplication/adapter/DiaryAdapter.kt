package com.example.mobileapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapplication.R

data class DiaryEntry(val date: String, val title: String, val description: String, val time: String, val imageResId: Int)

class DiaryAdapter(private val diaryEntries: List<DiaryEntry>) : RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_journal, parent, false)
        return DiaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val entry = diaryEntries[position]
        holder.tvDate.text = entry.date
        holder.tvTitle.text = entry.title
        holder.tvDescription.text = entry.description
        holder.tvTime.text = entry.time
        holder.ivImage.setImageResource(entry.imageResId)
        holder.ivImage.visibility = if (entry.imageResId != 0) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = diaryEntries.size

    class DiaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        val ivImage: ImageView = itemView.findViewById(R.id.iv_image)
    }
}