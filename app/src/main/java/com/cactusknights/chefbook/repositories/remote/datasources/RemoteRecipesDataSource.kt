package com.cactusknights.chefbook.repositories.remote.datasources

import android.webkit.URLUtil
import com.cactusknights.chefbook.domain.ServerRecipeDataSource
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.repositories.remote.api.ChefBookApi
import com.cactusknights.chefbook.repositories.remote.dto.DeleteRecipePictureInput
import com.cactusknights.chefbook.repositories.remote.dto.toRecipe
import com.cactusknights.chefbook.repositories.remote.dto.toRecipeInputDto
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject

class RemoteRecipesDataSource @Inject constructor(
    private val api: ChefBookApi
) : ServerRecipeDataSource {
    override suspend fun getRecipes(): ArrayList<Recipe> {
        val response = api.getRecipes()
        val recipeDtos = response.body()
        if (recipeDtos != null) {
            val recipes: ArrayList<Recipe> = arrayListOf()
            return recipeDtos.map { it.toRecipe() }.toCollection(recipes)
        } else throw IOException()
    }

    override suspend fun addRecipe(recipe: Recipe): Recipe {
        val response = api.createRecipe(recipe.toRecipeInputDto())
        recipe.remoteId = response.body()!!.id
        return uploadRecipePictures(recipe)
    }

    override suspend fun getRecipe(recipeId: Int): Recipe {
        val response = api.getRecipe(recipeId.toString())
        val recipeDto = response.body()
        if (recipeDto != null) return recipeDto.toRecipe()
        else throw IOException()
    }

    override suspend fun updateRecipe(recipe: Recipe): Recipe {
        api.updateRecipe(recipe.remoteId.toString(), recipe.toRecipeInputDto())
        return uploadRecipePictures(recipe)
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        api.deleteRecipe(recipe.remoteId!!.toString())
    }

    override suspend fun setRecipeFavouriteStatus(recipe: Recipe) {
        if (recipe.isFavourite) { api.markRecipeFavourite(recipe.remoteId.toString()) }
        else { api.unmarkRecipeFavourite(recipe.remoteId.toString()) }
    }

    override suspend fun setRecipeCategories(recipe: Recipe) {
        api.setRecipeCategories(recipe.remoteId.toString(), recipe.categories)
    }

    override suspend fun setRecipeLikeStatus(recipe: Recipe) {
        if (recipe.isLiked) { api.likeRecipe(recipe.remoteId.toString()) }
        else { api.unlikeRecipe(recipe.remoteId.toString()) }
    }

    private suspend fun uploadRecipePictures(newRecipe: Recipe, oldRecipe: Recipe? = null): Recipe {
        if (newRecipe.remoteId == null) throw IOException()

        var isUpdateNeed = false

        val oldPreview = oldRecipe?.preview.orEmpty()
        val newPreview = newRecipe.preview.orEmpty()

        // Send local preview to server
        if (!URLUtil.isValidUrl(newRecipe.preview)) {
            isUpdateNeed = true
            val preview = File(newPreview)
            val file = preview.asRequestBody(contentType = "image/jpeg".toMediaTypeOrNull())
            val response = api.uploadRecipePicture(newRecipe.remoteId.toString(), MultipartBody.Part.createFormData("file", "file", file))
            newRecipe.preview = response.body()?.link
        }

        // Delete old preview if need
        if (newPreview != oldPreview && URLUtil.isValidUrl(oldPreview)) {
            api.deleteRecipePicture(newRecipe.remoteId.toString(), DeleteRecipePictureInput(getPictureNameByURL(oldPreview)))
        }

        if (isUpdateNeed) api.updateRecipe(newRecipe.remoteId.toString(), newRecipe.toRecipeInputDto())
        return newRecipe
    }

    private fun getPictureNameByURL(url: String): String {
        val separator = url.lastIndexOf('/')
        if (separator == -1) throw IOException()
        return url.substring(separator+1)
    }
}