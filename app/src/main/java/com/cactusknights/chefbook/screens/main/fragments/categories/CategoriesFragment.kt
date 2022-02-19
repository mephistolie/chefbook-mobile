package com.cactusknights.chefbook.screens.main.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.cactusknights.chefbook.common.forceSubmitList
import com.cactusknights.chefbook.databinding.FragmentCategoriesBinding
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.screens.main.NavigationViewModel
import com.cactusknights.chefbook.screens.main.fragments.categories.adapters.CategoryAdapter
import com.cactusknights.chefbook.screens.main.fragments.categories.adapters.CategoryItemDecoration
import com.cactusknights.chefbook.screens.main.fragments.categories.dialogs.CategoryInputDialog
import com.cactusknights.chefbook.screens.main.fragments.categories.models.CategoriesScreenEvent
import com.cactusknights.chefbook.screens.main.fragments.categories.models.CategoriesScreenState
import com.cactusknights.chefbook.screens.main.models.NavigationEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private val viewModel : CategoriesViewModel by viewModels()
    private val activityViewModel by activityViewModels<NavigationViewModel>()
    private val categoriesAdapter = CategoryAdapter(::openCategory, ::addCategory) { updatedCategory ->
        CategoryInputDialog(updatedCategory,
            confirmListener = { category -> viewModel.obtainEvent(CategoriesScreenEvent.UpdateCategory(category)) },
            deleteListener = { category -> viewModel.obtainEvent(CategoriesScreenEvent.DeleteCategory(category)) })
            .show(requireActivity().supportFragmentManager, "Input Category")
    }

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvContent.setHasFixedSize(true)
        binding.rvContent.adapter = categoriesAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.categoriesState.collect { state ->
                    if (state is CategoriesScreenState.CategoriesUpdated) {
                        categoriesAdapter.differ.forceSubmitList(state.categories.sortedBy { it.name.lowercase() }.toList())
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openCategory(position: Int) {
        activityViewModel.obtainEvent(NavigationEvent.OpenCategory(categoriesAdapter.differ.currentList[position]))
    }

    private fun addCategory() {
        CategoryInputDialog(confirmListener = {category: Category-> viewModel.obtainEvent(CategoriesScreenEvent.AddCategory(category))}).show(requireActivity().supportFragmentManager, "Input Category")
    }
}