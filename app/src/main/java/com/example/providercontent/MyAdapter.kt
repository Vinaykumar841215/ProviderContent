package com.example.contentprovider2
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.providercontent.ContactModal
import com.example.providercontent.R

class MyAdapter(val context: Context, private var items: List<ContactModal> = emptyList()) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<ContactModal>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemImage : ImageView = itemView.findViewById(R.id.getImage)
        private val itemName: TextView = itemView.findViewById(R.id.getName)
        private val itemNumber: TextView = itemView.findViewById(R.id.getNumber)

        fun bind(item: ContactModal) {
//            itemImage.setImageBitmap(item.image.toString())
             Glide.with(context).load(item.image).into(itemImage)
            itemName.text = item.name
            itemNumber.text =item.phoneNumber
        }
    }
}