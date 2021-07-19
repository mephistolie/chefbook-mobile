package com.cactusknights.chefbook.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.activities.MainActivity
import com.cactusknights.chefbook.activities.RecipeActivity
import com.cactusknights.chefbook.adapters.RecipeAdapter
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.viewmodels.UserViewModel
import kotlin.collections.ArrayList


class CustomRecipesFragment(val category: String? = null): Fragment(), RecipeAdapter.RecipeClickListener {

    private val viewModel by activityViewModels<UserViewModel>()
    private var customRecipes: ArrayList<Recipe> = arrayListOf()
    private val customAdapter = RecipeAdapter(customRecipes, this)

    lateinit var recyclerView: RecyclerView
    private lateinit var emptyListTitle: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = customAdapter

        emptyListTitle = view.findViewById(R.id.empty_list_title)
    }

    override fun onStart() {
        super.onStart()
        viewModel.recipes.observe(this, { recipes ->
            if (category == null) {
                customRecipes = recipes.filter { it.isFavourite } as ArrayList<Recipe>
                customAdapter.updateRecipes(customRecipes)
                emptyListTitle.visibility = if (customRecipes.size > 0) View.GONE else View.VISIBLE
            } else {
                customRecipes = recipes.filter { it.categories.contains(category) } as ArrayList<Recipe>
                customAdapter.updateRecipes(customRecipes)
                emptyListTitle.visibility = if (customRecipes.size > 0) View.GONE else View.VISIBLE
            }
        })
    }

    override fun onRecipeClick(recipe: Recipe) {
        val intent = Intent(activity, RecipeActivity()::class.java)
        intent.putExtra("recipe", recipe)
        intent.putStringArrayListExtra("allCategories", viewModel.getCurrentCategories())
        intent.putExtra("isPremium", viewModel.isPremium())
        val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((activity as MainActivity))
        startActivity(intent, options.toBundle())
        (activity as MainActivity).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}