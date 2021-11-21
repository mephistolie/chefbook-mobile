package com.cactusknights.chefbook.screens.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.Utils.getFormattedTimeByMinutes
import com.cactusknights.chefbook.models.Recipe

class RecentlyAddedAdapter(val listener: (Recipe) -> Unit) : RecyclerView.Adapter<RecentlyAddedAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Recipe>() {

        override fun areItemsTheSame(oldRecipe: Recipe, newRecipe: Recipe): Boolean {
            return oldRecipe.id == newRecipe.id
        }

        override fun areContentsTheSame(oldRecipe: Recipe, newRecipe: Recipe): Boolean {
            return (oldRecipe.name == newRecipe.name &&
                    oldRecipe.servings == newRecipe.servings &&
                    oldRecipe.time == newRecipe.time)
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.text_name)
        val servings: TextView = itemView.findViewById(R.id.text_servings)
        val time: TextView = itemView.findViewById(R.id.text_time)

        init {
            itemView.setOnClickListener {
                listener(differ.currentList[adapterPosition])
            }
        }

        fun setTime(time: Int) {
            this.time.text = getFormattedTimeByMinutes(time, itemView.context.resources)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = differ.currentList[position]
        holder.name.text = recipe.name
        holder.servings.text = recipe.servings.toString()
        holder.setTime(recipe.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_recently_added, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}