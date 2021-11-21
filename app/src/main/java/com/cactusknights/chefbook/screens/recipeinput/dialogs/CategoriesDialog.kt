package com.cactusknights.chefbook.screens.recipeinput.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cactusknights.chefbook.common.Utils
import com.cactusknights.chefbook.screens.recipeinput.adapters.CategoryEditAdapter
import com.cactusknights.chefbook.databinding.DialogCategoriesBinding
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.screens.recipe.RecipeViewModel
import com.cactusknights.chefbook.screens.recipeinput.RecipeInputViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CategoriesDialog() : DialogFragment() {

    private val viewModel by activityViewModels<RecipeInputViewModel>()
    private lateinit var binding: DialogCategoriesBinding

    private val categoriesAdapter =
        CategoryEditAdapter { index: Int -> viewModel.changeCategoryCheckedStatus(index) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogCategoriesBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategories.adapter = categoriesAdapter

        binding.btnAddCategory.setOnClickListener {
            val newCategoryText = binding.inputNewCategory.text.toString()
            if (newCategoryText != "" && newCategoryText !in viewModel.state.value.categories.map { it.name }) {
                viewModel.addCategory(Category(name = binding.inputNewCategory.text.toString()))
            }
            binding.inputNewCategory.setText("")
        }

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnConfirm.setOnClickListener {
            dialog.dismiss()
        }

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                categoriesAdapter.differ.submitList(state.categories.mapIndexed { index, category ->
                    Selectable(
                        category.name,
                        state.selectedCategories[index].isSelected
                    )
                })
            }
        }

        return dialog
    }
}