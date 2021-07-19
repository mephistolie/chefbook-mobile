package com.cactusknights.chefbook.viewmodels

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cactusknights.chefbook.interfaces.AuthProvider
import com.cactusknights.chefbook.interfaces.ContentProvider
import com.cactusknights.chefbook.interfaces.ContentListener
import com.cactusknights.chefbook.repositories.FirebaseAuthRepository
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.repositories.FirebaseContentRepository


class UserViewModel: ViewModel() {

    private var authRepository: AuthProvider = FirebaseAuthRepository()
    private var contentRepository: ContentListener = FirebaseContentRepository()
    private var user = authRepository.getCurrentUser()

    val recipes = contentRepository.getRecipes()
    val categories = contentRepository.getCategories()
    var shoppingList = contentRepository.getShoppingList()
        set(value) = contentRepository.setShoppingList(value)

    fun signup(email: String, password: String, callback: (isSignedUp: Boolean) -> Unit) { authRepository.signupEmail(email, password, callback) }
    fun logonEmail(email: String, password: String, callback: (isLoggedIn: Boolean) -> Unit) { authRepository.logonEmail(email, password, callback) }
    fun logonGoogle(idToken: String, callback: (isLoggedIn: Boolean) -> Unit) { authRepository.logonGoogle(idToken, callback) }
    fun restorePassword(email: String, callback: (isReset: Boolean) -> Unit) { authRepository.restorePassword(email, callback) }
    fun getCurrentUser(): MutableLiveData<User> { return user }
    fun logout() { authRepository.logout() }
    fun buyPremium(donation_type: String, activity: Activity) { return authRepository.buyPremium(donation_type, activity) }
    fun isPremium(): Boolean { return authRepository.isPremium() }
    fun getRecipesCount(): Int { return contentRepository.getRecipesCount() }

    fun listenForUpdates() {
        contentRepository.listenToRecipes()
        contentRepository.listenToShoppingList()
    }

    fun stopListening() {
        contentRepository.stopListeningRecipes()
        contentRepository.stopListenShoppingList()
    }

    fun getCurrentCategories(): ArrayList<String> {
        val currentCategoriesSet = categories.value
        val currentCategories = arrayListOf<String>()
        if (currentCategoriesSet != null) {
            currentCategories.addAll(currentCategoriesSet)
        }
        return currentCategories
    }

    companion object {

        private val recipeEditor: ContentProvider = FirebaseContentRepository

        fun addRecipe(recipe: Recipe, callback: (isAdded: Boolean) -> Unit) { recipeEditor.addRecipe(recipe, callback) }
        fun updateRecipe(recipe: Recipe, callback: (isUpdated: Boolean) -> Unit) { recipeEditor.updateRecipe(recipe, callback) }
        fun deleteRecipe(recipe: Recipe, callback: (isDeleted: Boolean) -> Unit) { recipeEditor.deleteRecipe(recipe, callback) }
        fun setRecipeFavoriteStatus(recipe: Recipe) { recipeEditor.setRecipeFavoriteStatus(recipe) }

        fun addToShoppingList(items: ArrayList<String>) { recipeEditor.addToShoppingList(items) }
    }
}