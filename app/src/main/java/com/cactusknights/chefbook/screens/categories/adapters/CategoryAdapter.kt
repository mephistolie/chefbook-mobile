package com.cactusknights.chefbook.screens.categories.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.ListCategoriesBinding
import com.cactusknights.chefbook.models.Category

class CategoryAdapter(val listener: (Int) -> Unit) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ListCategoriesBinding.bind(itemView)
        init {
            binding.root.setOnClickListener {
                listener(adapterPosition)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldCategory: Category, newCategory: Category): Boolean {
            return oldCategory.id == newCategory.id
        }

        override fun areContentsTheSame(oldCategory: Category, newCategory: Category): Boolean {
            return oldCategory.name == newCategory.name && oldCategory.type != newCategory.type
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textName.text = differ.currentList[position].name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_categories, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}