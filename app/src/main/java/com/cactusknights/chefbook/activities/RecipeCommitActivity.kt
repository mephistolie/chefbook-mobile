package com.cactusknights.chefbook.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.adapters.CookingEditAdapter
import com.cactusknights.chefbook.adapters.IngredientEditAdapter
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.ActivityCommitRecipeBinding
import com.cactusknights.chefbook.dialogs.CategoriesDialog
import com.cactusknights.chefbook.dialogs.ConfirmDialog
import com.cactusknights.chefbook.models.Ingredient
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.viewmodels.UserViewModel
import java.util.*
import kotlin.collections.ArrayList


class RecipeCommitActivity: AppCompatActivity() {

    private var originalRecipe: Recipe? = null
    private var allCategories = arrayListOf<String>()
    private var categories: ArrayList<String> = originalRecipe?.categories ?: arrayListOf()

    private var ingredients: ArrayList<Ingredient> = arrayListOf(Ingredient(""))
    private lateinit var ingredientsAdapter: IngredientEditAdapter
    private val ingredientTouchHelper = ItemTouchHelper(IngredientsDragCallback())

    private var steps: ArrayList<String> = arrayListOf("")
    private lateinit var cookingAdapter: CookingEditAdapter
    private val cookingTouchHelper = ItemTouchHelper(IngredientsDragCallback())

    private lateinit var binding: ActivityCommitRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommitRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            originalRecipe = savedInstanceState.getSerializable("originalRecipe") as Recipe
            allCategories = savedInstanceState.getStringArrayList("allCategories") as ArrayList<String>
            val outRecipe = savedInstanceState.getSerializable("recipe") as Recipe
            categories = outRecipe.categories

            if (originalRecipe != null) binding.textSection.text = resources.getString(R.string.edit_recipe)
            binding.inputName.setText(outRecipe.name)
            binding.inputServings.setText(outRecipe.servings.toString())
            binding.inputCalories.setText(outRecipe.calories.toString())
            binding.inputTime.setText(outRecipe.time)

