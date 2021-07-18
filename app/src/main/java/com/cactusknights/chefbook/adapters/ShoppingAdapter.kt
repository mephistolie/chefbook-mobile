package com.cactusknights.chefbook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R

class ShoppingAdapter(private var shoppingList: ArrayList<String>, val listener: ItemClickListener): RecyclerView.Adapter<ShoppingAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item.text = shoppingList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_shopping, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: TextView = itemView.findViewById(R.id.item)
        private val deleteItem: AppCompatImageButton = itemView.findViewById(R.id.delete_item)

        init {
            deleteItem.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
}