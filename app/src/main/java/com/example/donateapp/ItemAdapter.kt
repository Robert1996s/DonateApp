package com.example.donateapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.donateapp.Activity.ItemInfoPage
import com.example.donateapp.DataClasses.Items
import com.example.donateapp.Models.GlobalItemList
import com.example.donateapp.Models.NetworkHandler

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

    //override fun getItemCount() = GlobalItemList.globalItemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        //val item = GlobalItemList.globalItemList[position]

        val itemDesc = item.description
        val itemTitle = item.title
        val itemImage = item.item_image_url
        holder.titleText.text = itemTitle
        holder.descriptionText.text = itemDesc

        //holder.itemImage.setImageResource(item.item_image_url!!.toInt())

        holder.itemView.setOnClickListener {
            val title : String? = itemTitle
            val description : String? = itemDesc

            val intent = Intent(context, ItemInfoPage::class.java)

            intent.putExtra("titleText", title)
            intent.putExtra("descriptionText", description)
            //intent.putExtra("displayedImage", image)

            context.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.title_text)
        val descriptionText: TextView = itemView.findViewById(R.id.description_text)
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)


    }
}

