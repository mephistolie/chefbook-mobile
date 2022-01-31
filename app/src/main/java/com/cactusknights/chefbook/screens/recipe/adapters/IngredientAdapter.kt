package com.cactusknights.chefbook.screens.recipe.adapters

import android.app.ActionBar
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.CellIngredientBinding
import com.cactusknights.chefbook.databinding.CellSectionBinding
import com.cactusknights.chefbook.models.*

class IngredientAdapter(val listener: (Int) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class IngredientHolder(val binding: CellIngredientBinding) : RecyclerView.ViewHolder(binding.root) {
        init { binding.checkboxSelected.setOnClickListener {
            differ.currentList[adapterPosition].isSelected = !differ.currentList[adapterPosition].isSelected
            listener(adapterPosition) }
        }
    }

    inner class SectionHolder(val binding: CellSectionBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Selectable<Ingredient>>() {
        override fun areItemsTheSame(oldIngredient: Selectable<Ingredient>, newIngredient: Selectable<Ingredient>): Boolean {
            return oldIngredient.item?.text == newIngredient.item?.text && oldIngredient.isSelected == newIngredient.isSelected && oldIngredient.item?.type == newIngredient.item?.type
        }

        override fun areContentsTheSame(oldIngredient: Selectable<Ingredient>, newIngredient: Selectable<Ingredient>): Boolean {
            return oldIngredient.item?.text == newIngredient.item?.text && oldIngredient.isSelected == newIngredient.isSelected && oldIngredient.item?.type == newIngredient.item?.type
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            val ingredient = differ.currentList[position].item
            val stringBuilder = SpannableStringBuilder(ingredient?.text)
            val digits = ingredient?.text?.map { it.isDigit() }
            if (digits != null && digits.indexOfFirst { it } >= 0) {
                val colorStyle = ForegroundColorSpan(ContextCompat.getColor(holder.itemView.context, R.color.deep_orange_light))
                stringBuilder.setSpan(colorStyle, digits.indexOfFirst { it }, digits.indexOfLast { it } + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            }

            (holder as IngredientHolder).binding.textName.text = stringBuilder
            holder.binding.checkboxSelected.isActivated = differ.currentList[position].isSelected
            holder.binding.textName.setTextColor(if (ingredient?.type == IngredientTypes.SECTION) ContextCompat.getColor(holder.itemView.context, R.color.navigation_ripple) else ContextCompat.getColor(holder.itemView.context, R.color.monochrome_invert))

            holder.binding.separator.visibility = if (position+1 == differ.currentList.size || differ.currentList[position+1].item!!.type == IngredientTypes.SECTION) View.GONE else View.VISIBLE
            setCornerRadius(holder.binding.root, position)
        } else {
            (holder as SectionHolder).binding.textName.text = differ.currentList[position].item?.text
            holder.binding.separator.visibility = if (position+1 == differ.currentList.size) View.GONE else View.VISIBLE
            setCornerRadius(holder.binding.root, position)
        }
    }

    private fun setCornerRadius(view: View, position: Int) {
        if (differ.currentList.size == 1 || position == 0 && differ.currentList[position+1].item!!.type == IngredientTypes.SECTION) {
            view.background = ContextCompat.getDrawable(view.context, R.drawable.selector_rounded)
            if (differ.currentList.size > 1) {
                val params = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
                params.setMargins(0, 0, 0, 36)
                view.layoutParams = params
            }
        } else if (position+1 == differ.currentList.size || differ.currentList[position+1].item!!.type == IngredientTypes.SECTION) {
            val params = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 0, 0, 36)
            view.layoutParams = params
            if (differ.currentList[position].item!!.type != IngredientTypes.SECTION) {
                view.background = ContextCompat.getDrawable(view.context, R.drawable.selector_rounded_bottom)
            } else {
                view.background = ContextCompat.getDrawable(view.context, R.drawable.selector_rounded)
            }
        } else if (position == 0) {
            view.background = ContextCompat.getDrawable(view.context, R.drawable.selector_rounded_top)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> { IngredientHolder(CellIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)) }
            else -> { SectionHolder(CellSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (differ.currentList[position].item?.type == IngredientTypes.INGREDIENT) 0 else 1
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}