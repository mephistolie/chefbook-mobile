package com.cactusknights.chefbook.repositories.remote.api

import com.cactusknights.chefbook.core.retrofit.IdResponse
import com.cactusknights.chefbook.core.retrofit.LinkResponse
import com.cactusknights.chefbook.core.retrofit.MessageResponse
import com.cactusknights.chefbook.repositories.remote.dto.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface RecipesApi {
    @GET("/v1/recipes")
    suspend fun getRecipes(): Response<List<RecipeDto>>

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

    @Multipart
    @POST("/v1/recipes/{recipe_id}/pictures")
    suspend fun uploadRecipePicture(@Path("recipe_id") recipeId: String, @Part picture: MultipartBody.Part): Response<LinkResponse>

    @DELETE("/v1/recipes/{recipe_id}/pictures")
    suspend fun deleteRecipePicture(@Path("recipe_id") recipeId: String, @Body picture: DeleteRecipePictureInput): Response<MessageResponse>

    @GET("/v1/recipes/{recipe_id}/encryption")
    suspend fun getRecipeKeyLink(@Path("recipe_id") recipeId: String) : Response<LinkResponse>

    @Multipart
    @POST("/v1/recipes/{recipe_id}/encryption")
    suspend fun uploadRecipeKey(@Path("recipe_id") recipeId: String, @Part key: MultipartBody.Part): Response<LinkResponse>

    @DELETE("/v1/recipes/{recipe_id}/encryption")
    suspend fun deleteRecipeKey(@Path("recipe_id") recipeId: String): Response<MessageResponse>
}