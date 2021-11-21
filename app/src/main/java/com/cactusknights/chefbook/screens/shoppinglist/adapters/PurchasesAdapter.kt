package com.cactusknights.chefbook.screens.shoppinglist.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.models.Selectable


class PurchasesAdapter(val listener: (Int) -> Unit): RecyclerView.Adapter<PurchasesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.text_name)
        val isPurchased: CheckBox = itemView.findViewById(R.id.checkbox_selected)
        val slider: ImageView = itemView.findViewById(R.id.iv_slider)

        init {
            isPurchased.isClickable = false
            isPurchased.setOnClickListener {
                if (isPurchased.isChecked) {
                    name.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    slider.visibility = View.GONE
                } else {
                    name.paintFlags = name.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    slider.visibility = View.VISIBLE
                }
                listener(adapterPosition)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Selectable<String>>() {
        override fun areItemsTheSame(oldPurchase: Selectable<String>, newPurchase: Selectable<String>): Boolean {
            return oldPurchase.item == newPurchase.item
        }

        override fun areContentsTheSame(oldPurchase: Selectable<String>, newPurchase: Selectable<String>): Boolean {
            return oldPurchase.item == newPurchase.item && oldPurchase.isSelected == newPurchase.isSelected
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = differ.currentList[position].item
        holder.isPurchased.isChecked = differ.currentList[position].isSelected
        if (differ.currentList[position].isSelected) {
            holder.slider.visibility = View.GONE
            holder.name.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.name.paintFlags = holder.name.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.slider.visibility = View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_purchase, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}