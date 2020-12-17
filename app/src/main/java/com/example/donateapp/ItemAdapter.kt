package com.example.donateapp

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.net.URL
import kotlinx.android.synthetic.main.activity_post_item.view.*
import kotlinx.android.synthetic.main.row_card.view.*
import java.lang.reflect.Array.get
import java.nio.file.Paths.get

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

        //holder.itemImage.setImageResource(item.item_image_url!!.toInt())

        holder.itemView.setOnClickListener {
            val title : String? = itemTitle
            val description : String? = itemDesc

            val intent = Intent(context, DetalInformation::class.java)

            intent.putExtra("titleText", title)
            intent.putExtra("descriptionText", description)
            intent.putExtra("displayedImage", image)

            context.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.title_text)
        val descriptionText: TextView = itemView.findViewById(R.id.description_text)
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)


    }
}

