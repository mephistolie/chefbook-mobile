package com.cactusknights.chefbook.legacy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.ListCategoriesEditBinding
import com.cactusknights.chefbook.models.Selectable

class CategoryEditAdapter(private var categories: ArrayList<Selectable<String>>,
                          val listener: EditCategoryClickListener) :
    RecyclerView.Adapter<CategoryEditAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textName.text = categories[position].item!!
        holder.binding.checkboxCategory.isChecked = categories[position].isSelected
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
        init {
            binding.checkboxCategory.setOnClickListener {
                listener.onCategoryClick(adapterPosition, binding.checkboxCategory.isChecked)
            }
        }
    }

    interface EditCategoryClickListener {
        fun onCategoryClick(position: Int, isChecked: Boolean)
    }
}