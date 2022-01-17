package com.cactusknights.chefbook.screens.recipe.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cactusknights.chefbook.common.Utils.forceSubmitList
import com.cactusknights.chefbook.databinding.DialogCategoriesBinding
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.screens.recipe.RecipeViewModel
import com.cactusknights.chefbook.screens.recipe.adapters.RecipeCategoryAdapter
import com.cactusknights.chefbook.screens.recipe.models.RecipeActivityEvent
import com.cactusknights.chefbook.screens.recipe.models.RecipeActivityState
import kotlinx.coroutines.flow.collect
import java.util.ArrayList

class CategoriesDialog : DialogFragment() {

    private val viewModel by activityViewModels<RecipeViewModel>()
    private lateinit var binding: DialogCategoriesBinding

    private val categoriesAdapter = RecipeCategoryAdapter()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        binding = DialogCategoriesBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategories.adapter = categoriesAdapter

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnConfirm.setOnClickListener {
            viewModel.obtainEvent(RecipeActivityEvent.SetCategories(categoriesAdapter.differ.currentList.filter {
                it.item?.id != null && it.isSelected }.map { it.item?.id!! } as ArrayList<Int>))
            dialog.dismiss()
        }

        lifecycleScope.launchWhenResumed {
            viewModel.recipeState.collect { state ->
                if (state is RecipeActivityState.DataUpdated) {
                    val cachedSelectedIds = categoriesAdapter.differ.currentList.filter { it.isSelected }.map { it.item?.id }
                    val newCategories = state.categories.map { Selectable(
                        item = it,
                        isSelected = it.id in state.recipe.categories || it.id in cachedSelectedIds) }
                    categoriesAdapter.differ.forceSubmitList(newCategories)
                }
            }
        }
        return dialog
    }
}