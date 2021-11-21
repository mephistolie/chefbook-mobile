package com.cactusknights.chefbook

import android.content.SharedPreferences
import android.util.Log
import com.cactusknights.chefbook.common.Constants.LOCAL_USERNAME
import com.cactusknights.chefbook.domain.ContentRepository
import com.cactusknights.chefbook.domain.LocalDataSource
import com.cactusknights.chefbook.domain.RemoteDataSource
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.models.User
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.Exception
import javax.inject.Inject

enum class DataSources {

    LOCAL, REMOTE;

    companion object {
        fun getDataSourceByString(input: String): DataSources {
            return if (input.lowercase() == "remote") REMOTE else LOCAL
        }
    }
}

class ChefBookRepository @Inject constructor(
    private val localSource: LocalDataSource,
    private val remoteSource: RemoteDataSource,
    private val sp: SharedPreferences
) : ContentRepository {

    var dataSource: DataSources = DataSources.REMOTE
    val user: MutableStateFlow<User?> = MutableStateFlow(User())

    init {
//        dataSource = DataSources.getDataSourceByString(sp.getString("", DataSources.LOCAL.toString()).orEmpty())
        dataSource = DataSources.REMOTE
        CoroutineScope(Dispatchers.IO).launch {
            getCurrentUser()
        }
    }

    private suspend fun getCurrentUser() {
        if (dataSource == DataSources.LOCAL) {
            user.emit(
                User(
                    name = sp.getString(LOCAL_USERNAME, "User").orEmpty(),
                    premium = null
                )
            )
        } else {
            try {
                user.emit(remoteSource.getUserInfo())
            } catch (e: Exception) {
                Log.e("Error", e.toString())
                user.emit(null)
            }
        }
    }

    override suspend fun listenToUser(): MutableStateFlow<User?> {
        return user
    }

    override suspend fun signUp(email: String, password: String) {
        return remoteSource.signUp(email, password)
    }

    override suspend fun signIn(email: String, password: String) {
        remoteSource.signIn(email, password)
        getCurrentUser()
    }

    override suspend fun signOut() {
        remoteSource.signOut()
    }

    override suspend fun listenToUserRecipes(): MutableStateFlow<ArrayList<Recipe>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipes(): ArrayList<Recipe> {
        return remoteSource.getRecipes()
    }

    override suspend fun addRecipe(recipe: Recipe) : Int {
        return remoteSource.addRecipe(recipe)
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        remoteSource.updateRecipe(recipe)
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        remoteSource.deleteRecipe(recipe)
    }

    override suspend fun setRecipeFavouriteStatus(recipe: Recipe) {
        remoteSource.setRecipeFavouriteStatus(recipe)
    }

    override suspend fun getCategories(): ArrayList<Category> {
        return remoteSource.getCategories()
    }

    override suspend fun addCategory(category: Category) : Int {
        return remoteSource.addCategory(category)
    }

    override suspend fun updateCategory(category: Category) {
        return remoteSource.updateCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
        return remoteSource.deleteCategory(category)
    }

    override suspend fun getShoppingList(): ArrayList<Selectable<String>> {
        return remoteSource.getShoppingList()
    }

    override suspend fun setShoppingList(shoppingList: ArrayList<Selectable<String>>) {
        return remoteSource.setShoppingList(shoppingList)
    }

    override suspend fun addToShoppingList(shoppingList: ArrayList<Selectable<String>>) {
        return remoteSource.addToShoppingList(shoppingList)
    }
}