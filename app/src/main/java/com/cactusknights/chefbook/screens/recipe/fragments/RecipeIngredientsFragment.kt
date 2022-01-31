package com.cactusknights.chefbook.screens.recipe.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cactusknights.chefbook.databinding.FragmentRecipeIngredientsBinding
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.screens.recipe.RecipeViewModel
import com.cactusknights.chefbook.screens.recipe.adapters.IngredientAdapter
import com.cactusknights.chefbook.screens.recipe.models.RecipeScreenEvent
import com.cactusknights.chefbook.screens.recipe.models.RecipeScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeIngredientsFragment : Fragment() {

    private val viewModel by activityViewModels<RecipeViewModel>()
    private var ingredientsAdapter = IngredientAdapter { index -> viewModel.obtainEvent(RecipeScreenEvent.ChangeIngredientSelectStatus(index)) }

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

        binding.rvIngredients.layoutManager = LinearLayoutManager(context)
        binding.rvIngredients.itemAnimator = null
        binding.rvIngredients.adapter = ingredientsAdapter

        binding.btnAddToShoplist.setOnClickListener { viewModel.obtainEvent(RecipeScreenEvent.AddSelectedToShoppingList) }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipeState.collect { state ->
                    if (state is RecipeScreenState.DataUpdated) {
                        ingredientsAdapter.differ.submitList(state.recipe.ingredients.mapIndexed { index, ingredient -> Selectable(ingredient, state.selectedIngredients[index].isSelected) })
                        binding.btnAddToShoplist.visibility = if (state.selectedIngredients.any { it.isSelected }) View.VISIBLE else View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}