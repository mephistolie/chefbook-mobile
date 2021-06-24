package com.cactusknights.chefbook.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.PopularAdapter
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.RecipeAdapter
import com.cactusknights.chefbook.app.viewmodels.RecipesViewModel


class RecipesFragment: Fragment(), RecipeAdapter.RecipeClickListener, PopularAdapter.RecipeClickListener {

    private lateinit var allRecyclerView: RecyclerView
    private lateinit var popularRecyclerView: RecyclerView
    private val allAdapter = RecipeAdapter(arrayListOf(), this)
    private val popularAdapter = PopularAdapter(arrayListOf(), this)

    private lateinit var viewModel : RecipesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipes_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allRecyclerView = view.findViewById(R.id.recipes_recycler_view)
        allRecyclerView.layoutManager = LinearLayoutManager(activity)
        allRecyclerView.adapter = allAdapter

        popularRecyclerView = view.findViewById(R.id.popular_recycler_view)
        popularRecyclerView.layoutManager = LinearLayoutManager(activity, GridLayoutManager.HORIZONTAL, false)
        popularRecyclerView.adapter = popularAdapter

        viewModel = ViewModelProvider(this).get(RecipesViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        viewModel.recipes.observe(this, { recipes ->
            allAdapter.updateRecipes(recipes)
            popularAdapter.updateRecipes(recipes)
        })
    }

    override fun onRecipeClick(position: Int) {
        Toast.makeText(activity, position.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onPopularClick(position: Int) {
        Toast.makeText(activity, position.toString(), Toast.LENGTH_LONG).show()
    }
}