package com.cactusknights.chefbook.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.adapters.CategoryEditAdapter

class CategoriesDialog(
    private val categories: ArrayList<String>,
    private val allCategories: ArrayList<String>,
    private val onCommitCategoriesCallback: (categories: ArrayList<String>, allCategories: ArrayList<String>) -> Unit
): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val categoriesAdapter = CategoryEditAdapter(allCategories, categories)

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_categories)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val categoriesRecyclerView = dialog.findViewById<RecyclerView>(R.id.categories_recycler_view)
        categoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        categoriesRecyclerView.adapter = categoriesAdapter


        val newCategory = dialog.findViewById<TextView>(R.id.new_category)
        val addCategory = dialog.findViewById<ImageButton>(R.id.add_category)

        val cancel = dialog.findViewById<ImageButton>(R.id.cancel)
        val confirm = dialog.findViewById<ImageButton>(R.id.confirm)

        addCategory.setOnClickListener {
            val newCategoryText = newCategory.text.toString()
            if (newCategoryText != "" && newCategoryText !in allCategories) {
                allCategories.add(newCategoryText)
                categoriesAdapter.notifyItemInserted(allCategories.size-1)
                newCategory.text = ""
            } else {
                newCategory.text = ""
            }
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        confirm.setOnClickListener {
            val newCategories = arrayListOf<String>()
            for (categoryView in categoriesRecyclerView.children) {
                val checkBox = categoryView.findViewById<CheckBox>(R.id.category_checkbox)
                if (checkBox.isChecked) {
                    newCategories.add(categoryView.findViewById<TextView>(R.id.category_name).text.toString())
                }
            }
            onCommitCategoriesCallback(newCategories, allCategories)
            dialog.dismiss()
        }

        return dialog
    }
}