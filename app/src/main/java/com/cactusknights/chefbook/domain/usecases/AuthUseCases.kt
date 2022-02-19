package com.cactusknights.chefbook.domain.usecases

import com.cactusknights.chefbook.common.usecases.Result
import com.cactusknights.chefbook.core.room.ChefBookDatabase
import com.cactusknights.chefbook.domain.AuthRepo
import com.cactusknights.chefbook.domain.RecipeBookSyncRepo
import com.cactusknights.chefbook.domain.ProfileRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class AuthUseCases @Inject constructor(
    private val authRepo: AuthRepo,
    private val profileRepo: ProfileRepo,
    private val recipeBookRepo: RecipeBookSyncRepo,
) {

    suspend fun signUp(email: String, password: String): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            authRepo.signUp(email, password)
            emit(Result.Success(null))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun signIn(email: String, password: String): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            val signedIn = authRepo.signIn(email, password)
            if (signedIn) CoroutineScope(Dispatchers.IO).launch { processDataSourceChanging() } else throw IOException()
            emit(Result.Success(null))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun signInLocally(): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            authRepo.signInLocally()
            emit(Result.Success(null))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    suspend fun signOut(): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            authRepo.signOut()
            processDataSourceChanging()
            emit(Result.Success(null))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    private fun processDataSourceChanging() {
        CoroutineScope(Dispatchers.IO).launch {
            profileRepo.getProfileInfo()
            recipeBookRepo.syncRecipeBook()
        }
    }
}