            ingredients = outRecipe.ingredients
            steps = outRecipe.cooking
        } else {
            originalRecipe = intent.extras?.get("targetRecipe") as Recipe?
            if (originalRecipe != null) {

                binding.textSection.text = resources.getString(R.string.edit_recipe)
                binding.inputName.setText(originalRecipe!!.name)
                binding.inputServings.setText(originalRecipe!!.servings.toString())
                binding.inputCalories.setText(originalRecipe!!.calories.toString())
                binding.inputTime.setText(originalRecipe!!.time)

                ingredients = originalRecipe!!.ingredients
                steps = originalRecipe!!.cooking
                categories = originalRecipe!!.categories
            }
            if (intent.extras?.get("allCategories") != null)
                allCategories.addAll(intent.extras?.getStringArrayList("allCategories") as ArrayList<String>)
        }

        binding.rvIngredients.layoutManager = LinearLayoutManager(this)
        ingredientsAdapter = IngredientEditAdapter(ingredients)
        binding.rvIngredients.adapter = ingredientsAdapter
        ingredientTouchHelper.attachToRecyclerView(binding.rvIngredients)

        binding.rvSteps.layoutManager = LinearLayoutManager(this)
        cookingAdapter = CookingEditAdapter(steps)
        binding.rvSteps.adapter = cookingAdapter
        cookingTouchHelper.attachToRecyclerView(binding.rvSteps)

        binding.btnAddIngredient.setOnClickListener {
            ingredients.add(Ingredient(""))
            ingredientsAdapter.notifyItemInserted(ingredients.size-1)
        }
        binding.btnAddSection.setOnClickListener {
            ingredients.add(Ingredient("", true))
            ingredientsAdapter.notifyItemInserted(ingredients.size-1)
        }
        binding.btnAddStep.setOnClickListener {
            steps.add("")
            cookingAdapter.notifyItemInserted(steps.size-1)
        }

        binding.btnCategories.setOnClickListener {
            CategoriesDialog(categories, allCategories, ::onCommitCategoriesCallback)
                .show(supportFragmentManager, "Categories Dialog")
        }

        binding.btnBack.setOnClickListener { onBackPressed() }
        binding.btnConfirm.setOnClickListener { commitRecipe() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("recipe", getRecipe())
        outState.putSerializable("originalRecipe", originalRecipe)
        outState.putStringArrayList("allCategories", allCategories)
        super.onSaveInstanceState(outState)
    }

    private fun commitRecipe() {
        val confirmedRecipe = getRecipe()
        when {
            confirmedRecipe.name.isEmpty() -> { Toast.makeText(this, resources.getString(R.string.enter_name), Toast.LENGTH_SHORT).show() }
            confirmedRecipe.ingredients.isEmpty() -> { Toast.makeText(this, resources.getString(R.string.enter_ingredients), Toast.LENGTH_SHORT).show() }
            confirmedRecipe.cooking.isEmpty() -> { Toast.makeText(this, resources.getString(R.string.enter_step), Toast.LENGTH_SHORT).show() }
            else -> {
                if (confirmedRecipe.id.isEmpty()) UserViewModel.addRecipe(confirmedRecipe, ::onCommitRecipeCallback)
                else UserViewModel.updateRecipe(confirmedRecipe, ::onCommitRecipeCallback)
                setResult(RESULT_OK, Intent().putExtra("recipe", confirmedRecipe))
                finish()
            }
        }
    }

    private fun onCommitRecipeCallback(isAdded: Boolean) {
        if (isAdded) { Toast.makeText(applicationContext, if (originalRecipe == null) resources.getString(R.string.recipe_added)
            else resources.getString(R.string.recipe_updated), Toast.LENGTH_SHORT).show() }
        else Toast.makeText(applicationContext, if (originalRecipe == null) resources.getString(R.string.failed_add)
            else resources.getString(R.string.failed_update), Toast.LENGTH_SHORT).show()
    }

    private fun getRecipe(): Recipe {

        val nameText = binding.inputName.text.toString()
        val servingsText = binding.inputServings.text.toString()
        val timeText = binding.inputTime.text.toString()
        val caloriesText = binding.inputCalories.text.toString()

        val notEmptyIngredients = ingredients.filter { it.name != "" } as ArrayList<Ingredient>
        val notEmptySteps = steps.filter { it != "" } as ArrayList<String>

        return Recipe(
            id = originalRecipe?.id ?: "", name = nameText,
            isFavourite = originalRecipe?.isFavourite ?: false,
            creationDate = originalRecipe?.creationDate ?: Date(),
            categories = categories,

            servings = if (servingsText.isNotEmpty()) servingsText.toInt() else 1,
            time = if (timeText.isNotEmpty()) timeText else "15 min",
            calories = if (caloriesText.isNotEmpty()) caloriesText.toInt() else 0,

            ingredients = notEmptyIngredients, cooking = notEmptySteps
        )
    }

    inner class IngredientsDragCallback: ItemTouchHelper.Callback() {

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = 0
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {

            val from = viewHolder.adapterPosition
            val to = target.adapterPosition
            return when (recyclerView.adapter) {
                ingredientsAdapter -> {
                    Collections.swap(ingredients, from, to)
                    ingredientsAdapter.notifyItemMoved(from, to)
                    true
                }
                cookingAdapter -> {
                    Collections.swap(steps, from, to)
                    cookingAdapter.notifyItemMoved(from, to)
                    cookingAdapter.notifyItemRangeChanged(0, steps.size, Any())
                    true
                } else -> false
            }
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { return }
    }

    private fun onCommitCategoriesCallback (newCategories: ArrayList<String>, allCategories: ArrayList<String>) {
        this.categories = newCategories
        this.allCategories = allCategories

    }

    override fun onBackPressed() {
        ConfirmDialog { finish() }.show(supportFragmentManager, "Confirm")
    }
}