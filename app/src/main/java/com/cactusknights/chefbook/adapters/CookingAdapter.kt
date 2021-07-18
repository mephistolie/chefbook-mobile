package com.cactusknights.chefbook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.ListStepsBinding

class CookingAdapter(private var steps: ArrayList<String>): RecyclerView.Adapter<CookingAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding?.step = steps[position]
        holder.number.text = ((position+1).toString()+".")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_steps, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return steps.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = DataBindingUtil.bind<ListStepsBinding>(itemView)
        val number: TextView = itemView.findViewById(R.id.number)
    }
}