package com.cactusknights.chefbook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.ListCategoriesEditBinding

class CategoryEditAdapter(private var categories: ArrayList<String>, private val checkedCategories: ArrayList<String>) :
    RecyclerView.Adapter<CategoryEditAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textName.text = categories[position]
        holder.binding.checkboxCategory.isChecked = categories[position] in checkedCategories
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_categories_edit, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ListCategoriesEditBinding.bind(itemView)
    }
}