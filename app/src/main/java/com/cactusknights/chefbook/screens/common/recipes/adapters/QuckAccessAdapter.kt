package com.cactusknights.chefbook.screens.common.recipes.adapters

import android.app.Activity
import android.view.LayoutInflater
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
import com.cactusknights.chefbook.databinding.CellRecipeMinimalBinding
import com.cactusknights.chefbook.models.RecipeInfo
import okhttp3.OkHttpClient
import javax.crypto.SecretKey

class QuckAccessAdapter(private val context: Activity, val listener: (RecipeInfo) -> Unit) : RecyclerView.Adapter<QuckAccessAdapter.ViewHolder>() {

    private var keys: Map<String, SecretKey> = mapOf()

    private val differCallback = object : DiffUtil.ItemCallback<RecipeInfo>() {

        override fun areItemsTheSame(oldRecipe: RecipeInfo, newRecipe: RecipeInfo): Boolean {
            return oldRecipe.id == newRecipe.id || oldRecipe.remoteId == newRecipe.remoteId
        }

        override fun areContentsTheSame(oldRecipe: RecipeInfo, newRecipe: RecipeInfo): Boolean {
            return oldRecipe.name == newRecipe.name && oldRecipe.time == newRecipe.time && oldRecipe.preview == newRecipe.preview
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class ViewHolder(val binding: CellRecipeMinimalBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                listener(differ.currentList[adapterPosition])
            }
        }

        fun setTime(time: Int) {
            binding.textTime.text = Utils.minutesToTimeString(time, itemView.context.resources)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = differ.currentList[position]
        holder.binding.textName.text = recipe.name
        holder.setTime(recipe.time)

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
                        .build()
                    previewLoader.enqueue(request)
                }
            }
        } else {
            holder.binding.ivCover.load(ContextCompat.getDrawable(holder.itemView.context, R.drawable.selector_orange_gradient))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CellRecipeMinimalBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setRecipeKeys(newKeys : Map<String, SecretKey>) {
        keys = newKeys
    }
}