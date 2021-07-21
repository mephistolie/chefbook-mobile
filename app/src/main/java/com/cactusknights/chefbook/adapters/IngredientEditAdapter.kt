package com.cactusknights.chefbook.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.models.Ingredient
import com.cactusknights.chefbook.databinding.ListIngredientsEditBinding

class IngredientEditAdapter(private var ingredients: ArrayList<Ingredient>): RecyclerView.Adapter<IngredientEditAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.binding?.ingredient = ingredient
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_ingredients_edit, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = DataBindingUtil.bind<ListIngredientsEditBinding>(itemView)
        private var ingredientName: EditText = itemView.findViewById(R.id.input_ingredient)
        private var deleteIngredientButton: ImageButton = itemView.findViewById(R.id.btn_delete_ingredient)

        init {

            ingredientName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    ingredients[adapterPosition].name = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            deleteIngredientButton.setOnClickListener {
                ingredients.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }
}