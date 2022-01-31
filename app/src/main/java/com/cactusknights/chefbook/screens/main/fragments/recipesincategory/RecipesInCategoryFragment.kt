package com.cactusknights.chefbook.screens.main.fragments.recipesincategory

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
import com.cactusknights.chefbook.databinding.FragmentRecyclerViewBinding
import com.cactusknights.chefbook.screens.main.NavigationViewModel
import com.cactusknights.chefbook.screens.common.recipes.RecipesViewModel
import com.cactusknights.chefbook.screens.common.recipes.adapters.RecipeAdapter
import com.cactusknights.chefbook.screens.main.models.NavigationEvent
import com.cactusknights.chefbook.screens.common.recipes.models.RecipesState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class RecipesInCategoryFragment: Fragment() {

    companion object {
        const val CATEGORY_ID = "CATEGORY_ID"
    }

    private val navigationViewModel by activityViewModels<NavigationViewModel>()
    private val recipeViewModel by activityViewModels<RecipesViewModel>()

    private lateinit var recipesInCategoryAdapter : RecipeAdapter

    private var _binding: FragmentRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private var categoryId by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvContent.layoutManager = LinearLayoutManager(context)
        recipesInCategoryAdapter = RecipeAdapter(requireActivity()) { recipe -> navigationViewModel.obtainEvent(NavigationEvent.OpenRecipe(recipe)) }
        binding.rvContent.adapter = recipesInCategoryAdapter
        categoryId = arguments?.getInt(CATEGORY_ID)?:0
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                recipeViewModel.recipesState.collect { state -> if (state is RecipesState.RecipesUpdated) render(state) }
            }
        }
    }

    private fun render(state: RecipesState.RecipesUpdated) {
        recipesInCategoryAdapter.differ.forceSubmitList(state.recipes.filter { it.categories.contains(categoryId) }.sortedBy { it.name })
        if (recipesInCategoryAdapter.differ.currentList.isEmpty()) {
            binding.textEmptyList.visibility = View.VISIBLE
            binding.rvContent.visibility = View.GONE
        } else {
            binding.textEmptyList.visibility = View.GONE
            binding.rvContent.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}