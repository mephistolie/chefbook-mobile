package com.cactusknights.chefbook.legacy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.DiffUtil
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
        val item: TextView = itemView.findViewById(R.id.text_name)
        private val deleteItem: AppCompatImageButton = itemView.findViewById(R.id.btn_delete)

        init {
            deleteItem.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    private class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}