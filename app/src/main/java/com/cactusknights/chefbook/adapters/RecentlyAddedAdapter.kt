package com.cactusknights.chefbook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.models.Recipe

class RecentlyAddedAdapter(private var recipes: ArrayList<Recipe>, val listener: OnRecentlyAdded) :
    RecyclerView.Adapter<RecentlyAddedAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.name.text = recipe.name
        holder.time.text = recipe.time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_popular, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.recipe_name)
        val time: TextView = itemView.findViewById(R.id.recipe_time)

        init {
            itemView.setOnClickListener {
                listener.onRecentlyAddedClick(recipes[adapterPosition])
            }
        }
    }

    interface OnRecentlyAdded {
        fun onRecentlyAddedClick(recipe: Recipe)
    }
}