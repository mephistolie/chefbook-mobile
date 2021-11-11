package com.cactusknights.chefbook.legacy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.screens.main.MainActivity
import com.cactusknights.chefbook.legacy.adapters.CategoryAdapter
import com.cactusknights.chefbook.databinding.FragmentRecyclerViewBinding
import com.cactusknights.chefbook.legacy.viewmodels.UuuserViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList


class CategoriesFragment: Fragment(), CategoryAdapter.CategoryClickListener {

    private val viewModel by activityViewModels<UuuserViewModel>()
    private var categories: ArrayList<String> = arrayListOf()
    private val categoriesAdapter = CategoryAdapter(categories, this)

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
        binding.rvContent.adapter = categoriesAdapter
    }

    override fun onStart() {
        lifecycleScope.launch {
            viewModel.listenToCategories().collect { newCategories: ArrayList<String> ->
                categories = newCategories
                categories.sortBy { it.lowercase() }
                categoriesAdapter.updateCategories(categories)
                binding.textEmptyList.visibility = if (categories.size > 0) View.GONE else View.VISIBLE
            }
        }
        super.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCategoryClick(position: Int) {
        val mainActivity = (activity as MainActivity)

        mainActivity.setTopMenu(categories[position], true)
        mainActivity.setFragment(CustomRecipesFragment(categories[position]), R.anim.zoom_in_show, R.anim.zoom_in_hide, "Recipes in Category")
    }
}