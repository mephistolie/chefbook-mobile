package com.cactusknights.chefbook.screens.recipeinput.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.ListCategoriesBinding
import com.cactusknights.chefbook.databinding.ListCategoriesEditBinding
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Selectable

class CategoryEditAdapter(val listener: (Int) -> Unit) :
    RecyclerView.Adapter<CategoryEditAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ListCategoriesEditBinding.bind(itemView)
        init {
            binding.checkboxCategory.setOnClickListener {
                listener(adapterPosition)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Selectable<String>>() {
        override fun areItemsTheSame(oldCategory: Selectable<String>, newCategory: Selectable<String>): Boolean {
            return oldCategory.item == newCategory.item
        }

        override fun areContentsTheSame(oldCategory: Selectable<String>, newCategory: Selectable<String>): Boolean {
            return oldCategory.item == newCategory.item && oldCategory.isSelected != newCategory.isSelected
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textName.text = differ.currentList[position].item
        holder.binding.checkboxCategory.isChecked = differ.currentList[position].isSelected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_categories_edit, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}