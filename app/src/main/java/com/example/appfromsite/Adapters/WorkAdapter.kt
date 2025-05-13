package com.example.appfromsite.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appfromsite.Entity.WorkEntity
import com.example.appfromsite.R

class WorkAdapter(private val prices: List<WorkEntity>) :
    RecyclerView.Adapter<WorkAdapter.PriceViewHolder>() {

    class PriceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val workName: TextView = itemView.findViewById(R.id.workName)
        val workSize: TextView = itemView.findViewById(R.id.workSize)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_work, parent, false)
        return PriceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        val price = prices[position]
        holder.workName.text = price.name
        holder.workSize.text = "${price.size} ${price.unit}"
    }

    override fun getItemCount() = prices.size
}