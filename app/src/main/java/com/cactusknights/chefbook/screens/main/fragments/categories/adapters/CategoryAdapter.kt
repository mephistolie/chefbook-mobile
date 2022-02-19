package com.cactusknights.chefbook.screens.main.fragments.categories.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.CellAddCategoryBinding
import com.cactusknights.chefbook.databinding.CellCategoryBinding
import com.cactusknights.chefbook.databinding.CellSelectableEditBinding
import com.cactusknights.chefbook.models.Category

class CategoryAdapter(val openCategoryListener: (Int) -> Unit, val addCategoryListener: () -> Unit, val updateCategoryListener: (Category) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class CategoryViewHolder(val binding: CellCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                openCategoryListener(adapterPosition)
            }
            binding.root.setOnLongClickListener {
                updateCategoryListener(differ.currentList[adapterPosition])
                true
            }
        }
    }

    inner class AddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = CellAddCategoryBinding.bind(itemView)
        init { binding.root.setOnClickListener { addCategoryListener() } }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldCategory: Category, newCategory: Category): Boolean {
            return oldCategory.id == newCategory.id || oldCategory.remoteId == newCategory.remoteId
        }

        override fun areContentsTheSame(oldCategory: Category, newCategory: Category): Boolean {
            return oldCategory.name == newCategory.name && oldCategory.cover == newCategory.cover
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder) {
            holder.binding.textName.text = differ.currentList[position].name
            holder.binding.textCover.text = differ.currentList[position].cover
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.cell_category) {
            CategoryViewHolder(CellCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.cell_add_category, parent, false)
            AddViewHolder(v)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == differ.currentList.size) R.layout.cell_add_category else R.layout.cell_category
    }
}