package com.cactusknights.chefbook.screens.recipeinput.adapters

import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.CellSelectableEditBinding
import com.cactusknights.chefbook.databinding.CellStepEditBinding
import com.cactusknights.chefbook.models.CookingStep
import com.cactusknights.chefbook.models.CookingStepTypes

class CookingInputAdapter(private var steps: MutableList<CookingStep>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            (holder as StepHolder).binding.inputStep.setText(steps[position].text)
            val sectionsSize = steps.subList(0, position).filter { it.type == CookingStepTypes.SECTION }.size
            holder.binding.textNumber.text = (position+1-sectionsSize).toString()
            holder.binding.inputStep.requestFocus()
        } else {
            (holder as SectionHolder).binding.inputSelectable.setText(steps[position].text)
            holder.binding.inputSelectable.setTypeface(null, Typeface.BOLD)
            holder.binding.inputSelectable.hint = holder.binding.inputSelectable.resources?.getString(R.string.section)
            holder.binding.inputSelectable.requestFocus()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> { StepHolder(CellStepEditBinding.inflate(LayoutInflater.from(parent.context), parent, false)) }
            else -> { SectionHolder(CellSelectableEditBinding.inflate(LayoutInflater.from(parent.context), parent, false)) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (steps[position].type == CookingStepTypes.STEP) 0 else 1
    }

    override fun getItemCount(): Int {
        return steps.size
    }

    inner class StepHolder(val binding: CellStepEditBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.inputStep.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    steps[adapterPosition].text = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            binding.btnDeleteStep.setOnClickListener {
                steps.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }

    inner class SectionHolder(val binding: CellSelectableEditBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.inputSelectable.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    steps[adapterPosition].text = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            binding.btnDeleteSelectable.setOnClickListener {
                steps.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }
}