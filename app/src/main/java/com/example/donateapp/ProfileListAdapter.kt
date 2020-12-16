package com.example.donateapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProfileListAdapter (
    private val context: Context,
    private val userItems: List<Items>) : RecyclerView.Adapter<ProfileListAdapter.ViewHolder>(){

    private val layoutInflater = LayoutInflater.from(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileListAdapter.ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.my_item_card, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = userItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = userItems[position]
        val itemTitle1 = item.title
        holder.itemTitle.text = itemTitle1
    }

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemTitle = itemView.findViewById<TextView>(R.id.my_item_title)
    }
}