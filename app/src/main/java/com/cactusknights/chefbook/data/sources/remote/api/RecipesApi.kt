package com.cactusknights.chefbook.data.sources.remote.api

import com.cactusknights.chefbook.core.retrofit.IdResponse
import com.cactusknights.chefbook.core.retrofit.LinkResponse
import com.cactusknights.chefbook.core.retrofit.MessageResponse
import com.cactusknights.chefbook.data.sources.remote.dto.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface RecipesApi {
    @GET("/v1/recipes")
    suspend fun getRecipes(
        @Query("owned") owned : Boolean = true,
        @Query("author_id") authorId : Int = 0,
        @Query("page") page : Int = 0,
        @Query("page_zie") pageSize : Int = 20,
        @Query("min_time") minTime : Int = 0,
        @Query("max_time") maxTime : Int = 0,
        @Query("min_servings") minServings : Int = 0,
        @Query("max_servings") maxServings : Int = 0,
        @Query("min_calories") minCalories : Int = 0,
        @Query("max_calories") maxCalories : Int = 0,
        @Query("sort_by") sortBy : String = "",
    ): Response<List<RecipeDto>>

    @POST("/v1/recipes")
    suspend fun createRecipe(@Body recipe: RecipeInputDto): Response<IdResponse>

    @GET("/v1/recipes/{recipe_id}")
    suspend fun getRecipe(@Path("recipe_id") recipeId: String): Response<RecipeDto>

    @PUT("/v1/recipes/{recipe_id}")
    suspend fun updateRecipe(@Path("recipe_id") recipeId: String, @Body recipe: RecipeInputDto): Response<MessageResponse>

    @DELETE("/v1/recipes/{recipe_id}")
    suspend fun deleteRecipe(@Path("recipe_id") recipeId: String): Response<MessageResponse>

    @PUT("/v1/recipes/favourites/{recipe_id}")
    suspend fun markRecipeFavourite(@Path("recipe_id") recipeId: String): Response<MessageResponse>

    @DELETE("/v1/recipes/favourites/{recipe_id}")
    suspend fun unmarkRecipeFavourite(@Path("recipe_id") recipeId: String): Response<MessageResponse>

    @PUT("/v1/recipes/liked/{recipe_id}")
    suspend fun likeRecipe(@Path("recipe_id") recipeId: String): Response<MessageResponse>

    @DELETE("/v1/recipes/liked/{recipe_id}")
    suspend fun unlikeRecipe(@Path("recipe_id") recipeId: String): Response<MessageResponse>

    @PUT("/v1/recipes/{recipe_id}/categories")
    suspend fun setRecipeCategories(@Path("recipe_id") recipeId: String, @Body categories: List<Int>): Response<MessageResponse>

    @GET("/v1/recipes/{recipe_id}/pictures")
    suspend fun getRecipePictures(@Path("recipe_id") recipeId: String): Response<List<String>>

    @Multipart
    @POST("/v1/recipes/{recipe_id}/pictures")
    suspend fun uploadRecipePicture(@Path("recipe_id") recipeId: String, @Part picture: MultipartBody.Part): Response<LinkResponse>

    @DELETE("/v1/recipes/{recipe_id}/pictures/{picture_name}")
    suspend fun deleteRecipePicture(@Path("recipe_id") recipeId: String, @Path("picture_name") pictureName: String): Response<MessageResponse>
}