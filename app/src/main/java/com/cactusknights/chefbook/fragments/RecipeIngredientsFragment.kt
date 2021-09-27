package com.cactusknights.chefbook.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cactusknights.chefbook.adapters.IngredientAdapter
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.FragmentRecipeIngredientsBinding
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.viewmodels.UserViewModel

class RecipeIngredientsFragment(var recipe: Recipe = Recipe()): Fragment(), IngredientAdapter.IngredientClickListener {

    private var ingredientsAdapter = IngredientAdapter(recipe.ingredients, this)
    private var addToShoppingList = arrayListOf<Int>()

    private lateinit var binding: FragmentRecipeIngredientsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeIngredientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getSerializable("recipe") as Recipe
            ingredientsAdapter = IngredientAdapter(recipe.ingredients, this)
        }

        if (recipe.calories == 0) {
            binding.textDivider.visibility = View.GONE
            binding.textCalories.visibility = View.GONE
        }

        binding.textServings.text = resources.getString(R.string.servings_placeholder, recipe.servings)
        binding.textCalories.text = resources.getString(R.string.calories_placeholder, recipe.calories)

        binding.rvIngredients.layoutManager = LinearLayoutManager(context)
        binding.rvIngredients.adapter = ingredientsAdapter

        binding.btnAddToShoplist.setOnClickListener {
            val addableIngredients = arrayListOf<String>()
            for (position in  addToShoppingList) {
                binding.rvIngredients.getChildAt(position).findViewById<AppCompatCheckBox>(R.id.checkbox_add_to_shopping_list).isChecked = false
                addableIngredients.add(recipe.ingredients[position].item!!)
            }
            UserViewModel.addToShoppingList(addableIngredients)
            addToShoppingList = arrayListOf()
            binding.btnAddToShoplist.visibility = View.GONE
            Toast.makeText(activity, resources.getString(R.string.added_to_shopping_list), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onIngredientClick(position: Int) {
        if (position in addToShoppingList) addToShoppingList.remove(position) else addToShoppingList.add(position)
        binding.btnAddToShoplist.visibility = if (addToShoppingList.isNotEmpty()) View.VISIBLE else View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("recipe", recipe)
        super.onSaveInstanceState(outState)
    }
}