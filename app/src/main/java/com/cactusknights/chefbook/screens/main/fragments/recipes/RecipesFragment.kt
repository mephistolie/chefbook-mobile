package com.cactusknights.chefbook.screens.main.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.screens.main.fragments.recipes.adapters.RecentlyAddedAdapter
import com.cactusknights.chefbook.screens.main.fragments.recipes.adapters.RecipeAdapter
import com.cactusknights.chefbook.databinding.FragmentRecipesDashboardBinding
import com.cactusknights.chefbook.common.Utils
import com.cactusknights.chefbook.common.Utils.forceSubmitList
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.screens.main.MainActivity
import com.cactusknights.chefbook.screens.main.NavigationViewModel
import com.cactusknights.chefbook.screens.main.models.NavigationEvent
import com.cactusknights.chefbook.screens.main.models.RecipesState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment: Fragment() {

    private var recipes = listOf<Recipe>()
    private var recentlyAddedRecipes = listOf<Recipe>()

    private val viewModel by activityViewModels<NavigationViewModel>()

    private lateinit var mainActivity: MainActivity

    private var _binding: FragmentRecipesDashboardBinding? = null
    private val binding get() = _binding!!

    private var allAdapter = RecipeAdapter { viewModel.obtainEvent(NavigationEvent.OpenRecipe(it)) }
    private val recentlyAddedAdapter = RecentlyAddedAdapter { viewModel.obtainEvent(NavigationEvent.OpenRecipe(it)) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = activity as MainActivity

        binding.rvAllRecipes.layoutManager = LinearLayoutManager(activity)
        binding.rvAllRecipes.adapter = allAdapter
        binding.rvAllRecipes.itemAnimator = null
        binding.rvRecentlyAdded.layoutManager = LinearLayoutManager(activity, GridLayoutManager.HORIZONTAL, false)
        binding.rvRecentlyAdded.adapter = recentlyAddedAdapter

        binding.llAddRecipe.setOnClickListener { viewModel.obtainEvent(NavigationEvent.AddRecipe) }

        binding.llFavourite.setOnClickListener { viewModel.obtainEvent(NavigationEvent.OpenFavouriteFragment) }
        binding.llCategories.setOnClickListener { viewModel.obtainEvent(NavigationEvent.OpenCategoriesFragment) }

        binding.inputSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.obtainEvent(NavigationEvent.SearchRecipe(text.toString()))
        }

        binding.btnClearSearch.setOnClickListener {
            binding.inputSearch.setText("")
            Utils.hideKeyboard(mainActivity)
        }
        viewLifecycleOwner.lifecycleScope.launch { viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.recipesState.collect { state -> render(state) }
        } }
    }

    private fun render(state: RecipesState) {
        when (state) {
            is RecipesState.Loading -> {
//                binding.llSearch.visibility = View.GONE
//                binding.llContent.visibility = View.GONE
            }
            is RecipesState.RecipesUpdated -> {
                binding.llFunctions.visibility = View.VISIBLE
                binding.textRecentlyAdded.visibility = View.VISIBLE
                binding.rvRecentlyAdded.visibility = View.VISIBLE
                binding.btnClearSearch.visibility = View.GONE
                binding.textAllRecipes.text = resources.getString(R.string.all_recipes)

                binding.llContent.visibility = View.VISIBLE

                recipes = state.recipes.sortedBy { it.name.lowercase() }
                setLayout(recipes.size)
                allAdapter.differ.forceSubmitList(recipes)

                recentlyAddedRecipes = state.recipes.sortedByDescending { it.updateTimestamp }.take(4)
                recentlyAddedAdapter.differ.forceSubmitList(recentlyAddedRecipes)
                binding.textEmptyList.visibility = if (recipes.isNotEmpty()) View.GONE else View.VISIBLE
            }
            is RecipesState.SearchResult -> {
                binding.llFunctions.visibility = View.GONE
                binding.textRecentlyAdded.visibility = View.GONE
                binding.rvRecentlyAdded.visibility = View.GONE
                binding.btnClearSearch.visibility = View.VISIBLE
                binding.textAllRecipes.text = resources.getString(R.string.search_results)

                allAdapter.differ.forceSubmitList(state.result.sortedBy { it.name.lowercase() })
                binding.textEmptyList.visibility = if (state.result.isNotEmpty()) View.GONE else View.VISIBLE
            }
        }
    }

    override fun onResume() {
        if (recipes.size >= 6) binding.inputSearch.setText("")
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setLayout(recipesCount: Int) {
        when {
            recipesCount >= 6 -> {
                binding.llSearch.visibility = View.VISIBLE
                binding.textRecentlyAdded.visibility = View.VISIBLE
                binding.rvRecentlyAdded.visibility = View.VISIBLE
                binding.textEmptyList.visibility = View.GONE
            }
            recipesCount > 0 -> {
                binding.llSearch.visibility = View.GONE
                binding.textRecentlyAdded.visibility = View.GONE
                binding.rvRecentlyAdded.visibility = View.GONE
                binding.textEmptyList.visibility = View.GONE
            }
            else -> {
                binding.llSearch.visibility = View.GONE
                binding.textRecentlyAdded.visibility = View.GONE
                binding.rvRecentlyAdded.visibility = View.GONE
                binding.textEmptyList.visibility = View.VISIBLE
            }
        }
    }
}