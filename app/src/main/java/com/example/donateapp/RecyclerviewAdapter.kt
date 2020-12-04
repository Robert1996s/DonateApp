package com.example.donateapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerviewAdapter(

    private val context: Context,
    private val items: List<Items>) : RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

           val titleText = itemView.findViewById<TextView>(R.id.title_text)
           val descriptionText = itemView.findViewById<TextView>(R.id.description)
           //itemView.imageIv.setImageResource(items.image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = layoutInflater.inflate(R.layout.row_card, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.titleText.text = item.title
        holder.descriptionText.text = item.description


    }

}



