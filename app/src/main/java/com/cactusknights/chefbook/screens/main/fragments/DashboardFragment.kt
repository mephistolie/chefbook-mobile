package com.cactusknights.chefbook.screens.main.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.legacy.activities.RecipeActivity
import com.cactusknights.chefbook.legacy.adapters.RecentlyAddedAdapter
import com.cactusknights.chefbook.legacy.adapters.RecipeAdapter
import com.cactusknights.chefbook.databinding.FragmentRecipesDashboardBinding
import com.cactusknights.chefbook.legacy.fragments.CategoriesFragment
import com.cactusknights.chefbook.legacy.fragments.CustomRecipesFragment
import com.cactusknights.chefbook.legacy.helpers.Utils
import com.cactusknights.chefbook.models.BaseRecipe
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.screens.main.MainActivity
import com.cactusknights.chefbook.screens.main.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment: Fragment() {

    private var recipes = listOf<BaseRecipe>()
    private var recentlyAddedRecipes = listOf<BaseRecipe>()
    private val viewModel by activityViewModels<MainActivityViewModel>()

    private lateinit var mainActivity: MainActivity

    private var _binding: FragmentRecipesDashboardBinding? = null
    private val binding get() = _binding!!

    private var allAdapter = RecipeAdapter(::openRecipe)
    private val recentlyAddedAdapter = RecentlyAddedAdapter(::openRecipe)

    private suspend fun checkForUpdates() {
        viewModel.recipes.collect { newRecipes: List<BaseRecipe> ->

            recipes = newRecipes.sortedBy { it.name.lowercase() }
            allAdapter.differ.submitList(recipes)

            recentlyAddedRecipes = newRecipes.sortedByDescending { it.creationTimestamp }.take(4)
            recentlyAddedAdapter.differ.submitList(recentlyAddedRecipes)

            setLayout(recipes.size)
        }
    }

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

        binding.btnAddRecipe.setOnClickListener {
//            if (viewModel.isPremium() || viewModel.getRecipesCount() < 15) {
//                val intent = Intent(activity, RecipeCommitActivity()::class.java)
//                intent.putStringArrayListExtra("allCategories", viewModel.getCategories())
//                val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mainActivity)
//                startActivity(intent, options.toBundle())
//                mainActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//            } else {
//                Toast.makeText(activity, R.string.recipes_limit, Toast.LENGTH_SHORT).show()
//                DonateDialog().show(requireActivity().supportFragmentManager, "Donate")
//            }
        }

        binding.btnFavourite.setOnClickListener {
            mainActivity.setTopMenu(resources.getString(R.string.favourite), true)
            mainActivity.setFragment(CustomRecipesFragment(), R.anim.zoom_in_show, R.anim.zoom_in_hide,"Favourite")
        }
        binding.btnCategories.setOnClickListener {
            mainActivity.setTopMenu(resources.getString(R.string.categories), true)
            mainActivity.setFragment(CategoriesFragment(), R.anim.zoom_in_show, R.anim.zoom_in_hide,"Categories")
        }

        binding.inputSearch.doOnTextChanged { text, _, _, _ ->

            if (text != null && text.isNotEmpty()) {
                binding.llFunctions.visibility = View.GONE
                binding.textRecentlyAdded.visibility = View.GONE
                binding.rvRecentlyAdded.visibility = View.GONE
                binding.btnClearSearch.visibility = View.VISIBLE
                binding.textAllRecipes.text = resources.getString(R.string.search_results)
                recipes = viewModel.recipes.value.filter {
                    it.name.lowercase().contains(text.toString().lowercase())
                            || !it.categories.filter { category -> category.lowercase().contains(text.toString().lowercase()) }.isNullOrEmpty()
                }
                allAdapter.differ.submitList(recipes)
                binding.textEmptyList.visibility = if (recipes.isNotEmpty()) View.GONE else View.VISIBLE
            } else {
                binding.llFunctions.visibility = View.VISIBLE
                binding.textRecentlyAdded.visibility = View.VISIBLE
                binding.rvRecentlyAdded.visibility = View.VISIBLE
                binding.btnClearSearch.visibility = View.GONE
                binding.textAllRecipes.text = resources.getString(R.string.all_recipes)
                recipes = viewModel.recipes.value.sortedBy { it.name.lowercase() }
                allAdapter.differ.submitList(recipes)
                binding.textEmptyList.visibility = if (recipes.isNotEmpty()) View.GONE else View.VISIBLE
            }
        }

        binding.btnClearSearch.setOnClickListener {
            binding.inputSearch.setText("")
            Utils.hideKeyboard(mainActivity)
        }
    }

    override fun onStart() {
        lifecycleScope.launch { checkForUpdates() }
        super.onStart()
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

    private fun openRecipe(recipe: BaseRecipe) {
        val intent = Intent(activity, RecipeActivity()::class.java)
        intent.putExtra("recipe", recipe)
//        intent.putStringArrayListExtra("allCategories", viewModel.getCategories())
//        intent.putExtra("isPremium", viewModel.isPremium())
        val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((mainActivity))
        startActivity(intent, options.toBundle())
        mainActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}