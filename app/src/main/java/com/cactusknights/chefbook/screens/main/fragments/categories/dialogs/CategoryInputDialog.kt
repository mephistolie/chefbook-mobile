package com.cactusknights.chefbook.screens.main.fragments.categories.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.cactusknights.chefbook.databinding.DialogCategoryInputBinding
import com.cactusknights.chefbook.models.Category

import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.ConfirmDialog


class CategoryInputDialog(category: Category? = null, val confirmListener: (Category) -> Unit, val deleteListener : (Category) -> Unit = {}) : DialogFragment() {

    private val category: Category = category ?: Category()
    private lateinit var binding: DialogCategoryInputBinding

    private val regex = Regex("^.*[а-яА-ЯёЁa-zA-Z0-9!@#\$%ˆ&*()_+-=\"№;%:?*]+?")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogCategoryInputBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (category.id != null) {
            binding.textTitle.text = resources.getText(R.string.edit_category)
            binding.inputName.setText(category.name)
            binding.inputCover.setText(category.cover)
            val fArray = arrayOfNulls<InputFilter>(1); fArray[0] = LengthFilter(if (category.cover.isEmpty() || regex.matches(category.cover)) 2 else category.cover.length)
            binding.inputCover.filters = fArray
            binding.btnDelete.visibility = View.VISIBLE
            binding.separator.visibility = View.VISIBLE
        }

        binding.inputCover.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty() && regex.matches(text)) {
                val fArray = arrayOfNulls<InputFilter>(1); fArray[0] = LengthFilter(2)
                binding.inputCover.filters = fArray
            } else {
                val fArray = arrayOfNulls<InputFilter>(1); fArray[0] = LengthFilter(if (text != null && text.isNotEmpty()) text.length else 8)
                binding.inputCover.filters = fArray
            }
        }

        binding.btnCancel.setOnClickListener { dialog.dismiss() }

        binding.btnDelete.setOnClickListener {
            ConfirmDialog { deleteListener(category); dialog.dismiss() }.show(requireActivity().supportFragmentManager, "Confirm") }

        binding.btnConfirm.setOnClickListener {
            val confirmedCategory = Category(category.id, category.remoteId, binding.inputName.text.toString(), binding.inputCover.text.toString())
            if (confirmedCategory.name.isNotEmpty()) {
                confirmListener(confirmedCategory)
                dialog.dismiss()
            }
        }

        return dialog
    }
}