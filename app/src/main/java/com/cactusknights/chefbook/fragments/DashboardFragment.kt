package com.cactusknights.chefbook.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.activities.RecipeCommitActivity
import com.cactusknights.chefbook.activities.MainActivity
import com.cactusknights.chefbook.activities.RecipeActivity
import com.cactusknights.chefbook.adapters.RecentlyAddedAdapter
import com.cactusknights.chefbook.adapters.RecipeAdapter
import com.cactusknights.chefbook.databinding.FragmentRecipesDashboardBinding
import com.cactusknights.chefbook.dialogs.DonateDialog
import com.cactusknights.chefbook.helpers.Utils
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.viewmodels.UserViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList


class DashboardFragment: Fragment(), RecipeAdapter.RecipeClickListener, RecentlyAddedAdapter.OnRecentlyAdded {

    private val recipes = arrayListOf<Recipe>()
    private val recentlyAddedRecipes = arrayListOf<Recipe>()
    private val viewModel by activityViewModels<UserViewModel>()

    private lateinit var mainActivity: MainActivity

    private var _binding: FragmentRecipesDashboardBinding? = null
    private val binding get() = _binding!!

    private var allAdapter = RecipeAdapter(recipes, this)
    private val recentlyAddedAdapter = RecentlyAddedAdapter(recentlyAddedRecipes, this)

    private suspend fun checkForUpdates() {
        viewModel.listenToRecipes().collect { newRecipes: ArrayList<Recipe> ->

            recipes.clear(); recipes.addAll(newRecipes)
            recipes.sortBy { it.name.lowercase() }
            allAdapter.notifyDataSetChanged()

            recentlyAddedRecipes.clear()
            recentlyAddedRecipes.addAll(newRecipes.sortedByDescending { it.creationDate }.take(4))
            recentlyAddedAdapter.notifyDataSetChanged()

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
        binding.rvRecentlyAdded.layoutManager = LinearLayoutManager(activity, GridLayoutManager.HORIZONTAL, false)
        binding.rvRecentlyAdded.adapter = recentlyAddedAdapter

        binding.btnAddRecipe.setOnClickListener {
            if (viewModel.isPremium() || viewModel.getRecipesCount() < 15) {
                val intent = Intent(activity, RecipeCommitActivity()::class.java)
                intent.putStringArrayListExtra("allCategories", viewModel.getCategories())
                val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mainActivity)
                startActivity(intent, options.toBundle())
                mainActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            } else {
                Toast.makeText(activity, R.string.recipes_limit, Toast.LENGTH_SHORT).show()
                DonateDialog().show(requireActivity().supportFragmentManager, "Donate")
            }
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
                recipes.clear()
                recipes.addAll(viewModel.getRecipes().filter {
                    it.name.lowercase().contains(text.toString().lowercase())
                            || !it.categories.filter { category -> category.lowercase().contains(text.toString().lowercase()) }.isNullOrEmpty()
                })
                allAdapter.notifyDataSetChanged()
                binding.textEmptyList.visibility = if (recipes.size > 0) View.GONE else View.VISIBLE
            } else {
                binding.llFunctions.visibility = View.VISIBLE
                binding.textRecentlyAdded.visibility = View.VISIBLE
                binding.rvRecentlyAdded.visibility = View.VISIBLE
                binding.btnClearSearch.visibility = View.GONE
                binding.textAllRecipes.text = resources.getString(R.string.all_recipes)
                recipes.clear()
                recipes.addAll(viewModel.getRecipes() as Collection<Recipe>)
                recipes.sortBy { it.name.lowercase() }
                allAdapter.notifyDataSetChanged()
                binding.textEmptyList.visibility = if (recipes.size > 0) View.GONE else View.VISIBLE
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

    override fun onRecipeClick(recipe: Recipe) { openRecipe(recipe) }

    override fun onRecentlyAddedClick(recipe: Recipe) { openRecipe(recipe) }

    private fun openRecipe(recipe: Recipe) {
        val intent = Intent(activity, RecipeActivity()::class.java)
        intent.putExtra("recipe", recipe)
        intent.putStringArrayListExtra("allCategories", viewModel.getCategories())
        intent.putExtra("isPremium", viewModel.isPremium())
        val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((mainActivity))
        startActivity(intent, options.toBundle())
        mainActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}