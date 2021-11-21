package com.cactusknights.chefbook.screens.recipeinput

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.screens.recipeinput.adapters.CookingEditAdapter
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.ActivityRecipeInputBinding
import com.cactusknights.chefbook.common.ConfirmDialog
import com.cactusknights.chefbook.legacy.helpers.showToast
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.screens.recipeinput.adapters.IngredientEditAdapter
import com.cactusknights.chefbook.screens.recipeinput.dialogs.CategoriesDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.http.HEAD
import java.util.*

@AndroidEntryPoint
class RecipeInputActivity: AppCompatActivity() {

    val viewModel: RecipeInputViewModel by viewModels()
    private lateinit var recipe : DecryptedRecipe

    private lateinit var ingredientsAdapter: IngredientEditAdapter
    private val ingredientTouchHelper = ItemTouchHelper(IngredientsDragCallback())

    private lateinit var cookingAdapter: CookingEditAdapter
    private val cookingTouchHelper = ItemTouchHelper(IngredientsDragCallback())

    private lateinit var binding: ActivityRecipeInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            viewModel.restoreState(savedInstanceState.get("state") as RecipeInputActivityState)
        } else {
            val originalRecipe = intent.extras?.get("recipe") as DecryptedRecipe?
            if (originalRecipe != null) viewModel.setRecipe(originalRecipe)
        }

        recipe = viewModel.state.value.recipe

        recipe.ingredients = viewModel.state.value.recipe.ingredients
        binding.rvIngredients.layoutManager = LinearLayoutManager(this)
        ingredientsAdapter = IngredientEditAdapter(recipe.ingredients)
        binding.rvIngredients.adapter = ingredientsAdapter
        ingredientTouchHelper.attachToRecyclerView(binding.rvIngredients)

        recipe.cooking = viewModel.state.value.recipe.cooking
        binding.rvSteps.layoutManager = LinearLayoutManager(this)
        cookingAdapter = CookingEditAdapter(recipe.cooking)
        binding.rvSteps.adapter = cookingAdapter
        cookingTouchHelper.attachToRecyclerView(binding.rvSteps)

        binding.btnCategories.setOnClickListener {
            CategoriesDialog()
                .show(supportFragmentManager, "Categories Dialog")
        }

        binding.btnAddIngredient.setOnClickListener {
            recipe.ingredients.add(MarkdownString(""))
            ingredientsAdapter.notifyItemInserted(recipe.ingredients.size-1)
        }
        binding.btnAddIngredientSection.setOnClickListener {
            recipe.ingredients.add(MarkdownString("", MarkdownTypes.HEADER))
            ingredientsAdapter.notifyItemInserted(recipe.ingredients.size-1)
        }
        binding.btnAddStep.setOnClickListener {
            recipe.cooking.add(MarkdownString())
            cookingAdapter.notifyItemInserted(recipe.cooking.size-1)
        }
        binding.btnAddCookingSection.setOnClickListener {
            recipe.cooking.add(MarkdownString("", MarkdownTypes.HEADER))
            cookingAdapter.notifyItemInserted(recipe.cooking.size-1)
        }

        binding.btnBack.setOnClickListener { onBackPressed() }
        binding.btnConfirm.setOnClickListener { commitRecipe() }

        binding.rgVisibility.check(binding.rbPrivate.id)
        binding.rgVisibility.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_private -> recipe.visibility = Visibility.PRIVATE
                R.id.rb_shared -> recipe.visibility = Visibility.SHARED
                R.id.rb_public -> recipe.visibility = Visibility.PUBLIC
            }
        }

        binding.inputName.doOnTextChanged { name, _, _, _ -> recipe.name = name.toString() }
        binding.inputCalories.doOnTextChanged { calories, _, _, _ -> recipe.calories = calories.toString().toIntOrNull()?:0 }
        binding.inputServings.doOnTextChanged { servings, _, _, _ -> recipe.servings = servings.toString().toIntOrNull()?:1 }
        binding.inputHours.doOnTextChanged { _, _, _, _ -> viewModel.setRecipeTime(binding.inputHours.text.toString(), binding.inputMinutes.text.toString()) }
        binding.inputMinutes.doOnTextChanged { _, _, _, _ -> viewModel.setRecipeTime(binding.inputHours.text.toString(), binding.inputMinutes.text.toString()) }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    if (state.isDone) {
                        setResult(RESULT_OK, Intent().putExtra("recipe", state.recipe))
                        finish()
                    }

                    binding.inputName.setText(state.recipe.name)
                    binding.inputServings.setText(if (state.recipe.servings != 0) state.recipe.servings.toString() else "")
                    binding.inputCalories.setText(if (state.recipe.calories != 0) state.recipe.calories.toString() else "")
                    val hours = state.recipe.time / 60
                    val minutes = state.recipe.time % 60
                    binding.inputHours.setText(if (hours != 0) hours.toString() else "")
                    binding.inputMinutes.setText(if (minutes != 0) minutes.toString() else "")
                    recipe = state.recipe
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("state", viewModel.state.value)
        super.onSaveInstanceState(outState)
    }

    private fun commitRecipe() {
        when {
            viewModel.state.value.recipe.name.isEmpty() -> { this.showToast(resources.getString(R.string.enter_name)) }
            viewModel.state.value.recipe.ingredients.isEmpty() -> { this.showToast(resources.getString(R.string.enter_ingredients)) }
            viewModel.state.value.recipe.cooking.isEmpty() -> { this.showToast(resources.getString(R.string.enter_step)) }
            else -> {
                viewModel.commitInput()
            }
        }
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
                    Collections.swap(recipe.ingredients, from, to)
                    ingredientsAdapter.notifyItemMoved(from, to)
                    true
                }
                cookingAdapter -> {
                    Collections.swap(recipe.cooking, from, to)
                    cookingAdapter.notifyItemMoved(from, to)
                    cookingAdapter.notifyItemRangeChanged(from, from+1, Any())
                    cookingAdapter.notifyItemRangeChanged(to, to+1, Any())
                    true
                } else -> false
            }
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { return }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            val topY = viewHolder.itemView.top + dY
            val bottomY = topY + viewHolder.itemView.height
            if (topY > 0 && bottomY < recyclerView.height) { super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive) }
        }
    }

    override fun onBackPressed() {
        ConfirmDialog { super.onBackPressed() }.show(supportFragmentManager, "Confirm")
    }
}