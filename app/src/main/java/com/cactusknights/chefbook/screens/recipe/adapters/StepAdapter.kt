package com.cactusknights.chefbook.screens.recipe.adapters

import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.*
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.forceSubmitList
import com.cactusknights.chefbook.databinding.CellSectionBinding
import com.cactusknights.chefbook.databinding.CellStepBinding
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.screens.main.fragments.categories.adapters.CategoryItemDecoration

class StepAdapter(
    val openPictureListener: (String) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class StepHolder(val binding: CellStepBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.rvPictures.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            binding.rvPictures.adapter = StepPictureAdapter(openPictureListener)
        }
    }
    inner class SectionHolder(val binding: CellSectionBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<CookingStep>() {
        override fun areItemsTheSame(oldStep: CookingStep, newStep: CookingStep): Boolean {
            return oldStep.text == newStep.text
        }

        override fun areContentsTheSame(oldStep: CookingStep, newStep: CookingStep): Boolean {
            return oldStep.text == newStep.text && oldStep.type == newStep.type
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            (holder as StepHolder)
            val sectionsSize = differ.currentList.subList(0, position).filter { it.type == CookingStepTypes.SECTION }.size
            if (differ.currentList[position].text.isNotEmpty()) {
                holder.binding.textDescription.visibility = View.VISIBLE
                holder.binding.textDescription.text = differ.currentList[position].text
            } else holder.binding.textDescription.visibility = View.GONE

            holder.binding.textNumber.text = ((position+1-sectionsSize).toString()+".")

            holder.binding.separator.visibility = if (position+1 == differ.currentList.size || differ.currentList[position+1].type ==  CookingStepTypes.SECTION) View.GONE else View.VISIBLE
            setCornerRadius(holder.binding.root, position)

            val pictures = differ.currentList[position].pictures
            if (pictures.isNotEmpty()) {
                holder.binding.rvPictures.visibility = View.VISIBLE
                (holder.binding.rvPictures.adapter as StepPictureAdapter).differ.forceSubmitList(differ.currentList[position].pictures)
            } else holder.binding.rvPictures.visibility = View.GONE
        } else {
            (holder as SectionHolder).binding.textName.text = differ.currentList[position].text
            holder.binding.separator.visibility = if (position+1 == differ.currentList.size) View.GONE else View.VISIBLE
            setCornerRadius(holder.binding.root, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> { StepHolder(CellStepBinding.inflate(LayoutInflater.from(parent.context), parent, false)) }
            else -> { SectionHolder(CellSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)) }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun getItemViewType(position: Int): Int {
        return if (differ.currentList[position].type != CookingStepTypes.SECTION) 0 else 1
    }

    private fun setCornerRadius(view: View, position: Int) {
        if (differ.currentList.size == 1 || position == 0 && differ.currentList[position+1].type == CookingStepTypes.SECTION) {
            view.background = ContextCompat.getDrawable(view.context, R.drawable.selector_rounded)
            if (differ.currentList.size > 1) {
                val params = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
                params.setMargins(0, 0, 0, 36)
                view.layoutParams = params
            }
        } else if (position+1 == differ.currentList.size || differ.currentList[position+1].type == CookingStepTypes.SECTION) {
            val params = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 0, 0, 36)
            view.layoutParams = params
            if (differ.currentList[position].type != CookingStepTypes.SECTION) {
                view.background = ContextCompat.getDrawable(view.context, R.drawable.selector_rounded_bottom)
            } else {
                view.background = ContextCompat.getDrawable(view.context, R.drawable.selector_rounded)
            }
        } else if (position == 0) {
            view.background = ContextCompat.getDrawable(view.context, R.drawable.selector_rounded_top)
        }
    }
}