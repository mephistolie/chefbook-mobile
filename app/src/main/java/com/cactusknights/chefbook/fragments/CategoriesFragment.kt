package com.cactusknights.chefbook.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.activities.MainActivity
import com.cactusknights.chefbook.adapters.CategoryAdapter
import com.cactusknights.chefbook.viewmodels.UserViewModel
import kotlin.collections.ArrayList


class CategoriesFragment: Fragment(), CategoryAdapter.CategoryClickListener {

    private val viewModel by activityViewModels<UserViewModel>()
    private var categories: ArrayList<String> = arrayListOf()
    private val categoriesAdapter = CategoryAdapter(categories, this)

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
        recyclerView.adapter = categoriesAdapter

        emptyListTitle = view.findViewById(R.id.empty_list_title)
    }

    override fun onStart() {
        super.onStart()
        viewModel.categories.observe(this, { newCategories ->
            categories = arrayListOf()
            categories.addAll(newCategories)
            categories.sortBy { it.lowercase() }
            categoriesAdapter.updateCategories(categories)
            emptyListTitle.visibility = if (categories.size > 0) View.GONE else View.VISIBLE
        })
    }

    override fun onCategoryClick(position: Int) {
        val mainActivity = (activity as MainActivity)

        mainActivity.setTopMenu(categories[position], true)
        mainActivity.setFragment(CustomRecipesFragment(categories[position]), R.anim.zoom_in_show, R.anim.zoom_in_hide, "Recipes in Category")
    }
}