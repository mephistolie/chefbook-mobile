package com.cactusknights.chefbook.screens.main.fragments.recipes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.Utils
import com.cactusknights.chefbook.models.Recipe

open class RecipeAdapter(val listener: (Recipe) -> Unit) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.text_name)
//        val favouriteIcon: CardView = itemView.findViewById(R.id.cv_favourite)
        val time: TextView = itemView.findViewById(R.id.text_time)
        val caloriesGroup: LinearLayoutCompat = itemView.findViewById(R.id.ll_calories)
        val calories: TextView = itemView.findViewById(R.id.text_calories)
        val servings: TextView = itemView.findViewById(R.id.text_servings)
        val cover: ImageView = itemView.findViewById(R.id.iv_cover)

        init {
            itemView.setOnClickListener {
                listener(differ.currentList[adapterPosition])
            }
        }

        fun setTime(time: Int) {
            this.time.text = Utils.getFormattedTimeByMinutes(time, itemView.context.resources)
        }

        fun setCalories(calories: Int) {
            this.calories.text = calories.toString() + " " + itemView.context.resources.getString(R.string.kcal)
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldRecipe: Recipe, newRecipe: Recipe): Boolean {
            return oldRecipe.id == newRecipe.id || oldRecipe.remoteId == newRecipe.remoteId
        }

        override fun areContentsTheSame(oldRecipe: Recipe, newRecipe: Recipe): Boolean {
            return (oldRecipe.name == newRecipe.name &&
                    oldRecipe.time == newRecipe.time &&
                    oldRecipe.isFavourite == newRecipe.isFavourite)
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = differ.currentList[position]
//        holder.favouriteIcon.visibility = if (recipe.isFavourite) View.VISIBLE else View.GONE
        holder.name.text = recipe.name
        holder.setTime(recipe.time)
        holder.servings.text = recipe.servings.toString()
        holder.setCalories(recipe.calories)
        holder.caloriesGroup.visibility = if (recipe.calories == 0) View.GONE else View.VISIBLE
        if (!recipe.preview.isNullOrEmpty()) holder.cover.load(recipe.preview) { this.crossfade(true) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cell_recipe, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}