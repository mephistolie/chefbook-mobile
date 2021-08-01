package com.cactusknights.chefbook.viewmodels

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.interfaces.AuthProvider
import com.cactusknights.chefbook.interfaces.ContentProvider
import com.cactusknights.chefbook.interfaces.ContentListener
import com.cactusknights.chefbook.repositories.FirebaseAuthRepository
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.repositories.FirebaseContentRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {

    private var authRepository: AuthProvider = FirebaseAuthRepository.instance
    private var contentRepository: ContentListener = FirebaseContentRepository.instance

    private lateinit var user: MutableStateFlow<User?>

    private lateinit var recipes: MutableStateFlow<ArrayList<Recipe>>
    private lateinit var categories: MutableStateFlow<ArrayList<String>>
    private lateinit var shoppingList: MutableStateFlow<ArrayList<String>>

    init {
        viewModelScope.launch {
            user = authRepository.listenTotUser()

            recipes = contentRepository.getRecipes()
            categories = contentRepository.getCategories()
            shoppingList = contentRepository.getShoppingList()
        }
    }

    fun listenToUser(): StateFlow<User?> { return user }

    fun getCurrentUser(): User? { return user.value }
    fun signup(email: String, password: String, callback: (isSignedUp: Boolean) -> Unit) { viewModelScope.launch {  authRepository.signupEmail(email, password, callback) } }
    fun logonEmail(email: String, password: String, callback: (isLoggedIn: Boolean) -> Unit) { viewModelScope.launch {  authRepository.logonEmail(email, password, callback) } }
    fun logonGoogle(idToken: String, callback: (isLoggedIn: Boolean) -> Unit) { viewModelScope.launch {  authRepository.logonGoogle(idToken, callback) } }
    fun restorePassword(email: String, callback: (isReset: Boolean) -> Unit) { viewModelScope.launch { authRepository.restorePassword(email, callback) } }
    fun logout() { viewModelScope.launch { authRepository.logout() } }

    fun buyPremium(donation_type: String, activity: AppCompatActivity) { viewModelScope.launch { authRepository.buyPremium(donation_type, activity) } }
    fun isPremium(): Boolean { return getCurrentUser()?.isPremium ?: false }


    fun startListeningToUpdates() {
        viewModelScope.launch {
            contentRepository.listenToRecipes()
            contentRepository.listenToShoppingList()
        }
    }

    fun listenToRecipes(): StateFlow<ArrayList<Recipe>> { return recipes }
    fun listenToCategories(): StateFlow<ArrayList<String>> { return categories }
    fun listenToShoppingList(): StateFlow<ArrayList<String>> { return shoppingList }

    fun stopListeningToUpdates() {
        viewModelScope.launch {
            contentRepository.stopListeningRecipes()
            contentRepository.stopListenShoppingList()
        }
    }

    fun getRecipes(): ArrayList<Recipe> { return recipes.value }
    fun getCategories(): ArrayList<String> { return categories.value }
    fun getShoppingList(): ArrayList<String> { return shoppingList.value }

    fun getRecipesCount(): Int { return recipes.value.size }
    fun setShoppingList(value: ArrayList<String>) { viewModelScope.launch { contentRepository.setShoppingList(value) } }

    companion object {

        private val recipeEditor: ContentProvider = FirebaseContentRepository

        fun addRecipe(recipe: Recipe, callback: (isAdded: Boolean) -> Unit) { GlobalScope.launch { recipeEditor.addRecipe(recipe, callback) } }
        fun updateRecipe(recipe: Recipe, callback: (isUpdated: Boolean) -> Unit) { GlobalScope.launch { recipeEditor.updateRecipe(recipe, callback) } }
        fun deleteRecipe(recipe: Recipe, callback: (isDeleted: Boolean) -> Unit) { GlobalScope.launch { recipeEditor.deleteRecipe(recipe, callback) } }
        fun setRecipeFavoriteStatus(recipe: Recipe) { GlobalScope.launch { recipeEditor.setRecipeFavoriteStatus(recipe) } }

        fun addToShoppingList(items: ArrayList<String>) { GlobalScope.launch { recipeEditor.addToShoppingList(items) } }
    }
}