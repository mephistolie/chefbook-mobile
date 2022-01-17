package com.cactusknights.chefbook.repositories.remote.api

import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Purchase
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.models.retrofit.*
import com.cactusknights.chefbook.repositories.remote.dto.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ChefBookApi {

    @POST("/v1/auth/sign-up")
    suspend fun signUp(@Body authData: AuthData): Response<IdResponse>

    @POST("/v1/auth/sign-in")
    suspend fun signIn(@Body authData: AuthData): Response<TokenResponse>

    @POST("/v1/auth/sign-out")
    suspend fun signOut(@Body elementModel: RefreshToken): Response<MessageResponse>

    @GET("/v1/users")
    suspend fun getUserInfo(): Response<UserDto>

    @PUT("/v1/users/change-name")
    suspend fun changeName(@Body username: UsernameInputDto): Response<MessageResponse>

    @Multipart
    @POST("/v1/users/avatar")
    suspend fun uploadAvatar(@Part image: MultipartBody.Part): Response<LinkResponse>

    @DELETE("/v1/users/avatar")
    suspend fun deleteAvatar(): Response<MessageResponse>

    @GET("/v1/users/key")
    suspend fun getUserKeyLink() : Response<LinkResponse>

    @Multipart
    @POST("/v1/users/key")
    suspend fun uploadUserKey(@Part key: MultipartBody.Part): Response<LinkResponse>

    @DELETE("/v1/users/key")
    suspend fun deleteUserKey(): Response<MessageResponse>


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


    @GET("/v1/categories")
    suspend fun getCategories(): Response<List<CategoryDto>>

    @POST("/v1/categories")
    suspend fun addCategory(@Body category: CategoryDto): Response<IdResponse>

    @PUT("/v1/categories/{category_id}")
    suspend fun updateCategory(@Path("category_id") categoryId: String, @Body category: CategoryDto): Response<MessageResponse>

    @DELETE("/v1/categories/{category_id}")
    suspend fun deleteCategory(@Path("category_id") categoryId: String): Response<MessageResponse>


    @GET("/v1/shopping-list")
    suspend fun getShoppingList(): Response<ShoppingListDto>

    @POST("/v1/shopping-list")
    suspend fun setShoppingList(@Body shoppingList: ShoppingListInputDto): Response<MessageResponse>

    @PUT("/v1/shopping-list")
    suspend fun addToShoppingList(@Body shoppingList: List<PurchaseDto>): Response<MessageResponse>

    @GET
    suspend fun getFile(@Url link: String) : Response<ResponseBody>
}

interface SessionApi {
    @POST("/v1/auth/refresh")
    suspend fun refreshSession(@Body elementModel: RefreshToken): Response<TokenResponse>
}