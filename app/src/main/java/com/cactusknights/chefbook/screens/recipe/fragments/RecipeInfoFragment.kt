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
import com.cactusknights.chefbook.common.Utils
import com.cactusknights.chefbook.databinding.FragmentRecipeInfoBinding
import com.cactusknights.chefbook.screens.recipe.RecipeViewModel
import com.cactusknights.chefbook.screens.recipe.models.RecipeScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeInfoFragment : Fragment() {

    private val viewModel by activityViewModels<RecipeViewModel>()

    private var _binding: FragmentRecipeInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipeState.collect { state ->
                    if (state is RecipeScreenState.DataLoaded) {
                        binding.textServings.text = state.recipe.servings.toString()
                        binding.textTime.text = Utils.minutesToTimeString(state.recipe.time, this@RecipeInfoFragment.resources)
                        if (state.recipe.calories > 0) {
                            binding.cvCalories.visibility = View.VISIBLE
                            binding.textCalories.text = state.recipe.calories.toString()
                        }
                        else binding.cvCalories.visibility = View.GONE

                        if (!state.recipe.description.isNullOrEmpty()) {
                            binding.llDescription.visibility = View.VISIBLE
                            binding.textDescription.text = state.recipe.description
                        } else {
                            binding.llDescription.visibility = View.GONE
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