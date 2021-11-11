package com.cactusknights.chefbook.legacy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.models.BaseRecipe
import com.cactusknights.chefbook.models.Recipe

class RecentlyAddedAdapter(val listener: (BaseRecipe) -> Unit) : RecyclerView.Adapter<RecentlyAddedAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<BaseRecipe>() {

        override fun areItemsTheSame(oldRecipe: BaseRecipe, newRecipe: BaseRecipe): Boolean {
            return oldRecipe.id == newRecipe.id
        }

        override fun areContentsTheSame(oldRecipe: BaseRecipe, newRecipe: BaseRecipe): Boolean {
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
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = differ.currentList[position]
        holder.name.text = recipe.name
        holder.servings.text = recipe.servings.toString()
        holder.time.text = recipe.time.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_recently_added, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}