package com.cactusknights.chefbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.cactusknights.chefbook.databinding.ListPopularBinding
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.app.models.Recipe

class PopularAdapter(private var recipes: ArrayList<Recipe>, val listener: RecipeClickListener) :
    RecyclerView.Adapter<PopularAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.binding?.recipe = recipe
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_popular, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = DataBindingUtil.bind<ListPopularBinding>(itemView)

        init {
            itemView.setOnClickListener {
                listener.onPopularClick(adapterPosition)
            }
        }
    }

    interface RecipeClickListener {
        fun onPopularClick(position: Int)
    }

    fun updateRecipes(recipes: ArrayList<Recipe>) {
        this.recipes = recipes
        this.notifyDataSetChanged()
    }
}