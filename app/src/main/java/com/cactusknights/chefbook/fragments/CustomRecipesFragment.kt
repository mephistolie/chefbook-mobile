package com.cactusknights.chefbook.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cactusknights.chefbook.activities.MainActivity
import com.cactusknights.chefbook.activities.RecipeActivity
import com.cactusknights.chefbook.adapters.RecipeAdapter
import com.cactusknights.chefbook.databinding.FragmentRecyclerViewBinding
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.viewmodels.UserViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList


class CustomRecipesFragment(val category: String? = null): Fragment(), RecipeAdapter.RecipeClickListener {

    private val viewModel by activityViewModels<UserViewModel>()
    private var customRecipes: ArrayList<Recipe> = arrayListOf()
    private val customAdapter = RecipeAdapter(customRecipes, this)

    private lateinit var binding: FragmentRecyclerViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvContent.layoutManager = LinearLayoutManager(context)
        binding.rvContent.adapter = customAdapter
    }

    override fun onStart() {
        lifecycleScope.launch { checkForUpdates() }
        super.onStart()
    }

    private suspend fun checkForUpdates() {
        viewModel.listenToRecipes().collect { recipes: ArrayList<Recipe> ->
            if (category == null) {
                customRecipes = recipes.filter { it.isFavourite } as ArrayList<Recipe>
                customAdapter.updateRecipes(customRecipes)
                binding.textEmptyList.visibility = if (customRecipes.size > 0) View.GONE else View.VISIBLE
            } else {
                customRecipes = recipes.filter { it.categories.contains(category) } as ArrayList<Recipe>
                customAdapter.updateRecipes(customRecipes)
                binding.textEmptyList.visibility = if (customRecipes.size > 0) View.GONE else View.VISIBLE
            }
        }
    }

    override fun onRecipeClick(recipe: Recipe) {
        val mainActivity = activity as MainActivity
        val intent = Intent(mainActivity, RecipeActivity()::class.java)
        intent.putExtra("recipe", recipe)
        intent.putStringArrayListExtra("allCategories", viewModel.getCategories())
        intent.putExtra("isPremium", viewModel.isPremium())
        val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mainActivity)
        startActivity(intent, options.toBundle())
        mainActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}