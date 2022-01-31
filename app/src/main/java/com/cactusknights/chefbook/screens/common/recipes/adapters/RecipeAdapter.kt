package com.cactusknights.chefbook.screens.common.recipes.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
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

// TODO: Check for leaks by context
open class RecipeAdapter(private val context: Activity, private val listener: (Recipe) -> Unit) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    private var keys : Map<String, SecretKey> = mapOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.text_name)
        val favouriteIcon: CardView = itemView.findViewById(R.id.cv_favourite)
        val likedIcon: CardView = itemView.findViewById(R.id.cv_liked)
        val lockIcon: CardView = itemView.findViewById(R.id.cv_lock)
        val time: TextView = itemView.findViewById(R.id.text_time)
        val calories: TextView = itemView.findViewById(R.id.text_calories)
        val servings: TextView = itemView.findViewById(R.id.text_servings)
        val likes: TextView = itemView.findViewById(R.id.text_likes)
        val coverImage: ImageView = itemView.findViewById(R.id.iv_cover)

        init {
            itemView.setOnClickListener {
                listener(differ.currentList[adapterPosition])
            }
        }

        fun setTime(time: Int) {
            this.time.text = Utils.minutesToTimeString(time, itemView.context.resources)
        }

        fun setCalories(calories: Int) {
            if (calories > 0) {
                this.calories.text = calories.toString() + " " + itemView.context.resources.getString(R.string.kcal)
            } else {
                this.calories.text = "-"
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldRecipe: Recipe, newRecipe: Recipe): Boolean {
            return oldRecipe.id == newRecipe.id || oldRecipe.remoteId == newRecipe.remoteId
        }

        override fun areContentsTheSame(oldRecipe: Recipe, newRecipe: Recipe): Boolean {
            return (oldRecipe.name == newRecipe.name &&
                    oldRecipe.time == newRecipe.time &&
                    oldRecipe.calories == newRecipe.calories &&
                    oldRecipe.preview == newRecipe.preview &&
                    oldRecipe.isFavourite == newRecipe.isFavourite)
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = differ.currentList[position]
        holder.favouriteIcon.visibility = if (recipe.isFavourite) View.VISIBLE else View.GONE
        holder.likedIcon.visibility = if (recipe.isFavourite) View.VISIBLE else View.GONE
        holder.lockIcon.visibility = if (recipe.encrypted) View.VISIBLE else View.GONE
        holder.name.text = recipe.name
        holder.setTime(recipe.time)
        holder.servings.text =  recipe.servings.toString()
        holder.likes.text = recipe.likes.toString()
        holder.setCalories(recipe.calories)
        if (!recipe.preview.isNullOrEmpty()) {
            if (!recipe.encrypted) holder.coverImage.load(recipe.preview) { crossfade(true) }
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
                        .target(holder.coverImage)
                        .crossfade(true)
                        .build()
                    previewLoader.enqueue(request)
                }
            }} else {
            if (recipe.isFavourite) {
                holder.coverImage.load(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_broccy_heart_eyes)) { crossfade(true) }
            } else {
                holder.coverImage.load(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_broccy)) { crossfade(true) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cell_recipe, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setRecipeKeys(newKeys : Map<String, SecretKey>) {
        keys = newKeys
    }
}