package com.cactusknights.chefbook.legacy.adapters

import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.ListSelectableBinding
import com.cactusknights.chefbook.databinding.ListStepsBinding
import com.cactusknights.chefbook.models.Selectable

class CookingAdapter(private var steps: ArrayList<Selectable<String>>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            (holder as CookingAdapter.StepHolder).binding?.step = steps[position].item
            val sectionsSize = steps.subList(0, position).filter { it.isSelected }.size
            holder.number.text = ((position+1-sectionsSize).toString()+".")
            if (position+1 < steps.size && steps[position+1].isSelected) {
                val params = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
                params.setMargins(0, 0, 0, 24)
                holder.stepView.layoutParams = params
            }
        } else {
            val ingredient = steps[position]
            (holder as SectionHolder).binding?.selectableItem = ingredient
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.list_steps, parent, false)
                return StepHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.list_selectable, parent, false)
                return SectionHolder(v)
            }
        }
    }

    override fun getItemCount(): Int {
        return steps.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (!steps[position].isSelected) 0 else 1
    }

    inner class StepHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = DataBindingUtil.bind<ListStepsBinding>(itemView)
        val number: TextView = itemView.findViewById(R.id.text_number)
        val stepView: ConstraintLayout = itemView.findViewById(R.id.ll_step)
    }

    inner class SectionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = DataBindingUtil.bind<ListSelectableBinding>(itemView)
    }
}