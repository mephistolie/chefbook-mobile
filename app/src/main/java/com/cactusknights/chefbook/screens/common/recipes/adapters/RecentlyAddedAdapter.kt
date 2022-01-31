package com.cactusknights.chefbook.screens.common.recipes.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
import com.cactusknights.chefbook.models.Recipe
import okhttp3.OkHttpClient
import javax.crypto.SecretKey

class RecentlyAddedAdapter(private val context: Activity, val listener: (Recipe) -> Unit) : RecyclerView.Adapter<RecentlyAddedAdapter.ViewHolder>() {

    private var keys: Map<String, SecretKey> = mapOf()

    private val differCallback = object : DiffUtil.ItemCallback<Recipe>() {

        override fun areItemsTheSame(oldRecipe: Recipe, newRecipe: Recipe): Boolean {
            return oldRecipe.id == newRecipe.id || oldRecipe.remoteId == newRecipe.remoteId
        }

        override fun areContentsTheSame(oldRecipe: Recipe, newRecipe: Recipe): Boolean {
            return oldRecipe.name == newRecipe.name && oldRecipe.time == newRecipe.time && oldRecipe.preview == newRecipe.preview
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.text_name)
        val cover: ImageView = itemView.findViewById(R.id.iv_cover)
        val time: TextView = itemView.findViewById(R.id.text_time)

        init {
            itemView.setOnClickListener {
                listener(differ.currentList[adapterPosition])
            }
        }

        fun setTime(time: Int) {
            this.time.text = Utils.minutesToTimeString(time, itemView.context.resources)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = differ.currentList[position]
        holder.name.text = recipe.name
        holder.setTime(recipe.time)

        if (!recipe.preview.isNullOrEmpty()) {
            if (!recipe.encrypted) holder.cover.load(recipe.preview) { crossfade(true) }
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
                        .target(holder.cover)
                        .build()
                    previewLoader.enqueue(request)
                }
            }
        } else {
            holder.cover.load(ContextCompat.getDrawable(holder.itemView.context, R.drawable.selector_orange_gradient))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cell_recipe_minimal, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setRecipeKeys(newKeys : Map<String, SecretKey>) {
        keys = newKeys
    }
}