package com.cactusknights.chefbook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R

class CategoryEditAdapter(private var categories: ArrayList<String>, private val checkedCategories: ArrayList<String>) :
    RecyclerView.Adapter<CategoryEditAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.category.text = categories[position]
        holder.checkBox.isChecked = categories[position] in checkedCategories
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_categories_edit, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val category: TextView = itemView.findViewById(R.id.category_name)
        val checkBox: CheckBox = itemView.findViewById(R.id.category_checkbox)
    }
}