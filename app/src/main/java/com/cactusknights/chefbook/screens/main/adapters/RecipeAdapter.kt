package com.cactusknights.chefbook.screens.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.Utils
import com.cactusknights.chefbook.models.Recipe

open class RecipeAdapter(val listener: (Recipe) -> Unit) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.text_name)
        val favouriteIcon: ImageView = itemView.findViewById(R.id.image_favourite)
        val time: TextView = itemView.findViewById(R.id.text_time)
        val categories: TextView = itemView.findViewById(R.id.text_categories)

        init {
            itemView.setOnClickListener {
                listener(differ.currentList[adapterPosition])
            }
        }

        fun setTime(time: Int) {
            this.time.text = Utils.getFormattedTimeByMinutes(time, itemView.context.resources)
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldRecipe: Recipe, newRecipe: Recipe): Boolean {
            return oldRecipe.id == newRecipe.id
        }

        override fun areContentsTheSame(oldRecipe: Recipe, newRecipe: Recipe): Boolean {
            if (oldRecipe.categories.size != newRecipe.categories.size) return false
            for (category in oldRecipe.categories) if (category !in newRecipe.categories) return false
            return (oldRecipe.name == newRecipe.name &&
                    oldRecipe.time == newRecipe.time &&
                    oldRecipe.isFavourite == newRecipe.isFavourite)
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = differ.currentList[position]
        holder.favouriteIcon.visibility = if (recipe.isFavourite) View.VISIBLE else View.GONE
        holder.name.text = recipe.name
        holder.setTime(recipe.time)
        holder.categories.text =
            if (recipe.categories.isNotEmpty()) recipe.categories.joinToString(separator = ", ")
            else holder.itemView.context.getString(R.string.uncategorized)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_recipes, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}