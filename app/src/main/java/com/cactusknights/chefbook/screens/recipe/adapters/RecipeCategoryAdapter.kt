package com.cactusknights.chefbook.screens.recipe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.databinding.CellCategoryEditBinding
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Selectable

class RecipeCategoryAdapter() :
    RecyclerView.Adapter<RecipeCategoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CellCategoryEditBinding) : RecyclerView.ViewHolder(binding.root) {
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
        holder.binding.textName.text = category?.name
        holder.binding.textCover.text = category?.cover
        holder.binding.checkboxCategory.isChecked = differ.currentList[position].isSelected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CellCategoryEditBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}