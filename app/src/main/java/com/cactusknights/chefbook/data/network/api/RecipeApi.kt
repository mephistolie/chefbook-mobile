package com.cactusknights.chefbook.data.network.api

import com.cactusknights.chefbook.data.dto.remote.common.IdResponse
import com.cactusknights.chefbook.data.dto.remote.common.LinkResponse
import com.cactusknights.chefbook.data.dto.remote.common.MessageResponse
import com.cactusknights.chefbook.data.dto.remote.common.RequestResult
import com.cactusknights.chefbook.data.dto.remote.recipe.RecipeCategoriesRequest
import com.cactusknights.chefbook.data.dto.remote.recipe.RecipeInfoResponse
import com.cactusknights.chefbook.data.dto.remote.recipe.RecipeInputRequest
import com.cactusknights.chefbook.data.dto.remote.recipe.RecipeResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {
    @GET("/v1/recipes")
    suspend fun getRecipes(
        @Query("search") search: String? = null,
        @Query("owned") owned: Boolean? = null,
        @Query("saved") saved: Boolean? = null,
        @Query("author_id") authorId: String? = null,
        @Query("sort_by") sortBy: String? = null,
        @Query("language") languages: List<String>? = null,
        @Query("min_time") minTime: Int? = null,
        @Query("max_time") maxTime: Int? = null,
        @Query("min_servings") minServings: Int? = null,
        @Query("max_servings") maxServings: Int? = null,
        @Query("min_calories") minCalories: Int? = null,
        @Query("max_calories") maxCalories: Int? = null,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
    ): RequestResult<List<RecipeInfoResponse>>

    @POST("/v1/recipes")
    suspend fun createRecipe(@Body recipe: RecipeInputRequest): RequestResult<IdResponse>

    @GET("/v1/recipes/{recipe_id}")
    suspend fun getRecipe(@Path("recipe_id") recipeId: String): RequestResult<RecipeResponse>

    @PUT("/v1/recipes/{recipe_id}")
    suspend fun updateRecipe(@Path("recipe_id") recipeId: String, @Body recipe: RecipeInputRequest): RequestResult<MessageResponse>

    @DELETE("/v1/recipes/{recipe_id}")
    suspend fun deleteRecipe(@Path("recipe_id") recipeId: String): RequestResult<MessageResponse>

    @POST("/v1/recipes/{recipe_id}/save")
    suspend fun addRecipeToRecipeBook(@Path("recipe_id") recipeId: String): RequestResult<MessageResponse>

    @DELETE("/v1/recipes/{recipe_id}/save")
    suspend fun removeRecipeFromRecipeBook(@Path("recipe_id") recipeId: String): RequestResult<MessageResponse>

    @PUT("/v1/recipes/{recipe_id}/favourite")
    suspend fun markRecipeFavourite(@Path("recipe_id") recipeId: String): RequestResult<MessageResponse>

    @DELETE("/v1/recipes/{recipe_id}/favourite")
    suspend fun unmarkRecipeFavourite(@Path("recipe_id") recipeId: String): RequestResult<MessageResponse>

    @PUT("/v1/recipes/{recipe_id}/likes")
    suspend fun likeRecipe(@Path("recipe_id") recipeId: String): RequestResult<MessageResponse>

    @DELETE("/v1/recipes/{recipe_id}/likes")
    suspend fun unlikeRecipe(@Path("recipe_id") recipeId: String): RequestResult<MessageResponse>

    @PUT("/v1/recipes/{recipe_id}/categories")
    suspend fun setRecipeCategories(@Path("recipe_id") recipeId: String, @Body body: RecipeCategoriesRequest): RequestResult<MessageResponse>

    @GET("/v1/recipes/{recipe_id}/pictures")
    suspend fun getRecipePictures(@Path("recipe_id") recipeId: String): RequestResult<List<String>>

    @Multipart
    @POST("/v1/recipes/{recipe_id}/pictures")
    suspend fun uploadRecipePicture(@Path("recipe_id") recipeId: String, @Part picture: MultipartBody.Part): RequestResult<LinkResponse>

    @DELETE("/v1/recipes/{recipe_id}/pictures/{picture_name}")
    suspend fun deleteRecipePicture(@Path("recipe_id") recipeId: String, @Path("picture_name") pictureName: String): RequestResult<MessageResponse>
}