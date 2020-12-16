package com.example.donateapp

import android.content.ClipData
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_card.view.*
import java.net.URL

class ItemAdapter (
    private val context: Context,
    private val items: List<Items>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    //private var itemList: List<Items> = ArrayList()

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.row_card, parent, false)
        return ViewHolder(itemView)
        /*return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_card,
                parent,
                false)) */
    }
    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val itemTitle = item.title
        val itemDesc = item.description
        val itemImage = item.item_image_url
        holder.titleText.text = itemTitle
        holder.descriptionText.text = itemDesc
        Glide.with(holder.itemView).load(item).into(holder.itemImage1)
        //Glide.with(holder.itemView.context).load(uri).into(holder.itemImage1)

    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val titleText = itemView.findViewById<TextView>(R.id.title_text)
        val descriptionText = itemView.findViewById<TextView>(R.id.description_text)
        val itemImage1 = itemView.findViewById<ImageView>(R.id.item_image)
    }

    inner class ImageViewHolder (itemView: View): RecyclerView.ViewHolder(itemView)

}
