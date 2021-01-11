package com.example.donateapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.donateapp.DataClasses.Items
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileListAdapter (
    private val context: Context,
    private val userItems: List<Items>) : RecyclerView.Adapter<ProfileListAdapter.ViewHolder>(){

    private val layoutInflater = LayoutInflater.from(context)
    private lateinit var auth: FirebaseAuth


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.my_item_card, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = userItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = userItems[position]
        val itemTitle = item.title
        val itemDesc = item.description

        holder.itemDesc.text = itemDesc
        holder.itemTitle.text = itemTitle


    }

    fun removeItem(position: Int){
        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        val user = auth.currentUser

        val item = userItems[position]
        db.collection("users").document(user!!.uid).collection("userItems").document(item.title.toString()).delete()
    }

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle = itemView.findViewById<TextView>(R.id.my_item_title)
        val itemDesc = itemView.findViewById<TextView>(R.id.my_item_desc)
        val deleteButton = itemView.findViewById<ImageButton>(R.id.deleteButton)
        var itemPosition = 0

        init {
            deleteButton.setOnClickListener {
                auth = FirebaseAuth.getInstance()
                removeItem(itemPosition)
                println("!!!${removeItem(itemPosition)}") //kotlin.UNIT??
                //delete post
            }
        }
    }
}