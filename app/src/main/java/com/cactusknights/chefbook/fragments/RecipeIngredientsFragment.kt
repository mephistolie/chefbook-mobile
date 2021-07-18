package com.cactusknights.chefbook.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatCallback
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.adapters.IngredientAdapter
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.activities.MainActivity
import com.cactusknights.chefbook.models.Ingredient
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.viewmodels.UserViewModel
import org.w3c.dom.Text

class RecipeIngredientsFragment(var recipe: Recipe = Recipe()): Fragment(), IngredientAdapter.IngredientClickListener {

    private lateinit var servingsView: TextView
    private lateinit var caloriesView: TextView
    private lateinit var divider: TextView

    private lateinit var ingredientsView: RecyclerView
    private lateinit var addToShoppingListBtn: Button

    private var ingredientsAdapter = IngredientAdapter(recipe.ingredients, this)
    private var addToShoppingList = arrayListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_ingredients, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        servingsView = view.findViewById(R.id.servings)
        caloriesView = view.findViewById(R.id.calories)
        divider = view.findViewById(R.id.divider)

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getSerializable("recipe") as Recipe
            ingredientsAdapter = IngredientAdapter(recipe.ingredients, this)
        }

        if (recipe.calories == 0) {
            divider.visibility = View.GONE
            caloriesView.visibility = View.GONE
        }

        servingsView.text = resources.getString(R.string.servings_placeholder, recipe.servings)
        caloriesView.text = resources.getString(R.string.calories_placeholder, recipe.calories)

        ingredientsView = view.findViewById(R.id.ingredients_recycler_view)
        ingredientsView.layoutManager = LinearLayoutManager(context)
        ingredientsView.adapter = ingredientsAdapter

        addToShoppingListBtn = view.findViewById(R.id.add_to_shoplist)
        addToShoppingListBtn.setOnClickListener {
            val addableIngredients = arrayListOf<String>()
            for (position in addToShoppingList) {
                ingredientsView.getChildAt(position).findViewById<AppCompatCheckBox>(R.id.add_to_shoplist).isChecked = false
                addableIngredients.add(recipe.ingredients[position].name)
            }
            UserViewModel.addToShoppingList(addableIngredients)
            addToShoppingList = arrayListOf()
            addToShoppingListBtn.visibility = View.GONE
            Toast.makeText(activity, resources.getString(R.string.added_to_shopping_list), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onIngredientClick(position: Int) {
        if (position in addToShoppingList) addToShoppingList.remove(position) else addToShoppingList.add(position)
        addToShoppingListBtn.visibility = if (addToShoppingList.isNotEmpty()) View.VISIBLE else View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("recipe", recipe)
        super.onSaveInstanceState(outState)
    }
}