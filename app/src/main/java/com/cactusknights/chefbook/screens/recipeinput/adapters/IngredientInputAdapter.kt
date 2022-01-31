package com.cactusknights.chefbook.screens.recipeinput.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.CellSelectableEditBinding
import com.cactusknights.chefbook.models.Ingredient
import com.cactusknights.chefbook.models.IngredientTypes

class IngredientInputAdapter(private var ingredients: ArrayList<Ingredient>): RecyclerView.Adapter<IngredientInputAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredients[position]
        if (ingredient.type == IngredientTypes.SECTION) {
            holder.binding.inputSelectable.setTypeface(null, Typeface.BOLD)
            holder.binding.inputSelectable.hint = holder.binding.inputSelectable.resources?.getString(R.string.section)
        } else {
            holder.binding.inputSelectable.setTypeface(null, Typeface.NORMAL)
            holder.binding.inputSelectable.hint = holder.binding.inputSelectable.resources?.getString(R.string.ingredient)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CellSelectableEditBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = ingredients.size

    inner class ViewHolder(val binding: CellSelectableEditBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.inputSelectable.doOnTextChanged { text, _, _, _ -> ingredients[adapterPosition].text = text.toString() }

            binding.btnDeleteSelectable.setOnClickListener {
                ingredients.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }
}