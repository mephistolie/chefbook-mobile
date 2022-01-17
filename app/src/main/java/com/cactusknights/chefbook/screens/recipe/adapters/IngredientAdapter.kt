package com.cactusknights.chefbook.screens.recipe.adapters

import android.app.ActionBar
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.models.MarkdownString
import com.cactusknights.chefbook.models.MarkdownTypes
import android.text.style.StyleSpan
import android.util.Log
import androidx.core.content.ContextCompat


class IngredientAdapter(val listener: (Int) -> Unit): RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    companion object {
        var boldStyle = StyleSpan(Typeface.BOLD)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.text_name)
        val isSelected: CheckBox = itemView.findViewById(R.id.checkbox_selected)
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
        val stringBuilder = SpannableStringBuilder(ingredient?.text)
        val digits = ingredient?.text?.map { Log.e("Test", it.toString()); it.isDigit() }
        if (digits != null && digits.indexOfFirst { it } >= 0) {
            stringBuilder.setSpan(boldStyle, digits.indexOfFirst { it }, digits.indexOfLast { it } + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }

        holder.name.text = stringBuilder
        holder.isSelected.isActivated = differ.currentList[position].isSelected
        holder.isSelected.visibility = if (ingredient?.type == MarkdownTypes.HEADER) View.GONE else View.VISIBLE
        holder.name.textSize =  if (ingredient?.type == MarkdownTypes.HEADER) 22F else 20F
        holder.name.setTextColor(if (ingredient?.type == MarkdownTypes.HEADER) ContextCompat.getColor(holder.itemView.context, R.color.navigation_ripple) else ContextCompat.getColor(holder.itemView.context, R.color.monochrome_invert))

        if (position+1 < differ.currentList.size && differ.currentList[position+1].item!!.type == MarkdownTypes.HEADER) {
            val params = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 0, 0, 24)
            holder.ingredientView.layoutParams = params
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cell_ingredient, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}