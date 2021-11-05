package com.cactusknights.chefbook.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cactusknights.chefbook.adapters.IngredientAdapter
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.FragmentRecipeIngredientsBinding
import com.cactusknights.chefbook.helpers.showToast
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.viewmodels.UuuserViewModel

class RecipeIngredientsFragment(var recipe: Recipe = Recipe()): Fragment(), IngredientAdapter.IngredientClickListener {

    private var ingredientsAdapter = IngredientAdapter(recipe.ingredients, this)
    private var addToShoppingList = arrayListOf<Int>()

    private var _binding: FragmentRecipeIngredientsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeIngredientsBinding.inflate(inflater, container, false)
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
                binding.rvIngredients.getChildAt(position).findViewById<AppCompatCheckBox>(R.id.checkbox_selected).isChecked = false
                addableIngredients.add(recipe.ingredients[position].item!!)
            }
            UuuserViewModel.addToShoppingList(addableIngredients)
            addToShoppingList = arrayListOf()
            binding.btnAddToShoplist.visibility = View.GONE
            activity?.showToast(resources.getString(R.string.added_to_shopping_list))
        }
    }

    override fun onIngredientClick(position: Int) {
        if (position in addToShoppingList) addToShoppingList.remove(position) else addToShoppingList.add(position)
        binding.btnAddToShoplist.visibility = if (addToShoppingList.isNotEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("recipe", recipe)
        super.onSaveInstanceState(outState)
    }
}