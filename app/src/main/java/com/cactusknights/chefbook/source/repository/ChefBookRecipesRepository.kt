package com.cactusknights.chefbook.source.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import com.cactusknights.chefbook.common.Constants.ACCESS_TOKEN
import com.cactusknights.chefbook.common.Constants.REFRESH_TOKEN
import com.cactusknights.chefbook.dialogs.GratitudeDialog
import com.cactusknights.chefbook.domain.AuthProvider
import com.cactusknights.chefbook.domain.RecipesProvider
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.models.retrofit.AuthData
import com.cactusknights.chefbook.models.retrofit.RefreshToken
import com.cactusknights.chefbook.models.retrofit.TokenResponse
import com.cactusknights.chefbook.source.remote.ChefBookApi
import com.cactusknights.chefbook.source.remote.dto.toRecipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class ChefBookRecipesRepository @Inject constructor(
    private val api: ChefBookApi,
    private val preferences: SharedPreferences
) : RecipesProvider {
    override suspend fun getRecipes(): List<Recipe> {
        val response = api.getRecipes()
        Log.e("RESPONSE", response.body().toString())
        val recipes = response.body()
        if (recipes != null) {
            return recipes.map { it.toRecipe() }
        } else throw IOException()
    }

    override suspend fun addRecipe(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecipe(recipeId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun setRecipeFavoriteStatus(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override suspend fun addToShoppingList(items: ArrayList<String>) {
        TODO("Not yet implemented")
    }
}