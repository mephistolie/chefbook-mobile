package com.cactusknights.chefbook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.models.Recipe
import kotlin.collections.ArrayList

class RecipeAdapter(private var recipes: ArrayList<Recipe>, val listener: RecipeClickListener) :
    RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.favouriteIcon.visibility = if (recipe.isFavourite) View.VISIBLE else View.GONE
        holder.name.text = recipe.name
        holder.time.text = recipe.time
        holder.categories.text =
            if (recipe.categories.isNotEmpty()) recipe.categories.joinToString(separator = ", ")
            else holder.itemView.context.getString(R.string.uncategorized)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_recipes, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.text_name)
        val favouriteIcon: ImageView = itemView.findViewById(R.id.image_favourite)
        val time: TextView = itemView.findViewById(R.id.text_time)
        val categories: TextView = itemView.findViewById(R.id.text_categories)

        init {
            itemView.setOnClickListener {
                listener.onRecipeClick(recipes[adapterPosition])
            }
        }
    }

    interface RecipeClickListener {
        fun onRecipeClick(recipe: Recipe)
    }

    fun updateRecipes(newRecipes: ArrayList<Recipe>) {
        recipes = arrayListOf()
        newRecipes.sortBy { it.name.lowercase() }
        recipes.addAll(newRecipes.toTypedArray())
        this.notifyDataSetChanged()
    }
}