package com.cactusknights.chefbook.screens.common.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.common.usecases.Result
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.domain.usecases.EncryptionUseCases
import com.cactusknights.chefbook.domain.usecases.RecipeEncryptionUseCases
import com.cactusknights.chefbook.domain.usecases.RecipesUseCases
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.screens.common.recipes.models.RecipesEvent
import com.cactusknights.chefbook.screens.common.recipes.models.RecipesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.crypto.SecretKey
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val recipeUseCases: RecipesUseCases,
    private val encryptionUseCases: EncryptionUseCases,
    private val recipeEncryptionUseCases: RecipeEncryptionUseCases
) : ViewModel(), EventHandler<RecipesEvent> {

    private val _recipesState: MutableStateFlow<RecipesState> = MutableStateFlow(RecipesState.Loading)
    val recipesState: StateFlow<RecipesState> = _recipesState.asStateFlow()

    private var recipes : List<RecipeInfo>? = null
    private var displayedRecipes: List<RecipeInfo> = listOf()

    private var storageUnlocked : Boolean = false

    private var keys : MutableMap<String, SecretKey> = mutableMapOf()

    private var recipeListeningJob : Job? = null

    init {
        viewModelScope.launch {
            listenToRecipes()
            launch {
                encryptionUseCases.listenToUnlockedState().collect {
                    if (it != storageUnlocked) {
                        storageUnlocked = it
                        processData(recipes)
                    }
                }
            }
        }
    }

    override fun obtainEvent(event: RecipesEvent) {
        viewModelScope.launch {
            when (event) {
                is RecipesEvent.ListenRecipes -> listenToRecipes()
                is RecipesEvent.SearchRecipe -> searchRecipe(event.query)
            }
        }
    }

    private fun listenToRecipes() {
        recipeListeningJob?.cancel()
        recipeListeningJob = viewModelScope.launch { recipeUseCases.listenToRecipes().collect { processData(it) } }
    }

    private suspend fun processData(data: List<RecipeInfo>?) {
        if (data != null) {
            recipes = data
            displayedRecipes = recipes!!.filter { !it.isEncrypted }
            if (storageUnlocked) {
                val encryptedRecipes = recipes!!.filter { it.isEncrypted }
                val decryptedRecipes = arrayListOf<RecipeInfo>()
                decryptedRecipes.addAll(displayedRecipes)
                encryptedRecipes.forEach { recipe ->
                    try {
                        recipeEncryptionUseCases.getRecipeKey(recipe).collect { result ->
                            if (result is Result.Success) {
                                decryptedRecipes.add(recipe.decrypt { data -> recipeEncryptionUseCases.decryptRecipeData(data, result.data!!) })
                                val preview = recipe.preview
                                if (preview != null) {
                                    keys[preview] = result.data!!
                                }
                            }
                        }
                    } catch (e: Exception) {}
                }
                displayedRecipes = decryptedRecipes
            }
            _recipesState.emit(RecipesState.RecipesUpdated(displayedRecipes, keys))
        }
    }

    private suspend fun searchRecipe(query: String) {
        if (query.isNotEmpty()) _recipesState.emit(RecipesState.SearchResult(displayedRecipes.filter { it.name.lowercase().contains(query.lowercase()) }, keys))
        else _recipesState.emit(RecipesState.RecipesUpdated(displayedRecipes, keys))
    }
}