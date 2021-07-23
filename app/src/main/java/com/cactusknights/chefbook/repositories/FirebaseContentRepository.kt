package com.cactusknights.chefbook.repositories

import com.cactusknights.chefbook.helpers.Utils
import com.cactusknights.chefbook.interfaces.ContentProvider
import com.cactusknights.chefbook.interfaces.ContentListener
import com.cactusknights.chefbook.models.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import kotlin.collections.ArrayList

class FirebaseContentRepository: ContentListener {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var recipesListener: ListenerRegistration
    private lateinit var shoppingListListener: ListenerRegistration

    private var recipes: MutableStateFlow<ArrayList<Recipe>> = MutableStateFlow(arrayListOf())
    private var categories: MutableStateFlow<ArrayList<String>> = MutableStateFlow(arrayListOf())
    private var shoppingList: MutableStateFlow<ArrayList<String>> = MutableStateFlow(arrayListOf())


    override suspend fun getRecipes(): MutableStateFlow<ArrayList<Recipe>> { return recipes }
    override suspend fun getCategories(): MutableStateFlow<ArrayList<String>> { return categories }
    override suspend fun getShoppingList(): MutableStateFlow<ArrayList<String>> { return shoppingList }

    override suspend fun getRecipesCount(): Int {
        val currentRecipes = recipes.value
        return currentRecipes.size
    }

    override suspend fun listenToRecipes() {
        val user = auth.currentUser
        if (user != null) {
            recipesListener = firestore.collection("users").document(user.uid)
                .collection("recipes").addSnapshotListener { snapshot, e ->

                if (e != null) { return@addSnapshotListener }

                if (snapshot != null) {
                    val allRecipes = arrayListOf<Recipe>()
                    val allCategories = arrayListOf<String>()
                    val documents = snapshot.documents
                    documents.forEach { document ->
                        val recipe = document.toObject(Recipe::class.java)
                        recipe?.id = document.id
                        if (recipe != null) {
                            allRecipes.add(recipe)
                            allCategories.addAll(recipe.categories.filter { it !in allCategories })
                        }
                    }
                    recipes.value = allRecipes
                    categories.value = allCategories
                }
            }
        }
    }

    override suspend fun listenToShoppingList() {
        val user = auth.currentUser
        if (user != null) {
            shoppingListListener = firestore.collection("users")
                .document(user.uid).addSnapshotListener { snapshot, e ->

                    if (e != null) { return@addSnapshotListener }

                    if (snapshot != null) {
                        val newShoppingList = arrayListOf<String>()
                        if (snapshot["shoppingList"] != null)
                            newShoppingList.addAll((snapshot["shoppingList"] as Collection<*>).map { it.toString() })
                        if (!Utils.areListsSame(shoppingList.value, newShoppingList))
                            shoppingList.value = newShoppingList
                    }
                }
        }
    }

    override suspend fun stopListeningRecipes() {
        if (this::recipesListener.isInitialized) {recipesListener.remove()}
    }

    override suspend fun stopListenShoppingList() {
        if (this::shoppingListListener.isInitialized) {shoppingListListener.remove()}
    }

    override suspend fun setShoppingList(shoppingList: ArrayList<String>) {
        val user = auth.currentUser
        if (user != null)
            firestore.collection("users")
                .document(user.uid).update(mapOf("shoppingList" to shoppingList))
    }

    companion object: ContentProvider {

        val instance = FirebaseContentRepository()

        private var auth = FirebaseAuth.getInstance()

        override suspend fun addRecipe(recipe: Recipe, callback: (isAdded: Boolean) -> Unit) {
            val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
            val user = auth.currentUser
            if (user != null) {
                firestore.collection("users").document(user.uid).collection("recipes").document().set(recipe)
                    .addOnSuccessListener { callback(true) }
                    .addOnFailureListener { callback(false) }
            }
        }

        override suspend fun updateRecipe(recipe: Recipe, callback: (isUpdated: Boolean) -> Unit) {
            val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
            val user = auth.currentUser
            if (user != null) {
                firestore.collection("users").document(user.uid).collection("recipes").document(recipe.id).set(recipe)
                    .addOnSuccessListener { callback(true) }
                    .addOnFailureListener { callback(false) }
            }
        }

        override suspend fun deleteRecipe(recipe: Recipe, callback: (isDeleted: Boolean) -> Unit) {
            val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
            val user = auth.currentUser
            if (user != null) {
                firestore.collection("users").document(user.uid).collection("recipes").document(recipe.id).delete()
                    .addOnSuccessListener { callback(true) }
                    .addOnFailureListener { callback(false) }
            }
        }

        override suspend fun setRecipeFavoriteStatus(recipe: Recipe) {
            val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
            val user = auth.currentUser
            if (user != null)
                firestore.collection("users")
                    .document(user.uid).collection("recipes").document(recipe.id).update(mapOf("favourite" to recipe.isFavourite))
        }

        override suspend fun addToShoppingList(items: ArrayList<String>) {
            val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                firestore.collection("users")
                    .document(user.uid).get().addOnSuccessListener { document ->
                        val newShoppingList = arrayListOf<String>()
                        newShoppingList.addAll((document["shoppingList"] as Collection<*>).map { it.toString() })
                        newShoppingList.addAll(items)
                        document.reference.update(mapOf("shoppingList" to newShoppingList))
                }
            }
        }
    }
}