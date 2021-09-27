package com.cactusknights.chefbook.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cactusknights.chefbook.adapters.CategoryEditAdapter
import com.cactusknights.chefbook.adapters.CategoryEditAdapter.EditCategoryClickListener
import com.cactusknights.chefbook.databinding.DialogCategoriesBinding
import com.cactusknights.chefbook.models.Selectable

class CategoriesDialog(
    private val categories: ArrayList<Selectable<String>>,
    private val onCommitCategoriesCallback: (categories: ArrayList<String>, allCategories: ArrayList<String>) -> Unit
): DialogFragment(), EditCategoryClickListener {

    private lateinit var binding: DialogCategoriesBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val categoriesAdapter = CategoryEditAdapter(categories, this)

        binding = DialogCategoriesBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategories.adapter = categoriesAdapter

        binding.btnAddCategory.setOnClickListener {
            val newCategoryText = binding.inputNewCategory.text.toString()
            if (newCategoryText != "" && newCategoryText !in categories.map { it.item }) {
                categories.add(Selectable(newCategoryText, true))
                categoriesAdapter.notifyItemInserted(categories.size-1)
                binding.inputNewCategory.setText("")
            } else {
                binding.inputNewCategory.setText("")
            }
        }

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnConfirm.setOnClickListener {
            onCommitCategoriesCallback(
                categories.filter { it.isSelected }.map { it.item!! } as ArrayList<String>,
                categories.map { it.item!! } as ArrayList<String>)
            dialog.dismiss()
        }

        return dialog
    }

    override fun onCategoryClick(position: Int, isChecked: Boolean) {
        categories[position].isSelected = isChecked
        binding.rvCategories.adapter?.notifyItemChanged(position)
    }
}