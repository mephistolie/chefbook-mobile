package com.cactusknights.chefbook.legacy.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.ListSelectableEditBinding
import com.cactusknights.chefbook.databinding.ListStepsEditBinding
import com.cactusknights.chefbook.models.Selectable

class CookingEditAdapter(private var steps: MutableList<Selectable<String>>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            (holder as StepHolder).binding?.step = steps[position].item
            val sectionsSize = steps.subList(0, position).filter { it.isSelected }.size
            holder.number.text = (position+1-sectionsSize).toString()
            holder.binding?.inputStep?.requestFocus()
        } else {
            val section = steps[position]
            (holder as SectionHolder).binding?.selectableItem = section
            holder.binding?.inputIngredient?.requestFocus()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.list_steps_edit, parent, false)
                return StepHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.list_selectable_edit, parent, false)
                return SectionHolder(v)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (!steps[position].isSelected) 0 else 1
    }

    override fun getItemCount(): Int {
        return steps.size
    }

    inner class StepHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = DataBindingUtil.bind<ListStepsEditBinding>(itemView)
        val number: TextView = itemView.findViewById(R.id.text_number)
        private var description: EditText = itemView.findViewById(R.id.input_step)
        private val deleteStepButton: ImageButton = itemView.findViewById(R.id.btn_delete_step)

        init {
            description.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    steps[adapterPosition].item = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            deleteStepButton.setOnClickListener {
                steps.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }

    inner class SectionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = DataBindingUtil.bind<ListSelectableEditBinding>(itemView)
        private var ingredientName: EditText = itemView.findViewById(R.id.input_ingredient)
        private var deleteIngredientButton: ImageButton = itemView.findViewById(R.id.btn_delete_ingredient)

        init {

            ingredientName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    steps[adapterPosition].item = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            deleteIngredientButton.setOnClickListener {
                steps.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }
}