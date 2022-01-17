package com.cactusknights.chefbook.screens.recipe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.CellCategoryEditBinding
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Selectable

class RecipeCategoryAdapter() :
    RecyclerView.Adapter<RecipeCategoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = CellCategoryEditBinding.bind(itemView)
        init {
            binding.checkboxCategory.setOnClickListener {
                differ.currentList[adapterPosition].isSelected = !differ.currentList[adapterPosition].isSelected
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Selectable<Category>>() {
        override fun areItemsTheSame(oldCategory: Selectable<Category>, newCategory: Selectable<Category>): Boolean {
            return oldCategory.item?.id == newCategory.item?.id || oldCategory.item?.remoteId == newCategory.item?.remoteId
        }

        override fun areContentsTheSame(oldCategory: Selectable<Category>, newCategory: Selectable<Category>): Boolean {
            return oldCategory.item?.name == newCategory.item?.name && oldCategory.isSelected != newCategory.isSelected
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = differ.currentList[position].item
        val text = category?.cover + " " + category?.name
        holder.binding.textName.text = text
        holder.binding.checkboxCategory.isChecked = differ.currentList[position].isSelected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cell_category_edit, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}