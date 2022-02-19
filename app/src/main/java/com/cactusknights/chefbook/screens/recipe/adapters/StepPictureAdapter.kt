package com.cactusknights.chefbook.screens.recipe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cactusknights.chefbook.databinding.CellStepPictureBinding
import com.cactusknights.chefbook.screens.recipe.dialogs.PictureDialog

class StepPictureAdapter(
    val openPictureListener: (String) -> Unit
): RecyclerView.Adapter<StepPictureAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CellStepPictureBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldLink: String, newLink: String): Boolean = oldLink == newLink
        override fun areContentsTheSame(oldLink: String, newLink: String): Boolean = oldLink == newLink
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.ivPicture.load(differ.currentList[position]) { crossfade(true) }
        holder.binding.ivPicture.setOnClickListener {
            openPictureListener(differ.currentList[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CellStepPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}