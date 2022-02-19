package com.cactusknights.chefbook.screens.recipeinput.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.*
import com.cactusknights.chefbook.models.Category
import java.io.File

class StepPicturesAdapter(val deletePictureListener: (Int) -> Unit, val addPictureListener: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class PictureViewHolder(val binding: CellStepEditPictureBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                deletePictureListener(adapterPosition)
            }
        }
    }

    inner class AddViewHolder(val binding: CellStepEditPictureAddBinding) : RecyclerView.ViewHolder(binding.root) {
        init { binding.root.setOnClickListener { addPictureListener() } }
    }

    private val differCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldLink: String, newLink: String): Boolean = oldLink == newLink
        override fun areContentsTheSame(oldLink: String, newLink: String): Boolean = oldLink == newLink
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PictureViewHolder) {
            val picture = differ.currentList[position]
            if (URLUtil.isValidUrl(picture)) holder.binding.ivPicture.load(picture) else holder.binding.ivPicture.load(File(picture))
            holder.binding.root.setOnClickListener {
                deletePictureListener(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.cell_step_edit_picture) {
            PictureViewHolder(CellStepEditPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            AddViewHolder(CellStepEditPictureAddBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == differ.currentList.size) R.layout.cell_step_edit_picture_add else R.layout.cell_step_edit_picture
    }
}