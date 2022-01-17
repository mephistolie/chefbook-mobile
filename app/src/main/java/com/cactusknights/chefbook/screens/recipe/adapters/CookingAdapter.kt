package com.cactusknights.chefbook.screens.recipe.adapters

import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.CellIngredientBinding
import com.cactusknights.chefbook.databinding.CellStepBinding
import com.cactusknights.chefbook.models.MarkdownString
import com.cactusknights.chefbook.models.MarkdownTypes

class CookingAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class StepHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = DataBindingUtil.bind<CellStepBinding>(itemView)
        val number: TextView = itemView.findViewById(R.id.text_number)
        val stepView: ConstraintLayout = itemView.findViewById(R.id.ll_step)
    }

    inner class SectionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.text_name)
        private val isSelected: CheckBox = itemView.findViewById(R.id.checkbox_selected)

        init {
            name.textSize = 22F
            name.setTextColor(ContextCompat.getColor(itemView.context, R.color.navigation_ripple))
            isSelected.visibility = View.GONE
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<MarkdownString>() {
        override fun areItemsTheSame(oldPurchase: MarkdownString, newPurchase: MarkdownString): Boolean {
            return oldPurchase.text == newPurchase.text
        }

        override fun areContentsTheSame(oldPurchase: MarkdownString, newPurchase: MarkdownString): Boolean {
            return oldPurchase.text == newPurchase.text && oldPurchase.type == newPurchase.type
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            (holder as StepHolder).binding?.step = differ.currentList[position].text
            val sectionsSize = differ.currentList.subList(0, position).filter { it.type == MarkdownTypes.HEADER }.size
            holder.number.text = ((position+1-sectionsSize).toString()+".")
            if (position+1 < differ.currentList.size && differ.currentList[position+1].type == MarkdownTypes.HEADER) {
                val params = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
                params.setMargins(0, 0, 0, 24)
                holder.stepView.layoutParams = params
            }
        } else {
            val ingredient = differ.currentList[position]
            (holder as SectionHolder).name.text = ingredient.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.cell_step, parent, false)
                StepHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.cell_ingredient, parent, false)
                SectionHolder(v)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (differ.currentList[position].type != MarkdownTypes.HEADER) 0 else 1
    }
}