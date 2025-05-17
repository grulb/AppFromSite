package com.example.appfromsite.Adapters.WorkAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appfromsite.Entity.WorkEntity
import com.example.appfromsite.R

class WorkAdapter : RecyclerView.Adapter<WorkAdapter.WorkViewHolder>() {
    private var works = emptyList<WorkEntity>()

    inner class WorkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.workName)
        val size: TextView = view.findViewById(R.id.workSize)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_work, parent, false)
        return WorkViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkViewHolder, position: Int) {
        val work = works[position]
        holder.name.text = work.name
        holder.size.text = "${work.size} ${work.unit}"
    }

    override fun getItemCount() = works.size

    fun submitList(newWorks: List<WorkEntity>) {
        works = newWorks
        notifyDataSetChanged()
    }
}