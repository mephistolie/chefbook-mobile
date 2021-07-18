package com.cactusknights.chefbook.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.adapters.CookingAdapter
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.adapters.IngredientAdapter
import com.cactusknights.chefbook.models.Recipe

class RecipeCookingFragment(var recipe: Recipe = Recipe()): Fragment() {

    private lateinit var timeView: TextView

    private lateinit var cookingView: RecyclerView
    private var cookingAdapter = CookingAdapter(recipe.cooking)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_cooking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timeView = view.findViewById(R.id.time)
        timeView.text = recipe.time

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getSerializable("recipe") as Recipe
            cookingAdapter = CookingAdapter(recipe.cooking)
        }

        cookingView = view.findViewById(R.id.cooking_recycler_view)
        cookingView.layoutManager = LinearLayoutManager(context)
        cookingView.adapter = cookingAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("recipe", recipe)
        super.onSaveInstanceState(outState)
    }
}