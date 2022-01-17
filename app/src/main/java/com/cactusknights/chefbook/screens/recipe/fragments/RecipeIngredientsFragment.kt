package com.cactusknights.chefbook.screens.recipe.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.Utils.forceSubmitList
import com.cactusknights.chefbook.databinding.FragmentRecipeIngredientsBinding
import com.cactusknights.chefbook.models.MarkdownString
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.screens.recipe.RecipeViewModel
import com.cactusknights.chefbook.screens.recipe.adapters.IngredientAdapter
import com.cactusknights.chefbook.screens.recipe.models.RecipeActivityEvent
import com.cactusknights.chefbook.screens.recipe.models.RecipeActivityState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeIngredientsFragment : Fragment() {

    private val viewModel by activityViewModels<RecipeViewModel>()
    private var ingredientsAdapter =
        IngredientAdapter { index ->
            viewModel.obtainEvent(RecipeActivityEvent.ChangeIngredientSelectStatus(index))
            val state = viewModel.recipeState.value
            binding.btnAddToShoplist.visibility = if (state is RecipeActivityState.DataUpdated
                && state.selectedIngredients.any { it.isSelected }) View.VISIBLE else View.GONE
        }

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
        binding.rvIngredients.adapter = ingredientsAdapter

        binding.btnAddToShoplist.setOnClickListener { viewModel.obtainEvent(RecipeActivityEvent.AddSelectedToShoppingList) }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipeState.collect { state ->
//                    val newList = arrayListOf<Selectable<MarkdownString>>().apply {
//                        addAll(state.recipe.ingredients.mapIndexed { index, ingredient -> Selectable(ingredient, state.selectedIngredients[index].isSelected) })
//                    }
                    if (state is RecipeActivityState.DataUpdated) {
                        ingredientsAdapter.differ.forceSubmitList(state.recipe.ingredients.mapIndexed { index, ingredient -> Selectable(ingredient, state.selectedIngredients[index].isSelected) } as ArrayList<Selectable<MarkdownString>>)

                        if (state.selectedIngredients.any { it.isSelected }) {
                            binding.btnAddToShoplist.visibility = View.VISIBLE
                        } else {
                            binding.btnAddToShoplist.visibility = View.GONE
                            for (child in 0 until binding.rvIngredients.childCount) {
                                binding.rvIngredients.getChildAt(child).findViewById<CheckBox>(R.id.checkbox_selected).isChecked = false
                            }
                        }
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