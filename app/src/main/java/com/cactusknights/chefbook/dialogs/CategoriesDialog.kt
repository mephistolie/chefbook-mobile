package com.cactusknights.chefbook.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.adapters.CategoryEditAdapter
import com.cactusknights.chefbook.databinding.DialogCategoriesBinding

class CategoriesDialog(
    private val categories: ArrayList<String>,
    private val allCategories: ArrayList<String>,
    private val onCommitCategoriesCallback: (categories: ArrayList<String>, allCategories: ArrayList<String>) -> Unit
): DialogFragment() {

    private lateinit var binding: DialogCategoriesBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val categoriesAdapter = CategoryEditAdapter(allCategories, categories)

        binding = DialogCategoriesBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategories.adapter = categoriesAdapter

        binding.btnAddCategory.setOnClickListener {
            val newCategoryText = binding.inputNewCategory.text.toString()
            if (newCategoryText != "" && newCategoryText !in allCategories) {
                allCategories.add(newCategoryText)
                categoriesAdapter.notifyItemInserted(allCategories.size-1)
                binding.inputNewCategory.setText("")
            } else {
                binding.inputNewCategory.setText("")
            }
        }

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnConfirm.setOnClickListener {
            val newCategories = arrayListOf<String>()
            for (categoryView in binding.rvCategories.children) {
                val checkBox = categoryView.findViewById<CheckBox>(R.id.checkbox_category)
                if (checkBox.isChecked) {
                    newCategories.add(categoryView.findViewById<TextView>(R.id.text_name).text.toString())
                }
            }
            onCommitCategoriesCallback(newCategories, allCategories)
            dialog.dismiss()
        }

        return dialog
    }
}