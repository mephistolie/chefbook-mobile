package com.cactusknights.chefbook.repositories.remote.api

import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.models.retrofit.*
import com.cactusknights.chefbook.repositories.remote.dto.RecipeDto
import com.cactusknights.chefbook.repositories.remote.dto.RecipeFavouriteInputDto
import com.cactusknights.chefbook.repositories.remote.dto.RecipeInputDto
import com.cactusknights.chefbook.repositories.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.*

interface ChefBookApi {

    @POST("/v1/auth/sign-up")
    suspend fun signUp(@Body authData: AuthData): Response<IdResponse>

    @POST("/v1/auth/sign-in")
    suspend fun signIn(@Body authData: AuthData): Response<TokenResponse>


    @GET("/v1/users/")
    suspend fun getUserInfo(): Response<UserDto>


    @GET("/v1/recipes/")
    suspend fun getRecipes(): Response<ArrayList<RecipeDto>>

    @POST("/v1/recipes/create")
    suspend fun createRecipe(@Body recipe: RecipeInputDto): Response<IdResponse>

    @PUT("/v1/recipes/{recipe_id}")
    suspend fun updateRecipe(@Path("recipe_id") recipeId: String, @Body recipe: RecipeInputDto): Response<MessageResponse>

    @DELETE("/v1/recipes/{recipe_id}")
    suspend fun deleteRecipe(@Path("recipe_id") recipeId: String): Response<MessageResponse>

    @PUT("/v1/recipes/mark-favourite")
    suspend fun markRecipeFavourite(@Body recipe: RecipeFavouriteInputDto): Response<MessageResponse>


    @GET("/v1/categories/")
    suspend fun getCategories(): Response<ArrayList<Category>>

    @POST("/v1/categories/add")
    suspend fun addCategory(@Body category: Category): Response<IdResponse>

    @PUT("/v1/categories/{category_id}")
    suspend fun updateCategory(@Path("category_id") categoryId: String, @Body category: Category): Response<MessageResponse>

    @DELETE("/v1/categories/{category_id}")
    suspend fun deleteCategory(@Path("category_id") categoryId: String): Response<MessageResponse>


    @GET("/v1/shopping-list")
    suspend fun getShoppingList(): Response<ArrayList<Selectable<String>>>

    @POST("/v1/shopping-list")
    suspend fun setShoppingList(@Body shoppingList: ArrayList<Selectable<String>>): Response<MessageResponse>

    @PUT("/v1/shopping-list")
    suspend fun addToShoppingList(@Body shoppingList: ArrayList<Selectable<String>>): Response<MessageResponse>
}

interface SessionApi {
    @POST("/v1/auth/refresh")
    suspend fun refreshSession(@Body elementModel: RefreshToken): Response<TokenResponse>

    @POST("/v1/auth/sign-out")
    suspend fun signOut(@Body elementModel: RefreshToken): Response<MessageResponse>
}