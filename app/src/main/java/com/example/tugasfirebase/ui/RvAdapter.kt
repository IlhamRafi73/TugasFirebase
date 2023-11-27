package com.example.tugasfirebase.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasfirebase.R
import com.example.tugasfirebase.database.Report

class RvAdapter(
    private val reportList: List<Report>,
    private val onItemClick: (Report) -> Unit,
    private val onDeleteClick: (Report) -> Unit
) : RecyclerView.Adapter<RvAdapter.NotesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentReport = reportList[position]
        holder.name.text = currentReport.nama
        holder.title.text = currentReport.judul
        holder.description.text = currentReport.isi
        holder.deleteButton.visibility = View.VISIBLE
    }

    override fun getItemCount(): Int {
        return reportList.size
    }

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.itemName)
        val title: TextView = itemView.findViewById(R.id.itemTitle)
        val description: TextView = itemView.findViewById(R.id.itemDescription)
        val deleteButton: ImageButton = itemView.findViewById(R.id.btnDelete)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(reportList[position])
                }
            }

            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClick(reportList[position])
                }
            }
        }
    }
}