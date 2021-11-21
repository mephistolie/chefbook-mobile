package com.cactusknights.chefbook.screens.recipe.adapters

import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.databinding.ListSelectableBinding
import com.cactusknights.chefbook.models.MarkdownString
import com.cactusknights.chefbook.models.MarkdownTypes

class IngredientAdapter(val listener: (Int) -> Unit): RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = DataBindingUtil.bind<ListSelectableBinding>(itemView)
        val ingredientView: LinearLayout = itemView.findViewById(R.id.ll_selectable)
        private val addToShopList: CheckBox = itemView.findViewById(R.id.checkbox_selected)

        init { addToShopList.setOnClickListener { listener(adapterPosition) } }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Selectable<MarkdownString>>() {
        override fun areItemsTheSame(oldIngredient: Selectable<MarkdownString>, newIngredient: Selectable<MarkdownString>): Boolean {
            return oldIngredient.item?.text == newIngredient.item?.text && oldIngredient.isSelected == newIngredient.isSelected && oldIngredient.item?.type == newIngredient.item?.type
        }

        override fun areContentsTheSame(oldIngredient: Selectable<MarkdownString>, newIngredient: Selectable<MarkdownString>): Boolean {
            return oldIngredient.item?.text == newIngredient.item?.text && oldIngredient.isSelected == newIngredient.isSelected && oldIngredient.item?.type == newIngredient.item?.type
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = differ.currentList[position].item
        holder.binding?.selectableItem = ingredient
        holder.binding?.checkboxSelected?.isActivated = differ.currentList[position].isSelected

        if (position+1 < differ.currentList.size && differ.currentList[position+1].item!!.type == MarkdownTypes.HEADER) {
            val params = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 0, 0, 24)
            holder.ingredientView.layoutParams = params
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_selectable, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}