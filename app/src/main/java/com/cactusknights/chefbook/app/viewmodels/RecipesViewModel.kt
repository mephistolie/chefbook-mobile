package com.cactusknights.chefbook.app.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cactusknights.chefbook.app.models.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ListenerRegistration

class RecipesViewModel : ViewModel() {
    private var _recipes =  MutableLiveData<ArrayList<Recipe>>()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var recipesListener: ListenerRegistration

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToRecipes()
    }

    fun save() {

    }

    private fun listenToRecipes() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            recipesListener = firestore.collection("users").document(user.uid).collection("recipes").addSnapshotListener {
                    snapshot, e ->

                if (e != null) {
                    Log.w(TAG, "Listen failed", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val allRecipes = ArrayList<Recipe>()
                    val documents = snapshot.documents
                    documents.forEach {
                        val recipe = it.toObject(Recipe::class.java)
                        if (recipe != null) {
                            allRecipes.add(recipe)
                        }
                    }
                    _recipes.value = allRecipes
                }
            }
        }
    }

    internal var recipes: MutableLiveData<ArrayList<Recipe>>
        get() { return _recipes }
        set(value) { _recipes = value}
}