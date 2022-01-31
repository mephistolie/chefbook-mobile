package com.cactusknights.chefbook.screens.main.fragments.favourite

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
import com.cactusknights.chefbook.common.Utils.forceSubmitList
import com.cactusknights.chefbook.databinding.FragmentRecyclerViewBinding
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.screens.main.MainActivity
import com.cactusknights.chefbook.screens.common.recipes.RecipesViewModel
import com.cactusknights.chefbook.screens.common.recipes.adapters.RecipeAdapter
import com.cactusknights.chefbook.screens.common.recipes.models.RecipesState
import com.cactusknights.chefbook.screens.recipe.RecipeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouriteRecipesFragment(val category: String? = null): Fragment() {

    private val viewModel by activityViewModels<RecipesViewModel>()
    private lateinit var favouriteAdapter : RecipeAdapter

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
        favouriteAdapter = RecipeAdapter(requireActivity(), ::onRecipeClick)
        binding.rvContent.adapter = favouriteAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipesState.collect { state -> if (state is RecipesState.RecipesUpdated) render(state) }
            }
        }
    }

    private fun render(state: RecipesState.RecipesUpdated) {
        favouriteAdapter.differ.forceSubmitList(state.recipes.filter { it.isFavourite }.sortedBy { it.name })
        if (favouriteAdapter.differ.currentList.isEmpty()) {
            binding.textEmptyList.visibility = View.VISIBLE
            binding.rvContent.visibility = View.GONE
        } else {
            binding.textEmptyList.visibility = View.GONE
            binding.rvContent.visibility = View.VISIBLE
        }
    }

    private fun onRecipeClick(recipe: Recipe) {
        val mainActivity = activity as MainActivity
        val intent = Intent(mainActivity, RecipeActivity::class.java)
        intent.putExtra("recipe", recipe)
        val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mainActivity)
        startActivity(intent, options.toBundle())
        mainActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}