package com.cactusknights.chefbook.screens.main.fragments.shoppinglist.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.CellPurchaseBinding
import com.cactusknights.chefbook.models.Purchase


class PurchasesAdapter(val listener: (Int) -> Unit): RecyclerView.Adapter<PurchasesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CellPurchaseBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.checkboxSelected.isClickable = false
            binding.checkboxSelected.setOnClickListener {
                if (binding.checkboxSelected.isChecked) {
                    binding.textName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    binding.textName.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.tab_indicator_text))
                    binding.ivSlider.visibility = View.GONE
                } else {
                    binding.textName.paintFlags = binding.textName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    binding.textName.setTextColor(ContextCompat.getColor(itemView.context, R.color.monochrome_invert))
                    binding.ivSlider.visibility = View.VISIBLE
                }
                listener(adapterPosition)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Purchase>() {
        override fun areItemsTheSame(oldPurchase: Purchase, newPurchase: Purchase): Boolean {
            return oldPurchase.id == newPurchase.id
        }

        override fun areContentsTheSame(oldPurchase: Purchase, newPurchase: Purchase): Boolean {
            return oldPurchase.name == newPurchase.name
                    && oldPurchase.isPurchased == newPurchase.isPurchased
                    && oldPurchase.multiplier == newPurchase.multiplier
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val purchase = differ.currentList[position]
        holder.binding.textName.text = purchase.name
        holder.binding.textMultiplier.text = "x${purchase.multiplier}"
        if (purchase.multiplier > 1) holder.binding.textMultiplier.visibility = View.VISIBLE else View.GONE
        holder.binding.checkboxSelected.isChecked = purchase.isPurchased
        if (purchase.isPurchased) {
            holder.binding.ivSlider.visibility = View.GONE
            holder.binding.textName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.textName.setTextColor(ContextCompat.getColor(holder.binding.textName.context, android.R.color.tab_indicator_text))
        } else {
            holder.binding.textName.paintFlags = holder.binding.textName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.binding.textName.setTextColor(ContextCompat.getColor(holder.binding.textName.context, R.color.monochrome_invert))
            holder.binding.ivSlider.visibility = View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CellPurchaseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}