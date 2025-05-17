package com.example.appfromsite.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.appfromsite.Entity.StockEntity
import com.example.appfromsite.R

class StockAdapter : RecyclerView.Adapter<StockAdapter.StockViewHolder>() {
    private val stocks = mutableListOf<StockEntity>()

    inner class StockViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val stockImage: ImageView = itemView.findViewById(R.id.stockPicture)
        val stockTitle: TextView = itemView.findViewById(R.id.stockTitle)
        val stockDesc: TextView = itemView.findViewById(R.id.stockDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stock, parent, false)
        return StockViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stock = stocks[position]

        holder.stockTitle.text = stock.title
        holder.stockDesc.text = stock.text

        Glide.with(holder.itemView.context)
            .load(stock.url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.stockImage)
    }

    override fun getItemCount(): Int = stocks.size

    fun updateData(newStocks: List<StockEntity>) {
        stocks.clear()
        stocks.addAll(newStocks)
        notifyDataSetChanged()
    }
}