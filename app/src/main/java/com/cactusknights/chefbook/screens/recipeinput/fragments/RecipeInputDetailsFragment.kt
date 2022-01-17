package com.cactusknights.chefbook.screens.recipeinput.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.databinding.FragmentRecipeInputDetailsBinding
import com.cactusknights.chefbook.models.MarkdownString
import com.cactusknights.chefbook.models.MarkdownTypes
import com.cactusknights.chefbook.screens.recipeinput.RecipeInputViewModel
import com.cactusknights.chefbook.screens.recipeinput.adapters.CookingInputAdapter
import com.cactusknights.chefbook.screens.recipeinput.adapters.IngredientInputAdapter
import com.cactusknights.chefbook.screens.recipeinput.models.RecipeInput
import com.cactusknights.chefbook.screens.recipeinput.models.RecipeInputState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class RecipeInputDetailsFragment : Fragment() {

    private val viewModel by activityViewModels<RecipeInputViewModel>()

    private lateinit var ingredientsAdapter: IngredientInputAdapter
    private lateinit var cookingAdapter: CookingInputAdapter

    private var _binding: FragmentRecipeInputDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeInputDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipeInputState.collect { render(it) }
            }
        }
    }

    private fun render(state: RecipeInputState) {
        if (state is RecipeInputState.NewRecipe || state is RecipeInputState.EditRecipe) {
            val recipe = if (state is RecipeInputState.NewRecipe) state.recipeInput else (state as RecipeInputState.EditRecipe).recipeInput

            binding.rvIngredients.layoutManager = LinearLayoutManager(requireContext())
            ingredientsAdapter = IngredientInputAdapter(recipe.ingredients)
            binding.rvIngredients.adapter = ingredientsAdapter
            val ingredientTouchHelper = ItemTouchHelper(ListInputDragCallback(recipe))
            ingredientTouchHelper.attachToRecyclerView(binding.rvIngredients)

            binding.rvSteps.layoutManager = LinearLayoutManager(requireContext())
            cookingAdapter = CookingInputAdapter(recipe.cooking)
            binding.rvSteps.adapter = cookingAdapter
            val cookingTouchHelper = ItemTouchHelper(ListInputDragCallback(recipe))
            cookingTouchHelper.attachToRecyclerView(binding.rvSteps)

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
        }
    }

    inner class ListInputDragCallback(val recipe: RecipeInput): ItemTouchHelper.Callback() {

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}