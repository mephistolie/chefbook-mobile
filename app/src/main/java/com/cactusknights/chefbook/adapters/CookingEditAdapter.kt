package com.cactusknights.chefbook.adapters

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
import com.cactusknights.chefbook.databinding.ListStepsEditBinding

class CookingEditAdapter(private var steps: ArrayList<String>): RecyclerView.Adapter<CookingEditAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding?.step = steps[position]
        holder.number.text = (position+1).toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_steps_edit, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return steps.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = DataBindingUtil.bind<ListStepsEditBinding>(itemView)
        val number: TextView = itemView.findViewById(R.id.number)
        private var description: EditText = itemView.findViewById(R.id.description)
        private val deleteStepButton: ImageButton = itemView.findViewById(R.id.delete_step)

        init {
            description.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    steps[adapterPosition] = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            deleteStepButton.setOnClickListener {
                steps.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }
}