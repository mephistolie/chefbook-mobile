package com.cactusknights.chefbook.screens.recipeinput.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.CellSelectableEditBinding
import com.cactusknights.chefbook.models.MarkdownString

class IngredientInputAdapter(private var ingredients: ArrayList<MarkdownString>): RecyclerView.Adapter<IngredientInputAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.binding?.selectableItem = ingredient
        holder.binding?.inputIngredient?.requestFocus()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cell_selectable_edit, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = DataBindingUtil.bind<CellSelectableEditBinding>(itemView)
        private var ingredientName: EditText = itemView.findViewById(R.id.input_ingredient)
        private var deleteIngredientButton: ImageButton = itemView.findViewById(R.id.btn_delete_ingredient)

        init {

            ingredientName.doOnTextChanged { text, _, _, _ -> ingredients[adapterPosition].text = text.toString() }

            deleteIngredientButton.setOnClickListener {
                ingredients.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }
}