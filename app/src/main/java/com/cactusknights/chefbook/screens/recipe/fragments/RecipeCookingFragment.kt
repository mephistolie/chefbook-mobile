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
import com.cactusknights.chefbook.common.Utils.forceSubmitList
import com.cactusknights.chefbook.databinding.FragmentRecipeCookingBinding
import com.cactusknights.chefbook.screens.recipe.RecipeViewModel
import com.cactusknights.chefbook.screens.recipe.adapters.CookingAdapter
import com.cactusknights.chefbook.screens.recipe.models.RecipeScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeCookingFragment : Fragment() {

    private val viewModel by activityViewModels<RecipeViewModel>()
    private var cookingAdapter = CookingAdapter()

    private var _binding: FragmentRecipeCookingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeCookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvSteps.layoutManager = LinearLayoutManager(context)
        binding.rvSteps.adapter = cookingAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipeState.collect { state ->
                    if (state is RecipeScreenState.DataUpdated) cookingAdapter.differ.forceSubmitList(state.recipe.cooking)
                }
            }
        }
    }

    override fun onStop() {
        lifecycleScope.cancel()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}