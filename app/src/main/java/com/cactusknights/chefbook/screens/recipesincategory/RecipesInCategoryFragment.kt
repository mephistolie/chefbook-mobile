package com.cactusknights.chefbook.screens.favourite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cactusknights.chefbook.screens.main.MainActivity
import com.cactusknights.chefbook.screens.recipe.RecipeActivity
import com.cactusknights.chefbook.screens.main.adapters.RecipeAdapter
import com.cactusknights.chefbook.databinding.FragmentRecyclerViewBinding
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.screens.main.MainActivityViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class RecipesInCategoryFragment(val category: String? = null): Fragment() {

    private val viewModel by activityViewModels<MainActivityViewModel>()
    private var customRecipes = listOf<Recipe>()
    private val customAdapter = RecipeAdapter(::onRecipeClick)

    private var _binding: FragmentRecyclerViewBinding? = null
    private val binding get() = _binding!!

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
        binding.rvContent.adapter = customAdapter
        viewLifecycleOwner.lifecycleScope.launch { viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) { checkForUpdates() } }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun checkForUpdates() {
        viewModel.getRecipes()
        viewModel.state.collect { state ->
            customRecipes = state.recipes.filter { it.categories.contains(state.currentCategory?.id) }
            customAdapter.differ.submitList(customRecipes)
        }
    }

//    private suspend fun checkForUpdates() {
//        viewModel.listenToRecipes().collect { recipes: ArrayList<Recipe> ->
//            if (category == null) {
//                customRecipes = recipes.filter { it.isFavourite } as ArrayList<Recipe>
////                customAdapter.differ.submitList(customRecipes)
//                binding.textEmptyList.visibility = if (customRecipes.size > 0) View.GONE else View.VISIBLE
//            } else {
//                customRecipes = recipes.filter { it.categories.contains(category) } as ArrayList<Recipe>
////                customAdapter.differ.submitList(customRecipes)
//                binding.textEmptyList.visibility = if (customRecipes.size > 0) View.GONE else View.VISIBLE
//            }
//        }
//    }

    fun onRecipeClick(recipe: Recipe) {
        val mainActivity = activity as MainActivity
        val intent = Intent(mainActivity, RecipeActivity()::class.java)
        intent.putExtra("recipe", recipe)
//        intent.putStringArrayListExtra("allCategories", viewModel.getCategories())
//        intent.putExtra("isPremium", viewModel.isPremium())
        val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mainActivity)
        startActivity(intent, options.toBundle())
        mainActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}