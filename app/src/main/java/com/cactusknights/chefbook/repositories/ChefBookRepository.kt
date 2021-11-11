package com.cactusknights.chefbook

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.cactusknights.chefbook.common.AuthException
import com.cactusknights.chefbook.common.Constants.LOCAL_USERNAME
import com.cactusknights.chefbook.domain.AuthProvider
import com.cactusknights.chefbook.domain.RecipesProvider
import com.cactusknights.chefbook.models.BaseRecipe
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.repositories.local.LocalDataSource
import com.cactusknights.chefbook.repositories.remote.ChefBookRecipesRepository
import com.cactusknights.chefbook.repositories.remote.RemoteDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

enum class DataSources {

    LOCAL, REMOTE;

    companion object {
        fun getDataSourceByString(input: String) : DataSources {
            return if (input.lowercase() == "remote") REMOTE else LOCAL
        }
    }
}

class ChefBookRepository @Inject constructor(
    private val localSource: LocalDataSource,
    private val remoteSource: RemoteDataSource,
    private val recipesSource: ChefBookRecipesRepository,
    private val sp: SharedPreferences
) : AuthProvider, RecipesProvider {

    var dataSource : DataSources = DataSources.REMOTE
    val user : MutableStateFlow<User?> = MutableStateFlow(User())

    init {
//        dataSource = DataSources.getDataSourceByString(sp.getString("", DataSources.LOCAL.toString()).orEmpty())
        dataSource = DataSources.REMOTE
        CoroutineScope(Dispatchers.IO).launch {
            getCurrentUser()
        }
    }


    suspend fun getCurrentUser() {
        if (dataSource == DataSources.LOCAL) {
            user.emit(User(
                name = sp.getString(LOCAL_USERNAME, "User").orEmpty(),
                premium = null
            ))
        } else {
            loop@ for(tryNumber in 0..1) {
                try {
                    user.emit(remoteSource.getUserInfo())
                    break
                } catch (e: AuthException) {
                    if (!refreshSession()) break@loop
                }
            }
        }
    }

    private suspend fun refreshSession(): Boolean {
        try {
            remoteSource.refreshSession()
            return true
        } catch (e: Exception) {
            user.emit(null)
            return false
        }
    }

    override suspend fun listenAuthState(): MutableStateFlow<User?> {
        return user
    }

    override suspend fun isLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun isPremium(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(email: String, password: String) {
        return remoteSource.signUp(email, password)
    }

    override suspend fun signInEmail(email: String, password: String) {
        remoteSource.signInEmail(email, password)
        getCurrentUser()
    }

    override suspend fun signInGoogle(idToken: String, callback: (isLoggedIn: Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun restorePassword(email: String, callback: (isReset: Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun buyPremium(donation_type: String, activity: AppCompatActivity) {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipes(): List<BaseRecipe> {
        return recipesSource.getRecipes()
    }

    override suspend fun addRecipe(recipe: BaseRecipe) {
        TODO("Not yet implemented")
    }

    override suspend fun updateRecipe(recipe: BaseRecipe) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecipe(recipeId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun setRecipeFavoriteStatus(recipe: BaseRecipe) {
        TODO("Not yet implemented")
    }

    override suspend fun addToShoppingList(items: ArrayList<String>) {
        TODO("Not yet implemented")
    }
}