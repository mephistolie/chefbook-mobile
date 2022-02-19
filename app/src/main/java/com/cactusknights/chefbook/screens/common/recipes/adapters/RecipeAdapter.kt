package com.cactusknights.chefbook.screens.common.recipes.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.util.CoilUtils
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.Utils
import com.cactusknights.chefbook.core.retrofit.interceptors.EncryptedDataInterceptor
import com.cactusknights.chefbook.databinding.CellRecipeBinding
import com.cactusknights.chefbook.models.RecipeInfo
import okhttp3.OkHttpClient
import javax.crypto.SecretKey

// TODO: Check for leaks by context
open class RecipeAdapter(private val context: Activity, private val listener: (RecipeInfo) -> Unit) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    private var keys : Map<String, SecretKey> = mapOf()

    inner class ViewHolder(val binding: CellRecipeBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                listener(differ.currentList[adapterPosition])
            }
        }

        fun setTime(time: Int) {
            this.binding.textTime.text = Utils.minutesToTimeString(time, itemView.context.resources)
        }

        fun setCalories(calories: Int) {
            if (calories > 0) {
                this.binding.textCalories.text = calories.toString() + " " + itemView.context.resources.getString(R.string.kcal)
            } else {
                this.binding.textCalories.text = "-"
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<RecipeInfo>() {
        override fun areItemsTheSame(oldRecipe: RecipeInfo, newRecipe: RecipeInfo): Boolean {
            return oldRecipe.id == newRecipe.id || oldRecipe.remoteId == newRecipe.remoteId
        }

        override fun areContentsTheSame(oldRecipe: RecipeInfo, newRecipe: RecipeInfo): Boolean {
            return (oldRecipe.name == newRecipe.name &&
                    oldRecipe.time == newRecipe.time &&
                    oldRecipe.calories == newRecipe.calories &&
                    oldRecipe.preview == newRecipe.preview &&
                    oldRecipe.isFavourite == newRecipe.isFavourite &&
                    oldRecipe.isLiked == newRecipe.isLiked &&
                    oldRecipe.likes == newRecipe.likes)
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = differ.currentList[position]
        holder.binding.cvFavourite.visibility = if (recipe.isFavourite) View.VISIBLE else View.GONE
        holder.binding.cvLiked.visibility = if (recipe.isLiked) View.VISIBLE else View.GONE
        holder.binding.cvLock.visibility = if (recipe.isEncrypted) View.VISIBLE else View.GONE
        holder.binding.textName.text = recipe.name
        holder.setTime(recipe.time)
        holder.binding.textServings.text =  recipe.servings.toString()
        holder.binding.textLikes.text = recipe.likes.toString()
        holder.setCalories(recipe.calories)
        if (!recipe.preview.isNullOrEmpty()) {
            if (!recipe.isEncrypted) holder.binding.ivCover.load(recipe.preview) { crossfade(true) }
            else {
                val key = keys[recipe.preview]
                if (key != null) {
                    val previewLoader = ImageLoader.Builder(holder.itemView.context)
                        .okHttpClient {
                            OkHttpClient.Builder()
                                .cache(CoilUtils.createDefaultCache(holder.itemView.context))
                                .addInterceptor(EncryptedDataInterceptor(context, key))
                                .build()
                        }.build()
                    val request = ImageRequest.Builder(holder.itemView.context)
                        .data(recipe.preview)
                        .target(holder.binding.ivCover)
                        .crossfade(true)
                        .build()
                    previewLoader.enqueue(request)
                }
            }} else {
            if (recipe.isFavourite) {
                holder.binding.ivCover.load(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_broccy_heart_eyes)) { crossfade(true) }
            } else {
                holder.binding.ivCover.load(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_broccy)) { crossfade(true) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CellRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setRecipeKeys(newKeys : Map<String, SecretKey>) {
        keys = newKeys
    }
}