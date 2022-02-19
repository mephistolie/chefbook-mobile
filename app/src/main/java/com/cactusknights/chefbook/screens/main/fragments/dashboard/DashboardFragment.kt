package com.cactusknights.chefbook.screens.main.fragments.dashboard

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
import com.cactusknights.chefbook.common.Utils
import com.cactusknights.chefbook.common.forceSubmitList
import com.cactusknights.chefbook.databinding.FragmentRecipesDashboardBinding
import com.cactusknights.chefbook.models.RecipeInfo
import com.cactusknights.chefbook.screens.main.MainActivity
import com.cactusknights.chefbook.screens.main.NavigationViewModel
import com.cactusknights.chefbook.screens.common.recipes.RecipesViewModel
import com.cactusknights.chefbook.screens.common.recipes.adapters.QuckAccessAdapter
import com.cactusknights.chefbook.screens.common.recipes.adapters.RecipeAdapter
import com.cactusknights.chefbook.screens.main.models.NavigationEvent
import com.cactusknights.chefbook.screens.common.recipes.models.RecipesEvent
import com.cactusknights.chefbook.screens.common.recipes.models.RecipesState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var recipes = listOf<RecipeInfo>()
    private var recentlyUpdatedRecipes = listOf<RecipeInfo>()

    private val navigationViewModel by activityViewModels<NavigationViewModel>()
    private val recipeViewModel by activityViewModels<RecipesViewModel>()

    private lateinit var mainActivity: MainActivity

    private var _binding: FragmentRecipesDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var allAdapter : RecipeAdapter
    private lateinit var quickAccessAdapter : QuckAccessAdapter

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
        allAdapter = RecipeAdapter(requireActivity()) { navigationViewModel.obtainEvent(NavigationEvent.OpenRecipe(it)) }
        quickAccessAdapter = QuckAccessAdapter(requireActivity()) { navigationViewModel.obtainEvent(NavigationEvent.OpenRecipe(it)) }

        binding.rvAllRecipes.layoutManager = LinearLayoutManager(activity)
        binding.rvAllRecipes.adapter = allAdapter
        binding.rvAllRecipes.itemAnimator = null
        binding.rvQuickAccess.layoutManager = LinearLayoutManager(activity, GridLayoutManager.HORIZONTAL, false)
        binding.rvQuickAccess.adapter = quickAccessAdapter

        binding.llAddRecipe.setOnClickListener { navigationViewModel.obtainEvent(NavigationEvent.AddRecipe) }

        binding.llFavourite.setOnClickListener { navigationViewModel.obtainEvent(NavigationEvent.OpenFavouriteFragment) }
        binding.llCategories.setOnClickListener { navigationViewModel.obtainEvent(NavigationEvent.OpenCategoriesFragment) }

        binding.inputSearch.doOnTextChanged { text, _, _, _ ->
            recipeViewModel.obtainEvent(RecipesEvent.SearchRecipe(text.toString()))
        }

        binding.btnClearSearch.setOnClickListener {
            binding.inputSearch.setText("")
            Utils.hideKeyboard(mainActivity)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                recipeViewModel.recipesState.collect { state -> render(state) }
            }
        }
    }

    private fun render(state: RecipesState) {
        when (state) {
            is RecipesState.Loading -> {
                binding.rvAllRecipes.visibility = View.GONE
                binding.rvQuickAccess.visibility = View.GONE
                binding.shimmerRecipes.visibility = View.VISIBLE
                binding.shimmerQuickAccess.visibility = View.VISIBLE
                binding.shimmerRecipes.startShimmer()
                binding.shimmerQuickAccess.startShimmer()
            }
            is RecipesState.RecipesUpdated -> {
                binding.llFunctions.visibility = View.VISIBLE
                binding.rvAllRecipes.visibility = View.VISIBLE
                binding.btnClearSearch.visibility = View.GONE
                binding.textAllRecipes.text = resources.getString(R.string.all_recipes)

                binding.llContent.visibility = View.VISIBLE

                if (state.keys != null) {
                    allAdapter.setRecipeKeys(state.keys)
                    quickAccessAdapter.setRecipeKeys(state.keys)
                }
                recipes = state.recipes.sortedBy { it.name.lowercase() }
                setLayout(recipes.size)
                allAdapter.differ.forceSubmitList(recipes)

                recentlyUpdatedRecipes = state.recipes.sortedByDescending { it.updateTimestamp }.take(6)
                quickAccessAdapter.differ.forceSubmitList(recentlyUpdatedRecipes)
                binding.textEmptyList.visibility = if (recipes.isNotEmpty()) View.GONE else View.VISIBLE
            }
            is RecipesState.SearchResult -> {
                binding.llFunctions.visibility = View.GONE
                binding.textQuickAccess.visibility = View.GONE
                binding.rvQuickAccess.visibility = View.GONE
                binding.btnClearSearch.visibility = View.VISIBLE
                binding.textAllRecipes.text = resources.getString(R.string.search_results)

                allAdapter.differ.forceSubmitList(state.result.sortedBy { it.name.lowercase() })
                binding.textEmptyList.visibility = if (state.result.isNotEmpty()) View.GONE else View.VISIBLE
            }
        }
        if (state != RecipesState.Loading) {
            binding.shimmerRecipes.visibility = View.GONE
            binding.shimmerQuickAccess.visibility = View.GONE
            binding.shimmerRecipes.stopShimmer()
            binding.shimmerQuickAccess.stopShimmer()
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
                binding.textQuickAccess.visibility = View.VISIBLE
                binding.rvQuickAccess.visibility = View.VISIBLE
                binding.textEmptyList.visibility = View.GONE
            }
            recipesCount > 0 -> {
                binding.llSearch.visibility = View.GONE
                binding.textQuickAccess.visibility = View.GONE
                binding.rvQuickAccess.visibility = View.GONE
                binding.textEmptyList.visibility = View.GONE
            }
            else -> {
                binding.llSearch.visibility = View.GONE
                binding.textQuickAccess.visibility = View.GONE
                binding.rvQuickAccess.visibility = View.GONE
                binding.textEmptyList.visibility = View.VISIBLE
            }
        }
    }
}