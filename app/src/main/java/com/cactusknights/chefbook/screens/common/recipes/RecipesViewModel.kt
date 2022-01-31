package com.cactusknights.chefbook.screens.common.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.common.usecases.Result
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.domain.usecases.EncryptionUseCases
import com.cactusknights.chefbook.domain.usecases.RecipesUseCases
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.EncryptedRecipe
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.decrypt
import com.cactusknights.chefbook.screens.common.recipes.models.RecipesEvent
import com.cactusknights.chefbook.screens.common.recipes.models.RecipesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.crypto.SecretKey
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val recipeUseCases: RecipesUseCases,
    private val encryptionUseCases: EncryptionUseCases
) : ViewModel(), EventHandler<RecipesEvent> {

    private val _recipesState: MutableStateFlow<RecipesState> = MutableStateFlow(RecipesState.Loading)
    val recipesState: StateFlow<RecipesState> = _recipesState.asStateFlow()

    private var recipes : List<Recipe>? = null
    private var displayedRecipes: List<DecryptedRecipe> = listOf()

    private var storageUnlocked : StateFlow<Boolean> = MutableStateFlow(false).asStateFlow()

    private var keys : MutableMap<String, SecretKey> = mutableMapOf()

    fun listenToUpdates() {
        viewModelScope.launch {
            launch { recipeUseCases.listenToRecipes().collect {  processData(it) } }
            launch {
                storageUnlocked = encryptionUseCases.listenToUnlockedState()
                storageUnlocked.collect { processData(recipes) }
            }
        }
    }

    override fun obtainEvent(event: RecipesEvent) {
        viewModelScope.launch {
            when (event) {
                is RecipesEvent.SearchRecipe -> searchRecipe(event.query)
            }
        }
    }

    private suspend fun processData(data: List<Recipe>?) {
        if (data != null) {
            recipes = data
            displayedRecipes = recipes!!.filterIsInstance(DecryptedRecipe::class.java)
            if (storageUnlocked.value) {
                val encryptedRecipes = recipes!!.filterIsInstance(EncryptedRecipe::class.java)
                val decryptedRecipes = arrayListOf<DecryptedRecipe>()
                decryptedRecipes.addAll(displayedRecipes)
                encryptedRecipes.forEach { recipe ->
                    try {
                        encryptionUseCases.getRecipeKey(recipe).collect { result ->
                            if (result is Result.Success) {
                                decryptedRecipes.add(recipe.decrypt { data -> encryptionUseCases.decryptRecipeData(data, result.data!!) })
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