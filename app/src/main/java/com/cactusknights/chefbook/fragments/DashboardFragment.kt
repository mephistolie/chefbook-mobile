package com.cactusknights.chefbook.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.activities.RecipeCommitActivity
import com.cactusknights.chefbook.activities.MainActivity
import com.cactusknights.chefbook.activities.RecipeActivity
import com.cactusknights.chefbook.adapters.RecentlyAddedAdapter
import com.cactusknights.chefbook.adapters.RecipeAdapter
import com.cactusknights.chefbook.dialogs.DonateDialog
import com.cactusknights.chefbook.helpers.Utils
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.viewmodels.UserViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlin.collections.ArrayList


class DashboardFragment: Fragment(), RecipeAdapter.RecipeClickListener, RecentlyAddedAdapter.OnRecentlyAdded {

    private val recipes = arrayListOf<Recipe>()
    private val recentlyAddedRecipes = arrayListOf<Recipe>()
    private val viewModel by activityViewModels<UserViewModel>()

    lateinit var mainActivity: MainActivity

    private lateinit var searchLayout: LinearLayout
    private lateinit var searchInput: TextInputEditText
    private lateinit var clearSearch: ImageButton

    private lateinit var recentlyAddedTitle: TextView
    private lateinit var allRecipesTitle: TextView

    private lateinit var functionButtons: LinearLayout
    private lateinit var addRecipeButton: CardView
    private lateinit var favouriteRecipesButton: CardView
    private lateinit var categoriesButton: CardView

    private lateinit var allRecyclerView: RecyclerView
    private lateinit var recentlyAddedRecyclerView: RecyclerView
    private var allAdapter = RecipeAdapter(recipes, this)
    private val recentlyAddedAdapter = RecentlyAddedAdapter(recentlyAddedRecipes, this)

    private lateinit var emptyListTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.recipes.observe(this, { newRecipes ->

            recipes.clear(); recipes.addAll(newRecipes)
            recipes.sortBy { it.name.lowercase() }
            allAdapter.notifyDataSetChanged()

            recentlyAddedRecipes.clear()
            recentlyAddedRecipes.addAll(newRecipes.sortedByDescending { it.creationDate }.take(4))
            recentlyAddedAdapter.notifyDataSetChanged()

            setLayout(recipes.size)
        })
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipes_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = activity as MainActivity

        searchLayout = view.findViewById(R.id.search_layout)
        searchInput = view.findViewById(R.id.search_field)
        clearSearch = view.findViewById(R.id.clear_search)
        recentlyAddedTitle = view.findViewById(R.id.popular_title)
        allRecipesTitle = view.findViewById(R.id.all_recipes_title)
        functionButtons = view.findViewById(R.id.function_buttons)
        addRecipeButton = view.findViewById(R.id.add_recipe)
        favouriteRecipesButton = view.findViewById(R.id.favourite)
        categoriesButton = view.findViewById(R.id.categories)
        allRecyclerView = view.findViewById(R.id.recipes_recycler_view)
        allRecyclerView.layoutManager = LinearLayoutManager(activity)
        allRecyclerView.adapter = allAdapter
        recentlyAddedRecyclerView = view.findViewById(R.id.popular_recycler_view)
        recentlyAddedRecyclerView.layoutManager = LinearLayoutManager(activity, GridLayoutManager.HORIZONTAL, false)
        recentlyAddedRecyclerView.adapter = recentlyAddedAdapter
        emptyListTitle = view.findViewById(R.id.empty_list_title)

        addRecipeButton.setOnClickListener {
            if (viewModel.isPremium() || viewModel.getRecipesCount() < 15) {
                val intent = Intent(activity, RecipeCommitActivity()::class.java)
                intent.putStringArrayListExtra("allCategories", viewModel.getCurrentCategories())
                val options: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(mainActivity)
                startActivity(intent, options.toBundle())
                mainActivity.overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
            } else {
                Toast.makeText(activity, R.string.recipes_limit, Toast.LENGTH_SHORT).show()
                DonateDialog().show(requireActivity().supportFragmentManager, "Donate")
            }
        }

        favouriteRecipesButton.setOnClickListener {
            mainActivity.setTopMenu(resources.getString(R.string.favourite), true)
            mainActivity.setFragment(CustomRecipesFragment(), R.anim.zoom_in_show, R.anim.zoom_in_hide,"Favourite")
        }
        categoriesButton.setOnClickListener {
            mainActivity.setTopMenu(resources.getString(R.string.categories), true)
            mainActivity.setFragment(CategoriesFragment(), R.anim.zoom_in_show, R.anim.zoom_in_hide,"Categories")
        }

        searchInput.doOnTextChanged { text, _, _, _ ->

            if (text != null && text.isNotEmpty()) {
                functionButtons.visibility = View.GONE
                recentlyAddedTitle.visibility = View.GONE
                recentlyAddedRecyclerView.visibility = View.GONE
                clearSearch.visibility = View.VISIBLE
                allRecipesTitle.text = resources.getString(R.string.search_results)
                recipes.clear()
                recipes.addAll(viewModel.recipes.value!!.filter { it.name.lowercase().contains(text.toString().lowercase()) })
                allAdapter.notifyDataSetChanged()
                emptyListTitle.visibility = if (recipes.size > 0) View.GONE else View.VISIBLE
            } else {
                functionButtons.visibility = View.VISIBLE
                recentlyAddedTitle.visibility = View.VISIBLE
                recentlyAddedRecyclerView.visibility = View.VISIBLE
                clearSearch.visibility = View.GONE
                allRecipesTitle.text = resources.getString(R.string.all_recipes)
                recipes.clear()
                recipes.addAll(viewModel.recipes.value as Collection<Recipe>)
                recipes.sortBy { it.name.lowercase() }
                allAdapter.notifyDataSetChanged()
                emptyListTitle.visibility = if (recipes.size > 0) View.GONE else View.VISIBLE
            }
        }

        clearSearch.setOnClickListener {
            searchInput.setText("")
            Utils.hideKeyboard(mainActivity)
        }
    }

    override fun onResume() {
        if (recipes.size >= 6) searchInput.setText("")
        super.onResume()
    }

    private fun setLayout(recipesCount: Int) {
        when {
            recipesCount >= 6 -> {
                searchLayout.visibility = View.VISIBLE
                recentlyAddedTitle.visibility = View.VISIBLE
                recentlyAddedRecyclerView.visibility = View.VISIBLE
                emptyListTitle.visibility = View.GONE
            }
            recipesCount > 0 -> {
                searchLayout.visibility = View.GONE
                recentlyAddedTitle.visibility = View.GONE
                recentlyAddedRecyclerView.visibility = View.GONE
                emptyListTitle.visibility = View.GONE
            }
            else -> {
                searchLayout.visibility = View.GONE
                recentlyAddedTitle.visibility = View.GONE
                recentlyAddedRecyclerView.visibility = View.GONE
                emptyListTitle.visibility = View.VISIBLE
            }
        }
    }

    override fun onRecipeClick(recipe: Recipe) { openRecipe(recipe) }

    override fun onRecentlyAddedClick(recipe: Recipe) { openRecipe(recipe) }

    private fun openRecipe(recipe: Recipe) {
        val intent = Intent(activity, RecipeActivity()::class.java)
        intent.putExtra("recipe", recipe)
        intent.putStringArrayListExtra("allCategories", viewModel.getCurrentCategories())
        intent.putExtra("isPremium", viewModel.isPremium())
        val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((mainActivity))
        startActivity(intent, options.toBundle())
        mainActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}