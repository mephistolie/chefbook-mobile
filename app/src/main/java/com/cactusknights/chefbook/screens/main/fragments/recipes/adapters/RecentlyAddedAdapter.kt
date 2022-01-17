package com.cactusknights.chefbook.screens.main.fragments.recipes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.models.Recipe

class RecentlyAddedAdapter(val listener: (Recipe) -> Unit) : RecyclerView.Adapter<RecentlyAddedAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Recipe>() {

        override fun areItemsTheSame(oldRecipe: Recipe, newRecipe: Recipe): Boolean {
            return oldRecipe.id == newRecipe.id
        }

        override fun areContentsTheSame(oldRecipe: Recipe, newRecipe: Recipe): Boolean {
            return oldRecipe.name == newRecipe.name
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.text_name)
        val cover: ImageView = itemView.findViewById(R.id.iv_cover)

        init {
            itemView.setOnClickListener {
                listener(differ.currentList[adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = differ.currentList[position]
        holder.name.text = recipe.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cell_recently_updated, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}