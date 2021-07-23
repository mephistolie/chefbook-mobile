package com.cactusknights.chefbook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.ListCategoriesBinding

class CategoryAdapter(private var categories: ArrayList<String>, val listener: CategoryClickListener) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textName.text = categories[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_categories, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ListCategoriesBinding.bind(itemView)
        init {
            binding.root.setOnClickListener {
                listener.onCategoryClick(adapterPosition)
            }
        }
    }

    interface CategoryClickListener {
        fun onCategoryClick(position: Int)
    }

    fun updateCategories(newCategories: ArrayList<String>) {
        categories = newCategories
        this.notifyDataSetChanged()
    }
}