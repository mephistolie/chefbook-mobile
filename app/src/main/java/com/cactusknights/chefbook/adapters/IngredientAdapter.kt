package com.cactusknights.chefbook.adapters

import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.models.Ingredient
import com.cactusknights.chefbook.databinding.ListIngredientsBinding

class IngredientAdapter(private var ingredients: ArrayList<Ingredient>, val listener: IngredientClickListener): RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.binding?.ingredient = ingredient

        if (position+1 < ingredients.size && ingredients[position+1].isSection) {
            val params = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 0, 0, 24)
            holder.ingredientView.layoutParams = params
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_ingredients, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = DataBindingUtil.bind<ListIngredientsBinding>(itemView)
        val ingredientView: LinearLayout = itemView.findViewById(R.id.ll_ingredient)
        private val addToShopList: CheckBox = itemView.findViewById(R.id.checkbox_add_to_shopping_list)

        init {
            addToShopList.setOnClickListener {
                listener.onIngredientClick(adapterPosition)
            }
        }
    }

    interface IngredientClickListener {
        fun onIngredientClick(position: Int)
    }

}